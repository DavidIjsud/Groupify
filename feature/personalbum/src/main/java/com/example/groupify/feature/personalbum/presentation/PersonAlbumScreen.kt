// feature/personalbum/src/main/.../presentation/PersonAlbumScreen.kt
package com.example.groupify.feature.personalbum.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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

    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PersonAlbumContract.UiEffect.ShowError -> errorMessage = effect.message
                is PersonAlbumContract.UiEffect.NavigateToAlbum -> { /* TODO: Step 5 */ }
            }
        }
    }

    val busy = uiState.isIndexing || uiState.isMatching

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = { permissionLauncher.launch(permission) }) {
            Text("Request Permission")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.StartIndexing) },
            enabled = hasPermission && !busy,
        ) {
            Text("Start Indexing")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.TestFaceDetection) },
            enabled = hasPermission && !busy,
        ) {
            Text("Test Face Detection")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onEvent(PersonAlbumContract.UiEvent.UseLatestPhotoAsReference) },
            enabled = hasPermission && !busy,
        ) {
            Text("Use Latest Photo as Reference")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                uiState.referencePhotoUri?.let { uri ->
                    viewModel.onEvent(PersonAlbumContract.UiEvent.FindMatches(uri))
                }
            },
            enabled = hasPermission && uiState.referencePhotoUri != null && !busy,
        ) {
            Text("Find Matches")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Indexed: ${uiState.indexedCount}")

        if (uiState.faceCount > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Faces detected: ${uiState.faceCount}")
        }

        uiState.referencePhotoUri?.let { uri ->
            Spacer(modifier = Modifier.height(4.dp))
            Text("Reference: …${uri.takeLast(40)}")
        }

        if (uiState.matchCount > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Matches found: ${uiState.matchCount}")
        }

        if (uiState.isIndexing) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Indexing...")
        }

        if (uiState.isMatching) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Matching...")
        }

        errorMessage?.let { msg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = msg, color = MaterialTheme.colorScheme.error)
        }
    }
}
