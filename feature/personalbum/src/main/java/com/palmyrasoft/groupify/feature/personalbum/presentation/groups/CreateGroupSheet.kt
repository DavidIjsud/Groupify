package com.palmyrasoft.groupify.feature.personalbum.presentation.groups

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.palmyrasoft.groupify.feature.personalbum.R
import com.palmyrasoft.groupify.feature.personalbum.domain.model.normalizedGroupName
import com.palmyrasoft.groupify.feature.personalbum.presentation.GroupifyTokens
import com.palmyrasoft.groupify.feature.personalbum.presentation.model.GroupUiModel
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupSheet(
    photoCount: Int,
    faceCount: Int,
    existingGroups: List<GroupUiModel>,
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit,
    onAddToExisting: (groupId: String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by rememberSaveable { mutableStateOf("") }
    var selectedGroupId by rememberSaveable { mutableStateOf<String?>(null) }

    val adding = selectedGroupId != null
    val selectedGroup = remember(selectedGroupId, existingGroups) {
        existingGroups.firstOrNull { it.id == selectedGroupId }
    }
    val trimmed = name.trim()
    val isDuplicate = trimmed.isNotEmpty() &&
        existingGroups.any { it.name.normalizedGroupName() == trimmed.normalizedGroupName() }
    val isValid = trimmed.isNotEmpty() && !isDuplicate

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GroupifyTokens.Card,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, end = 22.dp, bottom = 28.dp),
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 18.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(GroupifyTokens.TextDim),
            )

            // Title row
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.groups_sheet_title),
                        color = GroupifyTokens.TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = pluralStringResource(R.plurals.groups_photos_count, photoCount, photoCount) +
                            " · " +
                            pluralStringResource(R.plurals.groups_faces_count, faceCount, faceCount),
                        color = GroupifyTokens.TextSecondary,
                        fontSize = 14.sp,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(GroupifyTokens.Field)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.groups_cd_close),
                        tint = GroupifyTokens.TextSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // Name field — dimmed/disabled while in "adding to existing" mode
            NameField(
                value = name,
                onValueChange = { name = it },
                enabled = !adding,
                isValid = isValid,
                isDuplicate = isDuplicate,
            )

            Spacer(Modifier.height(8.dp))

            // Helper text
            HelperText(
                adding = adding,
                isValid = isValid,
                isDuplicate = isDuplicate,
                duplicateName = trimmed,
                onCreateNewInstead = { selectedGroupId = null },
            )

            Spacer(Modifier.height(14.dp))

            // Suggestions (only in the empty, create-new state)
            if (!adding && trimmed.isEmpty()) {
                SuggestionChips(onPick = { name = it })
            }

            // Add-to-existing / Adding-to list — hidden in the duplicate (blocked) state
            if (existingGroups.isNotEmpty() && !isDuplicate) {
                val rows = if (adding) listOfNotNull(selectedGroup) else existingGroups.take(6)
                ExistingGroupsList(
                    adding = adding,
                    newPhotoCount = photoCount,
                    groups = rows,
                    onTapGroup = { selectedGroupId = it },
                )
            }

            Spacer(Modifier.height(16.dp))

            // Primary CTA — morphs between create and add
            val ctaEnabled = if (adding) selectedGroup != null else isValid
            val ctaLabel = if (adding && selectedGroup != null) {
                stringResource(R.string.groups_cta_add, selectedGroup.name)
            } else {
                stringResource(R.string.groups_cta_create)
            }
            PrimaryButton(
                label = ctaLabel,
                enabled = ctaEnabled,
                icon = { tint ->
                    Icon(
                        imageVector = if (adding) Icons.Filled.Add else Icons.Filled.CreateNewFolder,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(19.dp),
                    )
                },
                onClick = {
                    val groupId = selectedGroupId
                    if (adding && groupId != null) onAddToExisting(groupId)
                    else if (isValid) onCreate(trimmed)
                },
            )
        }
    }
}

