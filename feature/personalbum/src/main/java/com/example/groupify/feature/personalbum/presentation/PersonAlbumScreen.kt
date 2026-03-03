package com.example.groupify.feature.personalbum.presentation

import android.Manifest
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.groupify.feature.personalbum.presentation.model.MatchUiModel
import com.example.groupify.feature.personalbum.presentation.model.QueryFaceUiModel
import java.io.File
import kotlin.math.roundToInt

private val AccentPurple = Color(0xFF7B61FF)
private val DarkBackground = Color(0xFF0E0E0E)
private val CardBackground = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF9E9E9E)
private val ErrorBackground = Color(0xFF3A1C1C)
private val ErrorText = Color(0xFFFF6B6B)
private val SelectedChipBackground = Color(0xFF2A2040)

@Composable
fun PersonAlbumScreen(
    viewModel: PersonAlbumViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var hasPermission by remember(permission) {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED,
        )
    }

    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            hasPermission = true
            pendingAction?.invoke()
        } else {
            viewModel.onPermissionDenied()
        }
        pendingAction = null
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let { viewModel.onEvent(PersonAlbumContract.UiEvent.PickQueryPhoto(it.toString())) }
    }

    var cameraOutputUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) {
            cameraOutputUri?.let {
                viewModel.onEvent(PersonAlbumContract.UiEvent.PickQueryPhoto(it.toString()))
            }
        }
    }

    fun launchGallery() {
        if (hasPermission) {
            galleryLauncher.launch("image/*")
        } else {
            pendingAction = { galleryLauncher.launch("image/*") }
            permissionLauncher.launch(permission)
        }
    }

    fun launchCamera() {
        runCatching {
            val tempFile = File.createTempFile("capture_", ".jpg", context.cacheDir)
            FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
        }.onSuccess { uri ->
            cameraOutputUri = uri
            cameraLauncher.launch(uri)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PersonAlbumContract.UiEffect.ShareUris -> {
                    if (effect.uris.isEmpty()) return@collect
                    val parsedUris = effect.uris.map { Uri.parse(it) }
                    val clip = ClipData.newUri(context.contentResolver, "Image", parsedUris.first())
                        .also { c -> parsedUris.drop(1).forEach { uri -> c.addItem(ClipData.Item(uri)) } }
                    val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                        type = "image/*"
                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(parsedUris))
                        clipData = clip
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share via"))
                }
            }
        }
    }

    val busy = uiState.isPreparingGallery || uiState.isDetecting

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "PhotoMatch",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Find similar photos instantly",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                    )
                }
            }

            // Query photo preview card with face bounding-box overlay
            item {
                QueryPhotoCard(
                    uiState = uiState,
                    busy = busy,
                    onTap = { launchGallery() },
                )
            }

            // Face chips — visible once faces are detected in the query photo
            if (uiState.queryFaces.isNotEmpty()) {
                item {
                    FaceSelectionSection(
                        uiState = uiState,
                        onToggle = { id -> viewModel.onEvent(PersonAlbumContract.UiEvent.ToggleFaceSelection(id)) },
                        onSelectAll = { viewModel.onEvent(PersonAlbumContract.UiEvent.SelectAllFaces) },
                        onClear = { viewModel.onEvent(PersonAlbumContract.UiEvent.ClearFaceSelection) },
                    )
                }
            }

            // Match sensitivity slider — shown once a query photo is selected
            if (uiState.selectedQueryPhotoUri != null) {
                item {
                    SensitivitySlider(
                        percent = uiState.matchSensitivityPercent,
                        onValueChange = { viewModel.onEvent(PersonAlbumContract.UiEvent.SetMatchSensitivity(it)) },
                    )
                }
            }

            // Take a Photo (camera) button
            item {
                OutlinedButton(
                    onClick = { launchCamera() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !busy,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (!busy) AccentPurple else TextSecondary),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AccentPurple,
                        disabledContentColor = TextSecondary,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Take a Photo")
                }
            }

            // Inline user message (errors, warnings)
            if (uiState.userMessage != null) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ErrorBackground)
                            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = uiState.userMessage!!,
                            color = ErrorText,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f),
                        )
                        if (!hasPermission) {
                            TextButton(onClick = {
                                pendingAction = { galleryLauncher.launch("image/*") }
                                permissionLauncher.launch(permission)
                            }) {
                                Text(
                                    text = "Grant",
                                    color = AccentPurple,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                        IconButton(
                            onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.DismissMessage) },
                            modifier = Modifier.size(32.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Dismiss",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }

            // Gallery preparation progress
            if (uiState.isPreparingGallery) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        if (uiState.preparingProgressTotal > 0) {
                            LinearProgressIndicator(
                                progress = {
                                    uiState.preparingProgressCurrent.toFloat() / uiState.preparingProgressTotal
                                },
                                modifier = Modifier.fillMaxWidth(),
                                color = AccentPurple,
                                trackColor = Color(0xFF3D3D3D),
                            )
                            Text(
                                text = "Indexing photos… ${uiState.preparingProgressCurrent} / ${uiState.preparingProgressTotal}",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        } else {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = AccentPurple,
                                trackColor = Color(0xFF3D3D3D),
                            )
                            Text(
                                text = "Preparing gallery…",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }

            // Start Detection button
            item {
                val hasSelectedFaces = uiState.queryFaces.any { it.isSelected }
                val startEnabled = uiState.selectedQueryPhotoUri != null &&
                    hasSelectedFaces &&
                    !busy &&
                    !uiState.isFaceLoading

                Button(
                    onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.StartDetection) },
                    enabled = startEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple,
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF2C2C2E),
                        disabledContentColor = TextSecondary,
                    ),
                ) {
                    if (busy || uiState.isFaceLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp,
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Text(
                        text = when {
                            uiState.isFaceLoading -> "Detecting faces…"
                            uiState.isPreparingGallery -> "Preparing gallery…"
                            uiState.isDetecting -> "Detecting…"
                            else -> "Start Detection"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            // Results section
            if (uiState.matches.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${uiState.matches.size} Similar Matches Found",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                items(uiState.matches.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        rowItems.forEach { match ->
                            MatchCard(match = match, modifier = Modifier.weight(1f))
                        }
                        if (rowItems.size == 1) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }

                item {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.ShareMatches) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AccentPurple),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentPurple),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share Matches")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Query photo preview card — image + ContentScale.Fit bounding-box overlay
// ---------------------------------------------------------------------------

@Composable
private fun QueryPhotoCard(
    uiState: PersonAlbumContract.UiState,
    busy: Boolean,
    onTap: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .clickable(enabled = !busy) { onTap() },
        contentAlignment = Alignment.Center,
    ) {
        if (uiState.selectedQueryPhotoUri != null) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val containerW = constraints.maxWidth.toFloat()
                val containerH = constraints.maxHeight.toFloat()

                // Capture intrinsic image size once the image loads
                var intrinsicSize by remember(uiState.selectedQueryPhotoUri) {
                    mutableStateOf<Size?>(null)
                }

                AsyncImage(
                    model = Uri.parse(uiState.selectedQueryPhotoUri),
                    contentDescription = "Selected query photo",
                    contentScale = ContentScale.Fit,
                    onSuccess = { state: AsyncImagePainter.State.Success ->
                        val s = state.painter.intrinsicSize
                        if (s.width > 0f && s.height > 0f) intrinsicSize = s
                    },
                    modifier = Modifier.fillMaxSize(),
                )

                // Draw bounding box for the focused face using ContentScale.Fit math
                val focusedFace = uiState.queryFaces.firstOrNull { it.id == uiState.focusedFaceId }
                val imgSize = intrinsicSize
                if (focusedFace != null && imgSize != null) {
                    val scale = minOf(containerW / imgSize.width, containerH / imgSize.height)
                    val offsetX = (containerW - imgSize.width * scale) / 2f
                    val offsetY = (containerH - imgSize.height * scale) / 2f
                    val bb = focusedFace.boundingBox

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(
                            color = AccentPurple,
                            topLeft = Offset(
                                x = bb.left * scale + offsetX,
                                y = bb.top * scale + offsetY,
                            ),
                            size = Size(
                                width = (bb.right - bb.left) * scale,
                                height = (bb.bottom - bb.top) * scale,
                            ),
                            style = Stroke(width = 3.dp.toPx()),
                        )
                    }
                }

                // Top-right check icon
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = AccentPurple,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(22.dp)
                        .background(Color(0xCC000000), CircleShape),
                )

                // Bottom bar: face-loading indicator or "tap to change" hint
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color(0x99000000))
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (uiState.isFaceLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = AccentPurple,
                                strokeWidth = 2.dp,
                            )
                            Text(
                                text = "Detecting faces…",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    } else {
                        Text(
                            text = "Tap to change photo",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        } else {
            // Empty state — upload prompt
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.CloudUpload,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(48.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tap to upload a photo",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select from your gallery",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Face selection section — header row, chips, count
// ---------------------------------------------------------------------------

@Composable
private fun FaceSelectionSection(
    uiState: PersonAlbumContract.UiState,
    onToggle: (Int) -> Unit,
    onSelectAll: () -> Unit,
    onClear: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Select who to search",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )
            TextButton(
                onClick = onSelectAll,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "Select all",
                    color = AccentPurple,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            TextButton(
                onClick = onClear,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "Clear",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 8.dp),
        ) {
            items(uiState.queryFaces, key = { "${uiState.selectedQueryPhotoUri}:${it.id}" }) { face ->
                FaceChip(face = face, onClick = { onToggle(face.id) })
            }
        }

        val selectedCount = uiState.queryFaces.count { it.isSelected }
        val faceWord = if (uiState.queryFaces.size == 1) "face" else "faces"
        Text(
            text = "$selectedCount of ${uiState.queryFaces.size} $faceWord selected",
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

// ---------------------------------------------------------------------------
// Individual face chip — circular thumbnail + label + selection badge
// ---------------------------------------------------------------------------

@Composable
private fun FaceChip(
    face: QueryFaceUiModel,
    onClick: () -> Unit,
) {
    val borderColor = if (face.isSelected) AccentPurple else Color(0xFF3D3D3D)
    val bgColor = if (face.isSelected) SelectedChipBackground else CardBackground

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Circular thumbnail with selection badge in bottom-right
            Box(modifier = Modifier.size(40.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF2C2C2E)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (face.thumbnailUri != null) {
                        AsyncImage(
                            model = Uri.parse(face.thumbnailUri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

                // Small check badge anchored at bottom-end of the 40dp box
                if (face.isSelected) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(AccentPurple),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(9.dp),
                        )
                    }
                }
            }

            Text(
                text = face.label,
                color = if (face.isSelected) Color.White else TextSecondary,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (face.isSelected) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Match sensitivity slider
// ---------------------------------------------------------------------------

@Composable
private fun SensitivitySlider(
    percent: Int,
    onValueChange: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Match sensitivity",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "$percent%",
                color = AccentPurple,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Slider(
            value = percent.toFloat(),
            onValueChange = { onValueChange(it.roundToInt()) },
            valueRange = 60f..95f,
            colors = SliderDefaults.colors(
                thumbColor = AccentPurple,
                activeTrackColor = AccentPurple,
                inactiveTrackColor = Color(0xFF3D3D3D),
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "60%", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
            Text(text = "95%", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
        }
    }
}

// ---------------------------------------------------------------------------
// Match result card
// ---------------------------------------------------------------------------

@Composable
private fun MatchCard(
    match: MatchUiModel,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp)),
    ) {
        AsyncImage(
            model = Uri.parse(match.uri),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xBB000000))
                .padding(horizontal = 6.dp, vertical = 3.dp),
        ) {
            Text(
                text = "${match.scorePercent}%",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
