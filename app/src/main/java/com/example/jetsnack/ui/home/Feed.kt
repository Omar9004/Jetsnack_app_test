/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.jetsnack.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetsnack.model.Filter
import com.example.jetsnack.model.SnackCollection
import com.example.jetsnack.model.SnackRepo
import com.example.jetsnack.ui.components.FilterBar
import com.example.jetsnack.ui.components.JetsnackDivider
import com.example.jetsnack.ui.components.JetsnackSurface
import com.example.jetsnack.ui.components.SnackCollection
import com.example.jetsnack.ui.theme.JetsnackTheme

@Composable
fun Feed(onSnackClick: (Long, String) -> Unit, modifier: Modifier = Modifier) {
    val snackCollections = remember { SnackRepo.getSnacks() }
    val filters = remember { SnackRepo.getFilters() }
    Feed(
        snackCollections,
        filters,
        onSnackClick,
        modifier,
    )
}

@Composable
private fun Feed(
    snackCollections: List<SnackCollection>,
    filters: List<Filter>,
    onSnackClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    JetsnackSurface(modifier = modifier.fillMaxSize()) {
        var filtersVisible by remember {
            mutableStateOf(false)
        }
        SharedTransitionLayout {
            Box {
                SnackCollectionList(
                    snackCollections,
                    filters,
                    filtersVisible = filtersVisible,
                    onFiltersSelected = {
                        filtersVisible = true
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onSnackClick = onSnackClick,
                )
                DestinationBar()
                AnimatedVisibility(filtersVisible, enter = fadeIn(), exit = fadeOut()) {
                    FilterScreen(
                        animatedVisibilityScope = this@AnimatedVisibility,
                        sharedTransitionScope = this@SharedTransitionLayout,
                    ) { filtersVisible = false }
                }
            }
        }
    }
}

@Composable
private fun SnackCollectionList(
    snackCollections: List<SnackCollection>,
    filters: List<Filter>,
    filtersVisible: Boolean,
    onFiltersSelected: () -> Unit,
    onSnackClick: (Long, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.testTag("SectionColumns")) {
        item {
            Spacer(
                Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 56.dp)),
                ),
            )
            FilterBar(
                filters,
                sharedTransitionScope = sharedTransitionScope,
                filterScreenVisible = filtersVisible,
                onShowFilters = onFiltersSelected,
            )
        }
        itemsIndexed(snackCollections) { index, snackCollection ->
            if (index > 0) {
                JetsnackDivider(thickness = 2.dp)
            }

            SnackCollection(
                snackCollection = snackCollection,
                onSnackClick = onSnackClick,
                index = index,
            )
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun HomePreview() {
    JetsnackTheme {
        Feed(onSnackClick = { _, _ -> })
    }
}