@Composable
private fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    isValid: Boolean,
    isDuplicate: Boolean,
) {
    val borderColor = when {
        isDuplicate -> GroupifyTokens.Danger
        isValid -> GroupifyTokens.Success
        else -> GroupifyTokens.Field
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(GroupifyTokens.Field)
            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .then(if (enabled) Modifier else Modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Folder,
            contentDescription = null,
            tint = GroupifyTokens.TextSecondary,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.groups_field_label).uppercase(),
                color = GroupifyTokens.TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(2.dp))
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.groups_field_placeholder),
                        color = GroupifyTokens.TextDim,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = GroupifyTokens.TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    cursorBrush = SolidColor(GroupifyTokens.Accent),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        when {
            isDuplicate -> StatusBadge(GroupifyTokens.Danger) {
                Icon(Icons.Filled.Close, null, tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(14.dp))
            }
            isValid -> StatusBadge(GroupifyTokens.Success) {
                Icon(Icons.Filled.Check, null, tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun StatusBadge(color: androidx.compose.ui.graphics.Color, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.size(24.dp).clip(CircleShape).background(color),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

@Composable
private fun HelperText(
    adding: Boolean,
    isValid: Boolean,
    isDuplicate: Boolean,
    duplicateName: String,
    onCreateNewInstead: () -> Unit,
) {
    Box(modifier = Modifier.height(22.dp)) {
        when {
            adding -> Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onCreateNewInstead() }) {
                Icon(Icons.Filled.ChevronLeft, null, tint = GroupifyTokens.Accent, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.groups_create_new_instead), color = GroupifyTokens.Accent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            isDuplicate -> Text(
                text = stringResource(R.string.groups_helper_duplicate, duplicateName),
                color = GroupifyTokens.Danger,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
            isValid -> Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Check, null, tint = GroupifyTokens.Success, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text(stringResource(R.string.groups_helper_available), color = GroupifyTokens.Success, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            else -> Text(stringResource(R.string.groups_helper_empty), color = GroupifyTokens.TextSecondary, fontSize = 13.sp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SuggestionChips(onPick: (String) -> Unit) {
    val suggestions = stringArrayResource(R.array.groups_name_suggestions)
    Column {
        Text(
            text = stringResource(R.string.groups_suggestions).uppercase(),
            color = GroupifyTokens.TextDim,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 10.dp),
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            suggestions.forEach { s ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(GroupifyTokens.AccentSoft)
                        .border(1.dp, GroupifyTokens.AccentBorder, RoundedCornerShape(999.dp))
                        .clickable { onPick(s) }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                ) {
                    Text(s, color = GroupifyTokens.Accent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.height(18.dp))
    }
}

@Composable
private fun ExistingGroupsList(
    adding: Boolean,
    newPhotoCount: Int,
    groups: List<GroupUiModel>,
    onTapGroup: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(GroupifyTokens.CardElevated)
            .then(
                if (adding) Modifier.border(1.dp, GroupifyTokens.AccentBorder, RoundedCornerShape(14.dp))
                else Modifier,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = (if (adding) stringResource(R.string.groups_adding_to) else stringResource(R.string.groups_add_to_existing)).uppercase(),
                color = GroupifyTokens.TextDim,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = if (adding) {
                    pluralStringResource(R.plurals.groups_new_photos, newPhotoCount, newPhotoCount)
                } else {
                    pluralStringResource(R.plurals.groups_count, groups.size, groups.size)
                },
                color = GroupifyTokens.TextSecondary,
                fontSize = 12.sp,
            )
        }
        groups.forEachIndexed { index, g ->
            val selected = adding
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (!adding) Modifier.clickable { onTapGroup(g.id) } else Modifier)
                    .background(if (selected) GroupifyTokens.AccentSoft else androidx.compose.ui.graphics.Color.Transparent)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GroupThumb(uri = g.previewUris.firstOrNull(), size = 38.dp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = g.name,
                            color = GroupifyTokens.TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (selected) {
                            Spacer(Modifier.width(8.dp))
                            Text("+$newPhotoCount", color = GroupifyTokens.Accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = if (selected) {
                            stringResource(R.string.groups_after_add, g.photoCount, g.photoCount + newPhotoCount)
                        } else {
                            pluralStringResource(R.plurals.groups_photos_count, g.photoCount, g.photoCount)
                        },
                        color = GroupifyTokens.TextSecondary,
                        fontSize = 12.sp,
                    )
                }
                if (selected) {
                    Box(
                        modifier = Modifier.size(26.dp).clip(CircleShape).background(GroupifyTokens.Accent),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Filled.Check, null, tint = GroupifyTokens.TextPrimary, modifier = Modifier.size(14.dp))
                    }
                } else {
                    Icon(Icons.Filled.Add, null, tint = GroupifyTokens.TextSecondary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun GroupThumb(uri: String?, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier.size(size).clip(RoundedCornerShape(8.dp)).background(GroupifyTokens.Field),
        contentAlignment = Alignment.Center,
    ) {
        if (uri != null) {
            AsyncImage(
                model = Uri.parse(uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(size),
            )
        } else {
            Icon(Icons.Filled.Folder, null, tint = GroupifyTokens.TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun PrimaryButton(
    label: String,
    enabled: Boolean,
    icon: @Composable (tint: androidx.compose.ui.graphics.Color) -> Unit,
    onClick: () -> Unit,
) {
    val bg = if (enabled) GroupifyTokens.Accent else GroupifyTokens.Accent.copy(alpha = 0.35f)
    val contentColor = if (enabled) GroupifyTokens.TextPrimary else GroupifyTokens.TextPrimary.copy(alpha = 0.7f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .clickable(enabled = enabled) { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon(contentColor)
        Spacer(Modifier.width(8.dp))
        Text(label, color = contentColor, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
}
