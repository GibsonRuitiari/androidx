/*
 * Copyright 2018 The Android Open Source Project
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

package androidx.webkit.internal;

import androidx.webkit.WebViewCompat;

import org.chromium.support_lib_boundary.VisualStateCallbackBoundaryInterface;
import org.jspecify.annotations.NonNull;

/**
 * Adapter between WebViewCompat.VisualStateCallback and VisualStateCallbackBoundaryInterface (the
 * corresponding interface shared with the support library glue in the WebView APK).
 */
public class VisualStateCallbackAdapter implements VisualStateCallbackBoundaryInterface {
    private final WebViewCompat.VisualStateCallback mVisualStateCallback;

    public VisualStateCallbackAdapter(
            WebViewCompat.@NonNull VisualStateCallback visualStateCallback) {
        mVisualStateCallback = visualStateCallback;
    }

    @Override
    public void onComplete(long requestId) {
        mVisualStateCallback.onComplete(requestId);
    }
}
