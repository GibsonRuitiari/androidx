/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.wear.compose.foundation.lazy.layout

import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset

internal interface LazyLayoutMeasuredItem {
    val index: Int
    val key: Any
    val isVertical: Boolean
    val mainAxisSizeWithSpacings: Int
    val placeablesCount: Int
    var nonScrollableItem: Boolean
    val constraints: Constraints

    fun getOffset(index: Int): IntOffset

    fun getParentData(index: Int): Any?
}

internal fun LazyLayoutMeasuredItem.hasAnimations(): Boolean = run {
    repeat(placeablesCount) { index ->
        getParentData(index).specs?.let {
            // found at least one
            return true
        }
    }
    return false
}

internal val Any?.specs
    get() = this as? LazyLayoutAnimationSpecsNode
