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

package androidx.xr.runtime.internal

import androidx.annotation.RestrictTo

/** Describes the state of the tracking performed. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
public class TrackingState private constructor(private val value: Int) {
    public companion object {
        /** The trackable is currently tracked and its pose is current. */
        @JvmField public val Tracking: TrackingState = TrackingState(0)

        /** Tracking has been paused for this instance but may be resumed in the future. */
        @JvmField public val Paused: TrackingState = TrackingState(1)

        /** Tracking has stopped for this instance and will never be resumed in the future. */
        @JvmField public val Stopped: TrackingState = TrackingState(2)
    }

    public override fun toString(): String =
        when (this) {
            Tracking -> "Tracking"
            Paused -> "Paused"
            Stopped -> "Stopped"
            else -> "Unknown"
        }
}
