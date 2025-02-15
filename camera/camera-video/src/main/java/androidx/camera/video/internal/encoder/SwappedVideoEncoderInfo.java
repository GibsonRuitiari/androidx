/*
 * Copyright 2022 The Android Open Source Project
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

package androidx.camera.video.internal.encoder;

import static androidx.core.util.Preconditions.checkArgument;

import android.util.Range;

import org.jspecify.annotations.NonNull;

/**
 * A VideoEncoderInfo wrapper that swaps the width and height constraints internally.
 *
 * @noinspection SuspiciousNameCombination
 */
public class SwappedVideoEncoderInfo implements VideoEncoderInfo {
    private final VideoEncoderInfo mVideoEncoderInfo;

    /**
     * @throws IllegalArgumentException if the {@param videoEncoderInfo} is not allowed swapping
     * width and height.
     */
    public SwappedVideoEncoderInfo(@NonNull VideoEncoderInfo videoEncoderInfo) {
        checkArgument(videoEncoderInfo.canSwapWidthHeight());
        mVideoEncoderInfo = videoEncoderInfo;
    }

    @Override
    public @NonNull String getName() {
        return mVideoEncoderInfo.getName();
    }

    @Override
    public boolean canSwapWidthHeight() {
        return mVideoEncoderInfo.canSwapWidthHeight();
    }

    @Override
    public boolean isSizeSupported(int width, int height) {
        return mVideoEncoderInfo.isSizeSupported(height, width);
    }

    @Override
    public @NonNull Range<Integer> getSupportedWidths() {
        return mVideoEncoderInfo.getSupportedHeights();
    }

    @Override
    public @NonNull Range<Integer> getSupportedHeights() {
        return mVideoEncoderInfo.getSupportedWidths();
    }

    @Override
    public @NonNull Range<Integer> getSupportedWidthsFor(int height) {
        return mVideoEncoderInfo.getSupportedHeightsFor(height);
    }

    @Override
    public @NonNull Range<Integer> getSupportedHeightsFor(int width) {
        return mVideoEncoderInfo.getSupportedWidthsFor(width);
    }

    @Override
    public int getWidthAlignment() {
        return mVideoEncoderInfo.getHeightAlignment();
    }

    @Override
    public int getHeightAlignment() {
        return mVideoEncoderInfo.getWidthAlignment();
    }

    @Override
    public @NonNull Range<Integer> getSupportedBitrateRange() {
        return mVideoEncoderInfo.getSupportedBitrateRange();
    }
}
