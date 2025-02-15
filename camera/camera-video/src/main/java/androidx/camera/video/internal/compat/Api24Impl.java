/*
 * Copyright 2021 The Android Open Source Project
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

package androidx.camera.video.internal.compat;

import android.media.AudioRecord;
import android.media.AudioRecordingConfiguration;
import android.media.AudioTimestamp;

import androidx.annotation.RequiresApi;

import org.jspecify.annotations.NonNull;

/**
 * Helper class to avoid verification errors for methods introduced in Android 7.0 (API 24).
 */
@RequiresApi(24)
public final class Api24Impl {

    private Api24Impl() {
    }

    /**
     * Gets the audio timestamp from a {@link AudioRecord}.
     */
    public static int getTimestamp(@NonNull AudioRecord audioRecord,
            @NonNull AudioTimestamp audioTimestamp, int timeBase) {
        return audioRecord.getTimestamp(audioTimestamp, timeBase);
    }

    /**
     * Gets the audio session ID from a {@link AudioRecordingConfiguration}.
     */
    public static int getClientAudioSessionId(
            @NonNull AudioRecordingConfiguration audioRecordingConfiguration) {
        return audioRecordingConfiguration.getClientAudioSessionId();
    }
}
