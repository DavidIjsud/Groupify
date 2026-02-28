// feature/personalbum/src/main/java/com/example/groupify/feature/personalbum/presentation/PersonAlbumScreen.kt
package com.example.groupify.feature.personalbum.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedTextField
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

    var personName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is PersonAlbumContract.UiEffect.ShowError -> errorMessage = effect.message
                is PersonAlbumContract.UiEffect.NavigateToAlbum -> { /* TODO: NavGraph wiring */ }
            }
        }
    }

    val busy = uiState.isIndexing || uiState.isMatching || uiState.isCreatingPerson || uiState.isLoadingAlbum

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

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = personName,
            onValueChange = { personName = it },
            label = { Text("Person name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                uiState.referencePhotoUri?.let { uri ->
                    viewModel.onEvent(PersonAlbumContract.UiEvent.CreatePerson(personName.trim(), uri))
                }
            },
            enabled = hasPermission &&
                personName.isNotBlank() &&
                uiState.referencePhotoUri != null &&
                !busy,
        ) {
            Text(if (uiState.isCreatingPerson) "Creating…" else "Create Person")
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

        when {
            uiState.isIndexing -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Indexing…")
            }
            uiState.isMatching -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Matching…")
            }
            uiState.isCreatingPerson -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Creating person…")
            }
            uiState.isLoadingAlbum -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading album…")
            }
        }

        errorMessage?.let { msg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = msg, color = MaterialTheme.colorScheme.error)
        }

        if (uiState.persons.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text("People", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
            ) {
                items(uiState.persons) { person ->
                    Text(
                        text = person.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(PersonAlbumContract.UiEvent.LoadAlbum(person.id))
                            }
                            .padding(vertical = 12.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    HorizontalDivider()
                }
            }
        }

        if (uiState.selectedPersonId != null) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Matched photos: ${uiState.albumUris.size}",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.albumUris.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                ) {
                    items(uiState.albumUris.take(20)) { uri ->
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
        }
    }
}
