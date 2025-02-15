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

package androidx.wear.compose.material3

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProgressIndicatorTest {

    @Test
    fun coerce_progress_fraction_overflow_enabled() {
        assertEquals(0.2f, wrapProgress(0.2f, true))
    }

    @Test
    fun coerce_progress_fraction_greater_than_one_overflow_enabled() {
        assertEquals(0.2f, wrapProgress(1.2f, true))
    }

    @Test
    fun coerce_progress_integer_greater_than_one_overflow_enabled() {
        assertEquals(1.0f, wrapProgress(2.0f, true))
    }

    @Test
    fun coerce_progress_zero_overflow_enabled() {
        assertEquals(0.0f, wrapProgress(0.0f, true))
    }

    @Test
    fun coerce_progress_negative_overflow_enabled() {
        assertEquals(0.0f, wrapProgress(-1.0f, true))
    }

    @Test
    fun coerce_progress_fraction_overflow_disabled() {
        assertEquals(0.2f, wrapProgress(0.2f, false))
    }

    @Test
    fun coerce_progress_fraction_greater_than_one_overflow_disabled() {
        assertEquals(1.0f, wrapProgress(1.2f, false))
    }

    @Test
    fun coerce_progress_integer_greater_than_one_overflow_disabled() {
        assertEquals(1.0f, wrapProgress(2.0f, false))
    }

    @Test
    fun coerce_progress_zero_overflow_disabled() {
        assertEquals(0.0f, wrapProgress(0.0f, false))
    }

    @Test
    fun coerce_progress_negative_overflow_disabled() {
        assertEquals(0.0f, wrapProgress(-1.0f, false))
    }
}
