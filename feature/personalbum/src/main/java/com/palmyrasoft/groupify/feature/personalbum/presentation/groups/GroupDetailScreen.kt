package com.palmyrasoft.groupify.feature.personalbum.presentation.groups

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.palmyrasoft.groupify.feature.personalbum.R
import com.palmyrasoft.groupify.feature.personalbum.presentation.GroupifyTokens
import com.palmyrasoft.groupify.feature.personalbum.presentation.util.shareImageUris

@Composable
fun GroupDetailScreen(
    groupId: String,
    onBack: () -> Unit,
    onAddMore: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val group by remember(groupId) { viewModel.observeGroup(groupId) }
        .collectAsStateWithLifecycle(initialValue = null)

    var menuOpen by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().background(GroupifyTokens.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Nav row
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CircleIconButton(
                    icon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.groups_cd_back), tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(20.dp)) },
                    onClick = onBack,
                )
                Box {
                    CircleIconButton(
                        icon = { Icon(Icons.Filled.MoreVert, stringResource(R.string.groups_cd_more), tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(20.dp)) },
                        onClick = { menuOpen = true },
                    )
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.groups_menu_delete), color = GroupifyTokens.Danger) },
                            onClick = {
                                menuOpen = false
                                showDeleteDialog = true
                            },
                        )
                    }
                }
            }

            // Header
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 14.dp)) {
                Text(
                    text = stringResource(R.string.groups_detail_eyebrow).uppercase(),
                    color = GroupifyTokens.Accent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = group?.name ?: "",
                    color = GroupifyTokens.TextPrimary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = pluralStringResource(R.plurals.groups_photos_count, group?.photoCount ?: 0, group?.photoCount ?: 0) +
                        " · " +
                        pluralStringResource(R.plurals.groups_faces_count, group?.faceCount ?: 0, group?.faceCount ?: 0),
                    color = GroupifyTokens.TextSecondary,
                    fontSize = 14.sp,
                )
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ActionButton(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.groups_btn_share),
                    icon = { Icon(Icons.Filled.Share, null, tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(17.dp)) },
                    filled = true,
                    onClick = { group?.let { shareImageUris(context, it.photoUris) } },
                )
                ActionButton(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.groups_btn_add_more),
                    icon = { Icon(Icons.Filled.CreateNewFolder, null, tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(17.dp)) },
                    filled = false,
                    onClick = onAddMore,
                )
            }

            // Photo grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 130.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(group?.photoUris ?: emptyList()) { uri ->
                    Box(modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(12.dp)).background(GroupifyTokens.Field)) {
                        AsyncImage(
                            model = Uri.parse(uri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.groups_delete_title), color = GroupifyTokens.TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.groups_delete_body), color = GroupifyTokens.TextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteGroup(groupId)
                    onBack()
                }) {
                    Text(stringResource(R.string.groups_delete_confirm), color = GroupifyTokens.Danger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.groups_delete_cancel), color = GroupifyTokens.TextSecondary)
                }
            },
            containerColor = GroupifyTokens.Card,
        )
    }
}

@Composable
private fun CircleIconButton(icon: @Composable () -> Unit, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(38.dp).clip(CircleShape).background(GroupifyTokens.Card).clickable { onClick() },
        contentAlignment = Alignment.Center,
        content = { icon() },
    )
}

@Composable
private fun ActionButton(
    label: String,
    icon: @Composable () -> Unit,
    filled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (filled) GroupifyTokens.Accent else GroupifyTokens.Card)
            .then(if (filled) Modifier else Modifier.border(1.dp, GroupifyTokens.Hairline, RoundedCornerShape(14.dp)))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(Modifier.width(6.dp))
        Text(label, color = GroupifyTokens.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}
