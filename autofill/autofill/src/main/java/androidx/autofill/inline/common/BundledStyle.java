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

package androidx.autofill.inline.common;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;

import org.jspecify.annotations.NonNull;

/**
 * Base class representing a type that encodes the style information, and can be exported
 * to a Bundle.
 *
 */
@RestrictTo(LIBRARY)
@RequiresApi(api = Build.VERSION_CODES.R)
public abstract class BundledStyle {

    protected final @NonNull Bundle mBundle;

    protected BundledStyle(@NonNull Bundle bundle) {
        mBundle = bundle;
    }

    /**
     * Returns the wrapped bundle containing the style specifications.
     *
     */
    @RestrictTo(LIBRARY)
    public final @NonNull Bundle getBundle() {
        return mBundle;
    }

    /**
     * Returns true if the wrapped bundle is valid according to the style key.
     *
     */
    @RestrictTo(LIBRARY)
    public boolean isValid() {
        return mBundle != null && mBundle.getBoolean(getStyleKey(), false);
    }

    /**
     * @throws IllegalStateException if the wrapped bundle is determined invalid by
     *                               {@link #isValid()}.
     */
    @RestrictTo(LIBRARY)
    public void assertIsValid() {
        if (!isValid()) {
            throw new IllegalStateException("Invalid style, missing bundle key " + getStyleKey());
        }
    }

    /**
     * Allows the subclass to define their own style key by implementing this method.
     */
    protected abstract @NonNull String getStyleKey();

    /**
     * Base builder class for the {@link BundledStyle}.
     *
     * @param <T> represents the type that this builder can build.
     */
    public abstract static class Builder<T extends BundledStyle> {

        protected final @NonNull Bundle mBundle = new Bundle();

        protected Builder(@NonNull String style) {
            mBundle.putBoolean(style, true);
        }

        /**
         * Returns a subclass of {@link BundledStyle} built by this builder.
         */
        public abstract @NonNull T build();
    }
}
