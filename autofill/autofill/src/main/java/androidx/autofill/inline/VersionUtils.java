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

package androidx.autofill.inline;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.autofill.inline.common.BundledStyle;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods responsible for producing/consuming bundles representing supported versions,
 * or the mapping from supported versions to corresponding UI styles.
 *
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
@RequiresApi(api = Build.VERSION_CODES.R)
public final class VersionUtils {
    /**
     * Bundle key pointing to a list representing UI versions.
     */
    private static final String KEY_INLINE_UI_VERSIONS = "androidx.autofill.inline.ui.version:key";

    /**
     * Returns true if the {@code version} is supported in the current library.
     */
    public static boolean isVersionSupported(@Nullable String version) {
        return UiVersions.getUiVersions().contains(version);
    }

    /**
     * {@code bundleWithSupportedVersions} is expected to be generated by either
     * {@link #writeSupportedVersions(Bundle)} or {@link #writeStylesToBundle(List, Bundle)}.
     *
     * @param bundleWithSupportedVersions bundle containing info about supported versions
     * @return the list of versions that are both specified in the {@code versionedBundle} and
     * supported by the current library.
     */
    public static @NonNull List<String> getSupportedVersions(
            @NonNull Bundle bundleWithSupportedVersions) {
        List<String> ret = new ArrayList<>();
        ArrayList<String> versions = bundleWithSupportedVersions.getStringArrayList(
                KEY_INLINE_UI_VERSIONS);
        if (versions != null) {
            for (String version : versions) {
                if (isVersionSupported(version)) {
                    ret.add(version);
                }
            }
        }
        return ret;
    }

    /**
     * Writes the supported version to the {@code bundle}, in the following format:
     *
     * - {@link #KEY_INLINE_UI_VERSIONS} -> {@link List<String>} versions
     *
     * <p>See also {@code #getSupportedVersions(Bundle)} for how it can be used.
     */
    public static void writeSupportedVersions(@NonNull Bundle bundle) {
        bundle.putStringArrayList(KEY_INLINE_UI_VERSIONS,
                new ArrayList<>(UiVersions.getUiVersions()));
    }

    /**
     * Writes the mapping from the UI version to corresponding style specification to the {@code
     * bundle} in the following format:
     *
     * - {@link #KEY_INLINE_UI_VERSIONS} -> {@link List<String>} versions
     * - {@link UiVersions#INLINE_UI_VERSION_1} -> {@link Bundle} built from {@link BundledStyle}
     * - ... (to add if there is more version in the future)
     */
    public static void writeStylesToBundle(@NonNull List<UiVersions.Style> styles,
            @NonNull Bundle bundle) {
        ArrayList<String> versions = new ArrayList<>();
        for (UiVersions.Style style : styles) {
            String version = style.getVersion();
            versions.add(style.getVersion());
            bundle.putBundle(version, style.getBundle());
        }
        bundle.putStringArrayList(KEY_INLINE_UI_VERSIONS, versions);
    }

    /**
     * Reads the style of {@code version} from the {@code versionedStylesBundle}.
     */
    public static @Nullable Bundle readStyleByVersion(@NonNull Bundle versionedStylesBundle,
            @UiVersions.InlineUiVersion @NonNull String version) {
        return versionedStylesBundle.getBundle(version);
    }

    private VersionUtils() {

    }
}
