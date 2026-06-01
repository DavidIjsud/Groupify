# PROJECT_CONTEXT.md

> Onboarding context for future AI sessions (and humans). Describes what Groupify is,
> how it's built, the conventions to honor, and the rules to follow when changing it.
> Last verified against the codebase: 2026-05-21.

---

## 1. What this app is

On-device face recognition / photo search for Android. Branded **in the UI as
"PhotoMatch — Find similar photos instantly."** (The Gradle/package name is `groupify`;
the public-facing product name is **PhotoMatch**. Be aware of this mismatch.)

**Core user flow:**
1. User picks a photo from the gallery or captures one with the camera.
2. ML Kit detects the faces in that *query* photo.
3. User selects which face(s) to search for (multi-select chips).
4. The app makes sure the gallery has been indexed (a background WorkManager job that
   detects + embeds every face in every photo), then matches the selected face(s)
   against the stored embeddings.
5. Matches render as a 2-column grid the user can multi-select and share.

**Everything runs on-device.** There are no network calls for the core feature — ML Kit
Face Detection and a bundled FaceNet TFLite model do all the work. The only cloud
dependency is Firebase Analytics + Crashlytics (telemetry only).

---

## 2. Tech stack & key versions

| Area | Choice |
|------|--------|
| Language / build | Kotlin 2.3.20, AGP 8.12.3, KSP 2.3.2 |
| SDK | compileSdk/targetSdk 36, minSdk 24 |
| App id / package | `com.palmyrasoft.groupify` (versionCode 9, versionName 1.0.7) |
| ABIs | arm64-v8a, armeabi-v7a |
| DI | Hilt 2.58 (KSP, not kapt) |
| Database | Room 2.8.4 |
| Face detection | ML Kit Face Detection 16.1.7 |
| Face embeddings | **LiteRT 1.2.0** (`com.google.ai.edge.litert:litert`) — replaced TensorFlow Lite. Code still imports `org.tensorflow.lite.Interpreter`; LiteRT keeps that package for compatibility. |
| Model asset | `app/src/main/assets/facenet.tflite` (~23 MB, 160×160 input) |
| Image loading | Coil 2.7.0 |
| EXIF | androidx.exifinterface 1.4.2 |
| Background work | WorkManager 2.11.2 + Hilt-Work 1.3.0 |
| UI | Jetbrains Compose (BOM 2026.03.01), Material 3, single-Activity |
| Telemetry | Firebase BoM 34.12.0 (Analytics + Crashlytics) |
| Startup | androidx.core:core-splashscreen 1.2.0 |

---

## 3. Module structure

```
:app                         Application + entry point only
  GroupifyApp                @HiltAndroidApp, Configuration.Provider (WorkManager),
                             registers MediaStoreObserver, gates auto-indexing
  MainActivity               @AndroidEntryPoint, installSplashScreen(), hosts PersonAlbumScreen
  Firebase, splash theme, WorkManager dep re-declaration

:feature:personalbum         The entire app logic AND the Compose UI live here
  domain/                    pure Kotlin: models, ports (interfaces), use cases, util
  data/                      ml/, local/ (Room), source/, thumbnail/, prefs/, repository/
  presentation/              MVI Contract, ViewModel, Compose screen, UI models
  di/                        Hilt modules
  workers/                   WorkManager worker, notifications, MediaStore observer
```

**Why `:app` re-declares WorkManager + Hilt-Work deps:** the feature module exposes them
via `implementation` scope, so they are *not* transitive to `:app`. `:app` needs them so
that (1) `androidx.work.impl.foreground.SystemForegroundService` and
`androidx.startup.InitializationProvider` resolve in its `AndroidManifest.xml`, and
(2) `HiltWorkerFactory` compiles in `GroupifyApp`. This is intentional — see the comment in
`app/build.gradle.kts`.

---

## 4. Architecture: Clean Architecture + MVI

Three layers inside `:feature:personalbum`, dependencies point inward (presentation → domain ← data):

- **Domain** — framework-free Kotlin. Models, **ports** (interfaces), and use cases.
  Every use case is `class XUseCase @Inject constructor(...)` with `operator fun invoke(...)`.
  No Android imports here (the one pragmatic exception: `IndexFacesAndEmbeddingsUseCase`
  uses `android.os.SystemClock`/`Log` behind `BuildConfig.DEBUG` for timing).
