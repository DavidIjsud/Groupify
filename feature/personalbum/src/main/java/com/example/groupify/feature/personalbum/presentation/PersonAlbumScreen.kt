// feature/personalbum/src/main/java/com/example/groupify/feature/personalbum/presentation/PersonAlbumScreen.kt
package com.example.groupify.feature.personalbum.presentation

import android.Manifest
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            hasPermission = true
        } else {
            viewModel.onPermissionDenied()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let { viewModel.onEvent(PersonAlbumContract.UiEvent.PickQueryPhoto(it.toString())) }
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PersonAlbumContract.UiEffect.ShowError -> errorMessage = effect.message
                is PersonAlbumContract.UiEffect.ShareUris -> {
                    val parsedUris = effect.uris.map { Uri.parse(it) }
                    val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                        type = "image/*"
                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(parsedUris))
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            clipData = ClipData.newUri(
                                context.contentResolver,
                                "Image",
                                parsedUris.first(),
                            ).also { clip ->
                                parsedUris.drop(1).forEach { uri ->
                                    clip.addItem(ClipData.Item(uri))
                                }
                            }
                        }
                    }
                    context.startActivity(Intent.createChooser(intent, "Share via"))
                }
            }
        }
    }

    val busy = uiState.isIndexing || uiState.isSearching

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Button(onClick = { permissionLauncher.launch(permission) }) {
            Text("Request Permission")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.StartIndexing) },
            enabled = hasPermission && !busy,
        ) {
            Text(if (uiState.isIndexing) "Indexing… (${uiState.indexedCount})" else "Start Indexing")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { galleryLauncher.launch("image/*") },
            enabled = hasPermission && !busy,
        ) {
            Text("Upload Photo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.StartSearch) },
            enabled = hasPermission && uiState.queryPhotoUri != null && !busy,
        ) {
            Text(if (uiState.isSearching) "Searching…" else "Start Search")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.ShareMatches) },
            enabled = uiState.matchUris.isNotEmpty(),
        ) {
            Text("Share Matches")
        }

        Spacer(modifier = Modifier.height(16.dp))

        uiState.queryPhotoUri?.let { uri ->
            Text("Selected: …${uri.takeLast(40)}")
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (uiState.indexedCount > 0) {
            Text("Indexed: ${uiState.indexedCount}")
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (uiState.matchUris.isNotEmpty()) {
            Text("Matches found: ${uiState.matchUris.size}")
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
            ) {
                items(uiState.matchUris.take(20)) { uri ->
                    Text(
                        text = "…${uri.takeLast(50)}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    HorizontalDivider()
                }
            }
        }

        errorMessage?.let { msg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = msg, color = MaterialTheme.colorScheme.error)
        }
    }
}
