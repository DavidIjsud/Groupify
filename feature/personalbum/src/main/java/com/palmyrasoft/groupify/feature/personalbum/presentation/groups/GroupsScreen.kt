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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.palmyrasoft.groupify.feature.personalbum.R
import com.palmyrasoft.groupify.feature.personalbum.presentation.GroupifyTokens
import com.palmyrasoft.groupify.feature.personalbum.presentation.model.GroupUiModel
import com.palmyrasoft.groupify.feature.personalbum.presentation.util.relativeUpdatedLabel

@Composable
fun GroupsScreen(
    onOpenGroup: (String) -> Unit,
    onStartMatch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupsViewModel = hiltViewModel(),
) {
    val state by viewModel.listState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize().background(GroupifyTokens.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.groups_title),
                        color = GroupifyTokens.TextPrimary,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    if (state.hasAnyGroups) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = pluralStringResource(R.plurals.groups_count, state.totalGroups, state.totalGroups) +
                                " · " +
                                pluralStringResource(R.plurals.groups_photos_saved, state.totalPhotos, state.totalPhotos),
                            color = GroupifyTokens.TextSecondary,
                            fontSize = 14.sp,
                        )
                    }
                }
            }

            if (!state.hasAnyGroups) {
                GroupsEmptyState(onStartMatch = onStartMatch)
            } else {
                SearchField(
                    query = state.query,
                    onQueryChange = viewModel::onSearchQueryChange,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                if (state.groups.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Text(
                            text = stringResource(R.string.groups_search_empty, state.query.trim()),
                            color = GroupifyTokens.TextSecondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 48.dp, start = 24.dp, end = 24.dp),
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 130.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        items(state.groups, key = { it.id }) { group ->
                            GroupCard(group = group, onClick = { onOpenGroup(group.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .background(GroupifyTokens.Card)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Search, null, tint = GroupifyTokens.TextSecondary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    stringResource(R.string.groups_search_placeholder),
                    color = GroupifyTokens.TextDim,
                    fontSize = 15.sp,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(color = GroupifyTokens.TextPrimary, fontSize = 15.sp),
                cursorBrush = SolidColor(GroupifyTokens.Accent),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun GroupsEmptyState(onStartMatch: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp).padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Stacked folder hero
        Box(modifier = Modifier.size(width = 140.dp, height = 116.dp), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .padding(start = 0.dp, bottom = 0.dp)
                    .size(width = 124.dp, height = 92.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(GroupifyTokens.Accent),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.CreateNewFolder, null, tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(42.dp))
            }
        }
        Spacer(Modifier.height(28.dp))
        Text(
            stringResource(R.string.groups_empty_title),
            color = GroupifyTokens.TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.groups_empty_body),
            color = GroupifyTokens.TextSecondary,
            fontSize = 15.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        Spacer(Modifier.height(28.dp))
        Row(
            modifier = Modifier
                .height(50.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(GroupifyTokens.Accent)
                .clickable { onStartMatch() }
                .padding(horizontal = 26.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.AutoAwesome, null, tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.groups_btn_start_match), color = GroupifyTokens.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun GroupCard(group: GroupUiModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(GroupifyTokens.Card)
            .border(1.dp, GroupifyTokens.Hairline, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(12.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp))) {
            CollageGrid(uris = group.previewUris)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xA6000000))
                    .padding(horizontal = 9.dp, vertical = 4.dp),
            ) {
                Text("${group.photoCount}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = group.name,
            color = GroupifyTokens.TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = pluralStringResource(R.plurals.groups_faces_count, group.faceCount, group.faceCount) +
                " · " + relativeUpdatedLabel(group.updatedAt),
            color = GroupifyTokens.TextSecondary,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** 2×2 collage; cells beyond the available photos render as empty tinted tiles. */
@Composable
private fun CollageGrid(uris: List<String>) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(2) { row ->
            Row(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(2) { col ->
                    val index = row * 2 + col
                    val uri = uris.getOrNull(index)
                    Box(modifier = Modifier.weight(1f).fillMaxSize().background(GroupifyTokens.Field)) {
                        if (uri != null) {
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
    }
}