- **Data** — implements the domain ports. All Android coupling (bitmaps, MediaStore, Room,
  TFLite, `@ApplicationContext`, FileProvider) is confined here.
- **Presentation** — MVI. `PersonAlbumContract` defines `UiState` / `UiEvent` / `UiEffect`;
  `PersonAlbumViewModel` (`@HiltViewModel`) reduces events into a `StateFlow<UiState>` and
  emits one-shot effects via a `SharedFlow<UiEffect>`. `PersonAlbumScreen` is the Compose UI.

### MVI contract shape
- `UiState` — immutable data class, single source of truth, exposed as `StateFlow`.
- `UiEvent` — `sealed interface` of everything the UI can do; dispatched through one
  `onEvent(event)` entry point.
- `UiEffect` — `sealed interface` of one-shot side effects (e.g. `ShareUris`); collected in
  a `LaunchedEffect` and never re-delivered on recomposition.

### Domain models (`domain/model`)
- `Photo(id, uri, dateTaken)`
- `BoundingBox(left, top, right, bottom: Float)`
- `Face(photoId, boundingBox, embedding: FloatArray)` — custom `equals`/`hashCode` (FloatArray)
- `Person(id, name, referenceEmbedding: FloatArray)` — same. **Persisted but not wired into the live UI** (see §9).
- `DetectedFace(boundingBox, trackingId, smiling/eye probabilities)`
- `QueryFace(id: Int, boundingBox)` — a face in the user's query photo
- `PhotoMatch(uri, score: Float)`

### Ports (`domain/.../`)
- `PhotoRepository`, `FaceIndexRepository`, `PersonRepository`
- `FaceDetector` (`detectFaces(uri): List<DetectedFace>`)
- `FaceEmbedder` (`embedFace(uri, BoundingBox): FloatArray`)
- `QueryFaceThumbnailGenerator` (`generate(uri, faces): Map<faceId, thumbUri>`)

