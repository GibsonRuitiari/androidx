/*
 * Copyright 2020 The Android Open Source Project
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

package android.support.wearable.watchface.accessibility;

import android.content.Context;
import android.support.wearable.complications.ComplicationText;
import android.text.format.DateFormat;

import androidx.annotation.RestrictTo;

import org.jspecify.annotations.NonNull;

/** Utilities for making watch faces and complications accessible. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class AccessibilityUtils {

    private AccessibilityUtils() {}

    /**
     * Returns a new {@link ComplicationText} that displays the current time in the default
     * timezone.
     */
    public static @NonNull ComplicationText makeTimeAsComplicationText(@NonNull Context context) {
        final String format;
        if (DateFormat.is24HourFormat(context)) {
            format = "HH:mm";
        } else {
            format = "h:mm a";
        }
        return new ComplicationText.TimeFormatBuilder().setFormat(format).build();
    }
}
