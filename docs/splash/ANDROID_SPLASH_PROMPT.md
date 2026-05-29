# Groupify — Android Splash Screen Spec (for Claude Code)

## Goal
Implement **only** the Android launch / splash screen using the Android 12+ Splash Screen API (`androidx.core:core-splashscreen`). Do not modify any other screen.

## Visual reference
- `splash-reference.png` (in `handoff/`) — iOS reference; Android version is a faithful adaptation within the constraints below
- `ic_splash_foreground.svg` — the Groupify brand mark
- `ic_splash_background.svg` — black + violet radial glow

## Constraints of the Android 12+ Splash API (read this first)
The system splash only supports:
1. A solid background color
2. A centered icon (adaptive — fg + bg layers, auto-clipped to circle on API 31+)
3. An optional branding image at the bottom (we don't use this)

You **cannot** render custom text ("Groupify" wordmark) on the system splash. Don't try. The wordmark animates in on the first real screen instead (see "First-screen animation" below).

## Spec

| Element | Spec |
|---|---|
| Background color | `#000000` (pure black) — set as `windowSplashScreenBackground` |
| Splash icon foreground | `ic_splash_foreground.svg` — convert to `drawable/ic_splash_foreground.xml` (vector drawable) |
| Splash icon background | `ic_splash_background.svg` — convert to `drawable/ic_splash_background.xml` (carries the violet glow) |
| Adaptive icon resource | `mipmap/ic_splash.xml` referencing the two layers above |
| Icon size on screen | Default (192dp safe zone, ~108dp visible) — do not override |
| Animation | None — use the default system reveal |

## Implementation steps

1. Add the dependency in `app/build.gradle.kts`:
   ```kotlin
   implementation("androidx.core:core-splashscreen:1.0.1")
   ```

2. Convert both SVGs to Android vector drawables (Android Studio: right-click `drawable/` → New → Vector Asset → Local file). Place as:
   - `app/src/main/res/drawable/ic_splash_foreground.xml`
   - `app/src/main/res/drawable/ic_splash_background.xml`

3. Create the adaptive icon at `app/src/main/res/mipmap-anydpi-v26/ic_splash.xml`:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
       <background android:drawable="@drawable/ic_splash_background"/>
       <foreground android:drawable="@drawable/ic_splash_foreground"/>
   </adaptive-icon>
   ```

4. Define the splash theme in `app/src/main/res/values/themes.xml`:
   ```xml
   <style name="Theme.Groupify.Splash" parent="Theme.SplashScreen">
       <item name="windowSplashScreenBackground">#000000</item>
       <item name="windowSplashScreenAnimatedIcon">@mipmap/ic_splash</item>
       <item name="postSplashScreenTheme">@style/Theme.Groupify</item>
   </style>
   ```

5. In `AndroidManifest.xml`, set the splash theme on the launcher activity:
   ```xml
   <activity
       android:name=".MainActivity"
       android:theme="@style/Theme.Groupify.Splash"
       android:exported="true">
       ...
   </activity>
   ```

6. In the launcher activity's `onCreate` (BEFORE `super.onCreate`):
   ```kotlin
   installSplashScreen()
   super.onCreate(savedInstanceState)
   setTheme(R.style.Theme_Groupify)  // swap back to the real theme
   ```

## First-screen animation (optional but recommended)
Because the system splash can't show the wordmark, fade in "Groupify" on the first real screen for ~250ms after the splash dismisses, then transition to Home. This gives parity with the iOS splash without breaking the Android pattern.

## Do not
- Do not create a custom `Activity` that mimics a splash with `Thread.sleep` or a delay. That is the legacy pattern and is forbidden on Android 12+.
- Do not change the app icon (`ic_launcher`) — that's a separate task.
- Do not add the wordmark to the splash screen drawable. The system will crop it.
- Do not change the splash duration. The system controls it based on cold-start time.

## Acceptance
- Cold launch on Android 12+ device: black screen with violet glow + centered Groupify mark → smooth transition to first screen.
- Android <12: same theme falls back gracefully (mark visible, glow may be cropped — acceptable).

---

**Confirm before editing:** list the files you plan to create/modify, then proceed.