### Dependency injection
- `PersonAlbumModule` — **`@Binds` abstract** module mapping each port to its impl.
- `DatabaseModule`, `WorkManagerModule` — **`@Provides object`** modules (needed because
  `@Binds` can't provide framework-constructed types like `RoomDatabase`/`WorkManager`).
- All `@InstallIn(SingletonComponent::class)`. Repos/detector/embedder/db/workmanager are `@Singleton`.

---

## 5. The key subsystems (and the non-obvious details)

### 5.1 Matching engine — `SearchByPhotoUseCase`
The heart of the app. `invoke(queryUri, selectedFaces, threshold, margin = 0.06)`:
- Embeds each selected query face, then scans **all** stored faces, tracking the **top-2**
  cosine similarities **per candidate photo** using primitive `HashMap<String, Float>`
  (no Pair/object allocations in the hot loop).
- Accepts a photo only if `best >= threshold` **AND** `best − second >= margin`.
  The margin test rejects ambiguous photos where two different people both score near the
  query — a reliable false-positive signal. Single-face photos auto-pass (second = `-∞`).
- Excludes the query photo itself; returns `List<PhotoMatch>` sorted by score desc.

### 5.2 Detector ↔ embedder coordinate contract
`MlKitFaceDetector` and `TFLiteFaceNetEmbedder` **both** decode via `BitmapDecodeUtils` at
`maxDim = 1024`, so they compute the *same* `inSampleSize`. The detector scales bounding
boxes **up** (`× sampleSize`) to full-resolution coordinates; the embedder scales them back
**down** (`÷ sampleSize`) to crop. **This contract is load-bearing — if you change `maxDim`
in one place you must change it in both, or face crops will be misaligned.**

### 5.3 `BitmapDecodeUtils` (internal object)
`decodeSampledAndRotatedBitmap(ctx, uri, maxDim) → (ARGB_8888 bitmap, sampleSize)`.
Three passes: bounds-only decode, sampled decode, EXIF rotation. Always returns ARGB_8888
(handles HARDWARE-config bitmaps). Callers own recycling.

### 5.4 Embedder performance — `TFLiteFaceNetEmbedder`
- Lazy `Interpreter` with `setNumThreads(min(4, cpus).coerceAtLeast(2))`.
- Input 160×160, normalized `(pixel − 127.5) / 127.5`; output is **L2-normalized** so cosine
  similarity reduces to a dot product.
- `inferenceMutex` serializes inference (Interpreter is not thread-safe).
- Per-photo bitmap cache (`decodeMutex`, last URI only): during indexing every face in a
  photo reuses one decode. **Do not recycle the source bitmap held in the cache.**

### 5.5 Indexing — `IndexFacesAndEmbeddingsUseCase`
- Upserts all photos → fetches unindexed → per photo: detect faces, embed each.
- **Batches** DB writes every `BATCH_SIZE = 50` photos (one Room transaction per batch
  instead of per photo); progress still emits per-photo for a smooth bar.
- A photo that throws is **not** marked indexed → it retries on the next run. A face that
  fails to embed is skipped without aborting the whole photo.

### 5.6 Background work — `IndexFacesWorker`
- `@HiltWorker` `CoroutineWorker` run as a **foreground service** (`dataSync`). Calls
  `setForeground()` up front and on every progress tick; overrides `getForegroundInfo()`
  for API 31+ pre-declaration.
- On success → `IndexingOnboardingPrefs.markInitialIndexComplete()` + completion notification.
- `WORK_NAME = "face_indexing"`; enqueue via `enqueueOneTime()` using `ExistingWorkPolicy.KEEP`
  so concurrent triggers never duplicate the worker.
- `PersonAlbumViewModel.awaitIndexing()` observes by **unique work name** (not request id)
  via `getWorkInfosForUniqueWorkFlow(...).transformWhile`, so progress survives the user
  leaving and returning to the screen.

### 5.7 Opt-in auto-indexing gate
`IndexingOnboardingPrefs.hasCompletedInitialIndex()` gates **all** automatic indexing.
On first install nothing indexes silently — the user must start a search, acknowledge the
onboarding dialog, and let the first run finish. Only afterward do `GroupifyApp.onCreate`
(launch reindex) and the 3-second-debounced `MediaStoreObserver` (new-photo reindex) start
enqueuing work. This is a deliberate privacy choice — preserve it.

### 5.8 Persistence (Room v3)
`PersonAlbumDatabase` v3, `exportSchema = false`, `fallbackToDestructiveMigration()`.
Entities: `PhotoEntity`, `FaceEmbeddingEntity` (photoId indexed, embedding stored as a
`ByteArray` blob via `Converters` using `ByteBuffer` native order), `PersonEntity`.
The DB is a **rebuildable cache** — destructive migration is acceptable because a schema
bump just forces re-indexing.

---

## 6. Presentation details
- `PersonAlbumScreen` is dark-themed (accent `#7B61FF`), one `LazyColumn`.
- Handles runtime permission itself (`READ_MEDIA_IMAGES` on API 33+, else `READ_EXTERNAL_STORAGE`).
- Gallery picker (`GetContent`) + camera (`TakePicture` → temp file via FileProvider authority
  `${packageName}.provider`).
- Query photo card draws the focused face's bounding box using `ContentScale.Fit` math.
- Face chips (LazyRow) show cropped circular thumbnails generated by
  `AndroidQueryFaceThumbnailGenerator` (1.4× padded square, 128px JPEG in cacheDir).
- Match grid: 2 columns, score badge per tile, long-press to enter multi-select, share via
  `UiEffect.ShareUris` → `ACTION_SEND_MULTIPLE` chooser built in the Composable.
- `DEFAULT_MATCH_THRESHOLD = 0.60f` (hardcoded in the ViewModel).

---

## 7. SEO strategy

> _Intentionally left blank for now — to be filled in a later session._

---

## 8. Conventions & rules to follow

**Architecture**
- Respect the layer boundaries: presentation → domain ← data. **Domain stays framework-free**
  (no Android imports beyond the existing `BuildConfig.DEBUG` timing exception).
- New behavior that crosses a boundary goes through a **port** (interface in domain, impl in
  data, bound in `PersonAlbumModule`). Don't let the ViewModel touch data classes directly.
- Use cases are single-responsibility, `@Inject constructor`, `operator fun invoke`.
- **Never leak domain models into `UiState`** — map to a `*UiModel` (e.g. `MatchUiModel`,
  `QueryFaceUiModel`).

**MVI**
- All UI intent flows through `onEvent(UiEvent)`. Add a case to the `sealed interface` and the
  `when`. One-shot things (navigation, share, toasts) are `UiEffect`, not state.
- State is immutable; update with `_uiState.update { it.copy(...) }`.

**ML / bitmaps**
- Decode through `BitmapDecodeUtils`; keep detector and embedder `maxDim` identical.
- Recycle bitmaps you create; never recycle the embedder's cached source bitmap.
- Keep TFLite calls inside `inferenceMutex`.

**Background work**
- Enqueue indexing only via `IndexFacesWorker.enqueueOneTime()` (KEEP policy).
- Do not trigger auto-indexing without checking `hasCompletedInitialIndex()` — it protects the
  first-run opt-in.

**Build / config**
- The feature module needs `buildConfig = true` (it's an AGP 8.x library that reads
  `BuildConfig.DEBUG`). All debug timing uses `SystemClock.elapsedRealtime()` behind a
  `BuildConfig.DEBUG` guard — keep it out of release paths.
- Add dependencies via the `gradle/libs.versions.toml` version catalog, not hardcoded strings
  (the Firebase/splash deps in `:app` are the existing exceptions).
- If you bump the Room schema, also bump `version` in `PersonAlbumDatabase` (destructive
  migration will wipe the index — acceptable, but be deliberate).

**Privacy**
- Core matching must remain **on-device**. Don't add network calls to the detect/embed/match
  path. Firebase is telemetry only.

---

## 9. Known gaps / tech debt (don't mistake for live features)
- **Dead "person album" path:** `Person`/`PersonEntity`/`PersonDao`/`PersonRepository`,
  `CreatePersonAlbumUseCase`, `GetPersonAlbumUseCase` are fully built but **not reachable from
  the UI**. The module and class names (`personalbum`, `PersonAlbumScreen/ViewModel`) reflect
  this abandoned earlier direction — they do **not** match the live "PhotoMatch / search-by-photo"
  product. Don't assume those names describe current behavior.
- **Superseded use cases still present:** `FindMatchingPhotosUseCase` (older single-reference
  matcher), `IndexPhotosUseCase`, `DetectFacesInPhotoUseCase`. The live matcher is
  `SearchByPhotoUseCase`; the live indexer is `IndexFacesAndEmbeddingsUseCase`.
- **Sensitivity slider:** strings exist (`60%`–`95%`) but there is no slider in the screen;
  the threshold is hardcoded at `0.60`.
- **Gallery query cap:** `AndroidMediaStorePhotoDataSource` queries at most **1000** photos —
  larger libraries are silently truncated.

---

## 10. Where to look (file map)

| Concern | File |
|---------|------|
| App entry / WorkManager config | `app/.../GroupifyApp.kt`, `MainActivity.kt` |
| MVI contract | `presentation/PersonAlbumContract.kt` |
| ViewModel (orchestration) | `presentation/PersonAlbumViewModel.kt` |
| Compose UI | `presentation/PersonAlbumScreen.kt` |
| Matching engine | `domain/usecase/SearchByPhotoUseCase.kt` |
| Indexing | `domain/usecase/IndexFacesAndEmbeddingsUseCase.kt` |
| Face detection | `data/ml/MlKitFaceDetector.kt` |
| Embeddings | `data/ml/TFLiteFaceNetEmbedder.kt` |
| Bitmap decode contract | `data/ml/BitmapDecodeUtils.kt` |
| Room DB | `data/local/PersonAlbumDatabase.kt` + `dao/`, `entity/` |
| MediaStore | `data/source/AndroidMediaStorePhotoDataSource.kt` |
| Background worker | `workers/IndexFacesWorker.kt`, `MediaStoreObserver.kt` |
| Opt-in gate | `data/prefs/IndexingOnboardingPrefs.kt` |
| DI | `di/PersonAlbumModule.kt`, `DatabaseModule.kt`, `WorkManagerModule.kt` |
| Version catalog | `gradle/libs.versions.toml` |
