/*
 * Copyright 2019 The Android Open Source Project
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

package androidx.camera.core.impl.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import org.jspecify.annotations.NonNull;

/**
 * Singleton instance of an async main thread {@link Handler}.
 * @see HandlerCompat#createAsync(Looper)
 */
public final class MainThreadAsyncHandler {
    private static volatile Handler sHandler;

    /**
     * Returns a main thread handler which marks all messages/runnables posted as async.
     * @see HandlerCompat#createAsync(Looper)
     */
    public static @NonNull Handler getInstance() {
        if (sHandler != null) {
            return sHandler;
        }
        synchronized (MainThreadAsyncHandler.class) {
            if (sHandler == null) {
                sHandler = HandlerCompat.createAsync(Looper.getMainLooper());
            }
        }

        return sHandler;
    }

    // Should not be instantiated
    private MainThreadAsyncHandler() {}
}
