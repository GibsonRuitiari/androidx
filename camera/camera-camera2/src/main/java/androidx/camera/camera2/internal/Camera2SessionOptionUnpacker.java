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

package androidx.camera.camera2.internal;

import android.util.Size;

import androidx.annotation.OptIn;
import androidx.camera.camera2.impl.Camera2ImplConfig;
import androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat;
import androidx.camera.camera2.internal.compat.workaround.PreviewPixelHDRnet;
import androidx.camera.camera2.interop.ExperimentalCamera2Interop;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.MutableOptionsBundle;
import androidx.camera.core.impl.OptionsBundle;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfig;

import org.jspecify.annotations.NonNull;

/**
 * A {@link SessionConfig.OptionUnpacker} implementation for unpacking Camera2 options into a
 * {@link SessionConfig.Builder}.
 */
final class Camera2SessionOptionUnpacker implements SessionConfig.OptionUnpacker {

    static final Camera2SessionOptionUnpacker INSTANCE = new Camera2SessionOptionUnpacker();

    @OptIn(markerClass = ExperimentalCamera2Interop.class)
    @Override
    public void unpack(
            @NonNull Size resolution,
            @NonNull UseCaseConfig<?> config,
            final SessionConfig.@NonNull Builder builder) {
        SessionConfig defaultSessionConfig =
                config.getDefaultSessionConfig(/*valueIfMissing=*/ null);

        Config implOptions = OptionsBundle.emptyBundle();
        int templateType = SessionConfig.defaultEmptySessionConfig().getTemplateType();

        // Apply/extract defaults from session config
        if (defaultSessionConfig != null) {
            templateType = defaultSessionConfig.getTemplateType();
            builder.addAllDeviceStateCallbacks(defaultSessionConfig.getDeviceStateCallbacks());
            builder.addAllSessionStateCallbacks(defaultSessionConfig.getSessionStateCallbacks());
            builder.addAllRepeatingCameraCaptureCallbacks(
                    defaultSessionConfig.getRepeatingCameraCaptureCallbacks());
            implOptions = defaultSessionConfig.getImplementationOptions();
        }

        // Set the any additional implementation options
        builder.setImplementationOptions(implOptions);

        // Apply quirks
        if (config instanceof PreviewConfig) {
            PreviewPixelHDRnet.setHDRnet(resolution, builder);
        }

        // Get Camera2 extended options
        final Camera2ImplConfig camera2Config = new Camera2ImplConfig(config);

        // Apply template type
        builder.setTemplateType(camera2Config.getCaptureRequestTemplate(templateType));

        // Add extension callbacks
        builder.addDeviceStateCallback(
                camera2Config.getDeviceStateCallback(
                        CameraDeviceStateCallbacks.createNoOpCallback()));
        builder.addSessionStateCallback(
                camera2Config.getSessionStateCallback(
                        CameraCaptureSessionStateCallbacks.createNoOpCallback()));
        builder.addCameraCaptureCallback(
                CaptureCallbackContainer.create(
                        camera2Config.getSessionCaptureCallback(
                                Camera2CaptureCallbacks.createNoOpCallback())));

        // Set video stabilization mode
        builder.setVideoStabilization(config.getVideoStabilizationMode());
        builder.setPreviewStabilization(config.getPreviewStabilizationMode());

        // Copy extended Camera2 configurations
        MutableOptionsBundle extendedConfig = MutableOptionsBundle.create();
        extendedConfig.insertOption(Camera2ImplConfig.SESSION_PHYSICAL_CAMERA_ID_OPTION,
                camera2Config.getPhysicalCameraId(null));
        extendedConfig.insertOption(Camera2ImplConfig.STREAM_USE_CASE_OPTION,
                camera2Config.getStreamUseCase(OutputConfigurationCompat.STREAM_USE_CASE_NONE));
        builder.addImplementationOptions(extendedConfig);

        // Copy extension keys
        builder.addImplementationOptions(camera2Config.getCaptureRequestOptions());
    }
}
