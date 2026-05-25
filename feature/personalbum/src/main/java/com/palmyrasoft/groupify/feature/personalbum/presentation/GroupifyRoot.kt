package com.palmyrasoft.groupify.feature.personalbum.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.palmyrasoft.groupify.feature.personalbum.R
import com.palmyrasoft.groupify.feature.personalbum.presentation.groups.GroupDetailScreen
import com.palmyrasoft.groupify.feature.personalbum.presentation.groups.GroupsScreen
import com.palmyrasoft.groupify.feature.personalbum.presentation.groups.GroupsViewModel

private enum class GroupifyTab { Home, Groups }

/**
 * App root: hosts the existing Home (PersonAlbumScreen) and the new Groups destination behind a
 * floating bottom tab bar, plus a pushed Group-detail screen.
 *
 * Both ViewModels are obtained here (Activity-scoped), so switching tabs never tears down the
 * Home ViewModel — an in-progress match keeps running and its results persist across switches.
 */
@Composable
fun GroupifyRoot() {
    val personAlbumViewModel: PersonAlbumViewModel = hiltViewModel()
    val groupsViewModel: GroupsViewModel = hiltViewModel()

    var tab by rememberSaveable { mutableStateOf(GroupifyTab.Home) }
    var detailGroupId by rememberSaveable { mutableStateOf<String?>(null) }

    // System back on the detail screen returns to the Groups list rather than exiting.
    BackHandler(enabled = detailGroupId != null) { detailGroupId = null }

    Box(modifier = Modifier.fillMaxSize().background(GroupifyTokens.Background)) {
        val openDetailId = detailGroupId
        if (openDetailId != null) {
            GroupDetailScreen(
                groupId = openDetailId,
                onBack = { detailGroupId = null },
                onAddMore = {
                    detailGroupId = null
                    tab = GroupifyTab.Home
                },
                viewModel = groupsViewModel,
            )
        } else {
            when (tab) {
                GroupifyTab.Home -> PersonAlbumScreen(viewModel = personAlbumViewModel)
                GroupifyTab.Groups -> GroupsScreen(
                    onOpenGroup = { detailGroupId = it },
                    onStartMatch = { tab = GroupifyTab.Home },
                    viewModel = groupsViewModel,
                )
            }

            GroupifyTabBar(
                selected = tab,
                onSelect = { tab = it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
            )
        }
    }
}

@Composable
private fun GroupifyTabBar(
    selected: GroupifyTab,
    onSelect: (GroupifyTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(GroupifyTokens.Card.copy(alpha = 0.95f))
            .border(1.dp, GroupifyTokens.Hairline, RoundedCornerShape(32.dp))
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TabItem(
            icon = Icons.Filled.Home,
            label = stringResource(R.string.groups_tab_home),
            active = selected == GroupifyTab.Home,
            onClick = { onSelect(GroupifyTab.Home) },
        )
        TabItem(
            icon = Icons.Filled.Folder,
            label = stringResource(R.string.groups_tab_groups),
            active = selected == GroupifyTab.Groups,
            onClick = { onSelect(GroupifyTab.Groups) },
        )
    }
}

@Composable
private fun TabItem(
    icon: ImageVector,
    label: String,
    active: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = if (active) GroupifyTokens.TextPrimary else GroupifyTokens.TextSecondary
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (active) GroupifyTokens.AccentSoft else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = contentColor, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
