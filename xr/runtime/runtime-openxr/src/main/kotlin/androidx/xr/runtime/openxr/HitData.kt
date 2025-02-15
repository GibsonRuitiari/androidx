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

package androidx.xr.runtime.openxr

import androidx.annotation.RestrictTo
import androidx.xr.runtime.math.Pose

/**
 * Data associated with a hit result.
 *
 * @property pose The pose of the hit result.
 * @property id The id of the trackable that was hit. It is the address of the native object.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
public data class HitData(val pose: Pose, val id: Long) {}
