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

package androidx.pdf.content

import android.graphics.Point
import androidx.annotation.RestrictTo

/**
 * Represents one edge of the selected content.
 *
 * @param index: Index of the selection boundary
 * @param point: The point of selection boundary.
 * @param isRtl: Determines whether the direction of selection is right-to-left (rtl) or reverse
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class SelectionBoundary(
    public val index: Int = 0,
    public val point: Point? = null,
    public val isRtl: Boolean = false
)
