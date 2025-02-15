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

package androidx.xr.compose.subspace.node

import androidx.compose.runtime.AbstractApplier
import androidx.xr.compose.platform.Logger
import androidx.xr.compose.unit.VolumeConstraints
import androidx.xr.runtime.math.Pose
import androidx.xr.runtime.math.Quaternion
import androidx.xr.runtime.math.Vector3

/**
 * Node applier for subspace compositions.
 *
 * See [androidx.compose.ui.node.UiApplier]
 */
internal class SubspaceNodeApplier(root: SubspaceLayoutNode) :
    AbstractApplier<SubspaceLayoutNode>(root) {

    init {
        root.measurePolicy = SubspaceLayoutNode.RootMeasurePolicy
    }

    override fun insertTopDown(index: Int, instance: SubspaceLayoutNode) {
        // Ignored. Insert is performed in [insertBottomUp] to build the tree bottom-up to avoid
        // duplicate notification when the child nodes enter the tree.
    }

    override fun insertBottomUp(index: Int, instance: SubspaceLayoutNode) {
        current.insertAt(index, instance)
    }

    override fun remove(index: Int, count: Int) {
        current.removeAt(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.move(from, to, count)
    }

    override fun onClear() {
        root.removeAll()
    }

    override fun onEndChanges() {
        val measureResults =
            root.measurableLayout.measure(
                VolumeConstraints(0, VolumeConstraints.INFINITY, 0, VolumeConstraints.INFINITY)
            )

        (measureResults as SubspaceLayoutNode.MeasurableLayout).placeAt(
            Pose(Vector3.Zero, Quaternion.Identity)
        )
        Logger.log("SubspaceNodeApplier") { root.debugTreeToString() }
        Logger.log("SubspaceNodeApplier") { root.debugEntityTreeToString() }
    }
}
