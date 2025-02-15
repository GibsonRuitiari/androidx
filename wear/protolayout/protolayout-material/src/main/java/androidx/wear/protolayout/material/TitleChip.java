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

package androidx.wear.protolayout.material;

import static androidx.annotation.Dimension.DP;
import static androidx.wear.protolayout.DimensionBuilders.dp;
import static androidx.wear.protolayout.LayoutElementBuilders.HORIZONTAL_ALIGN_UNDEFINED;
import static androidx.wear.protolayout.material.ChipDefaults.ICON_SIZE;
import static androidx.wear.protolayout.material.ChipDefaults.TITLE_HEIGHT;
import static androidx.wear.protolayout.material.ChipDefaults.TITLE_HORIZONTAL_PADDING;
import static androidx.wear.protolayout.material.ChipDefaults.TITLE_PRIMARY_COLORS;
import static androidx.wear.protolayout.materialcore.Helper.checkNotNull;
import static androidx.wear.protolayout.materialcore.Helper.staticString;

import android.content.Context;

import androidx.annotation.Dimension;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters;
import androidx.wear.protolayout.DimensionBuilders.ContainerDimension;
import androidx.wear.protolayout.LayoutElementBuilders.HorizontalAlignment;
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement;
import androidx.wear.protolayout.ModifiersBuilders.Clickable;
import androidx.wear.protolayout.TypeBuilders.StringProp;
import androidx.wear.protolayout.expression.Fingerprint;
import androidx.wear.protolayout.proto.LayoutElementProto;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * ProtoLayout component {@link TitleChip} that represents clickable object with the text.
 *
 * <p>The Title Chip is Stadium shaped object with a larger height then standard Chip and it will
 * take one line of text of {@link Typography#TYPOGRAPHY_TITLE2} style.
 *
 * <p>The recommended set of {@link ChipColors} styles can be obtained from {@link ChipDefaults},
 * e.g. {@link ChipDefaults#TITLE_PRIMARY_COLORS} to get a color scheme for a primary {@link
 * TitleChip}.
 *
 * <p>When accessing the contents of a container for testing, note that this element can't be simply
 * casted back to the original type, i.e.:
 *
 * <pre>{@code
 * TitleChip chip = new TitleChip...
 * Box box = new Box.Builder().addContent(chip).build();
 *
 * TitleChip myChip = (TitleChip) box.getContents().get(0);
 * }</pre>
 *
 * will fail.
 *
 * <p>To be able to get {@link TitleChip} object from any layout element, {@link #fromLayoutElement}
 * method should be used, i.e.:
 *
 * <pre>{@code
 * TitleChip myChip = TitleChip.fromLayoutElement(box.getContents().get(0));
 * }</pre>
 *
 * @see androidx.wear.protolayout.material.layouts.PrimaryLayout.Builder#setContent if this
 *     TitleChip is used inside of {@link androidx.wear.protolayout.material.layouts.PrimaryLayout}.
 */
public class TitleChip implements LayoutElement {
    private final @NonNull Chip mElement;

    TitleChip(@NonNull Chip element) {
        this.mElement = element;
    }

    /** Builder class for {@link TitleChip}. */
    public static final class Builder implements LayoutElement.Builder {
        private final @NonNull Context mContext;
        private final @NonNull String mText;
        private final @NonNull Clickable mClickable;
        private final @NonNull DeviceParameters mDeviceParameters;
        private @NonNull ChipColors mChipColors = TITLE_PRIMARY_COLORS;
        @HorizontalAlignment private int mHorizontalAlign = HORIZONTAL_ALIGN_UNDEFINED;
        private @Nullable StringProp mContentDescription = null;

        // Indicates that the width isn't set, so it will be automatically set by Chip.Builder
        // constructor.
        private @Nullable ContainerDimension mWidth = null;
        private @Nullable String mIconResourceId = null;

        /**
         * Creates a builder for the {@link TitleChip} with associated action and the given text
         *
         * @param context The application's context.
         * @param text The text to be displayed in this title chip. Text will be displayed in 1 line
         *     and truncated if it doesn't fit.
         * @param clickable Associated {@link Clickable} for click events. When the TitleChip is
         *     clicked it will fire the associated action.
         * @param deviceParameters The device parameters used for styling text.
         */
        public Builder(
                @NonNull Context context,
                @NonNull String text,
                @NonNull Clickable clickable,
                @NonNull DeviceParameters deviceParameters) {
            this.mContext = context;
            this.mText = text;
            this.mClickable = clickable;
            this.mDeviceParameters = deviceParameters;
        }

        /**
         * Sets the colors for the {@link TitleChip}. If set, {@link
         * ChipColors#getBackgroundColor()} will be used for the background of the button and {@link
         * ChipColors#getContentColor()} for the text. If not set, {@link
         * ChipDefaults#TITLE_PRIMARY_COLORS} will be used.
         */
        public @NonNull Builder setChipColors(@NonNull ChipColors chipColors) {
            mChipColors = chipColors;
            return this;
        }

        /** Sets the horizontal alignment in the chip. If not set, content will be centered. */
        public @NonNull Builder setHorizontalAlignment(
                @HorizontalAlignment int horizontalAlignment) {
            mHorizontalAlign = horizontalAlignment;
            return this;
        }

        /**
         * Sets the width of {@link TitleChip}. If not set, default value will be set to fill the
         * screen.
         */
        public @NonNull Builder setWidth(@NonNull ContainerDimension width) {
            mWidth = width;
            return this;
        }

        /**
         * Sets the width of {@link TitleChip}. If not set, default value will be set to fill the
         * screen.
         */
        public @NonNull Builder setWidth(@Dimension(unit = DP) float width) {
            mWidth = dp(width);
            return this;
        }

        /**
         * Sets the icon for the {@link TitleChip}. Provided icon will be tinted to the given
         * content color from {@link ChipColors}. This icon should be image with chosen alpha
         * channel that can be tinted.
         */
        public @NonNull Builder setIconContent(@NonNull String imageResourceId) {
            this.mIconResourceId = imageResourceId;
            return this;
        }

        /**
         * Sets the static content description for the {@link TitleChip}. It is highly recommended
         * to provide this for chip containing an icon.
         */
        public @NonNull Builder setContentDescription(@NonNull CharSequence contentDescription) {
            return setContentDescription(staticString(contentDescription.toString()));
        }

        /**
         * Sets the content description for the {@link TitleChip}. It is highly recommended to
         * provide this for chip containing an icon.
         *
         * <p>While this field is statically accessible from 1.0, it's only bindable since version
         * 1.2 and renderers supporting version 1.2 will use the dynamic value (if set).
         */
        public @NonNull Builder setContentDescription(@NonNull StringProp contentDescription) {
            this.mContentDescription = contentDescription;
            return this;
        }

        /** Constructs and returns {@link TitleChip} with the provided content and look. */
        @Override
        public @NonNull TitleChip build() {
            Chip.Builder chipBuilder =
                    new Chip.Builder(mContext, mClickable, mDeviceParameters)
                            .setChipColors(mChipColors)
                            .setContentDescription(
                                    mContentDescription == null
                                            ? staticString(mText)
                                            : mContentDescription)
                            .setHeight(TITLE_HEIGHT)
                            .setMaxLines(1)
                            .setHorizontalPadding(TITLE_HORIZONTAL_PADDING)
                            .setPrimaryLabelContent(mText)
                            .setPrimaryLabelTypography(Typography.TYPOGRAPHY_TITLE2)
                            .setIsPrimaryLabelScalable(false);

            if (mWidth != null) {
                chipBuilder.setWidth(mWidth);
            }

            if (mIconResourceId != null) {
                chipBuilder.setIconContent(mIconResourceId).setIconSize(ICON_SIZE);
            }

            if (mHorizontalAlign != HORIZONTAL_ALIGN_UNDEFINED) {
                chipBuilder.setHorizontalAlignment(mHorizontalAlign);
            }

            return new TitleChip(chipBuilder.build());
        }
    }

    /** Returns width of this Chip. */
    public @NonNull ContainerDimension getWidth() {
        return mElement.getWidth();
    }

    /** Returns click event action associated with this Chip. */
    public @NonNull Clickable getClickable() {
        return mElement.getClickable();
    }

    /** Returns chip color of this Chip. */
    public @NonNull ChipColors getChipColors() {
        return mElement.getChipColors();
    }

    /** Returns text content of this Chip. */
    public @NonNull String getText() {
        return checkNotNull(mElement.getPrimaryLabelContent());
    }

    /** Returns the horizontal alignment of the content in this Chip. */
    @HorizontalAlignment
    public int getHorizontalAlignment() {
        return mElement.getHorizontalAlignment();
    }

    /** Returns icon id from this TitleChip if it has been added. Otherwise, it returns null. */
    public @Nullable String getIconContent() {
        return mElement.getIconContent();
    }

    /** Returns metadata tag set to this TitleChip. */
    @NonNull String getMetadataTag() {
        return mElement.getMetadataTag();
    }

    /**
     * Returns TitleChip object from the given LayoutElement (e.g. one retrieved from a container's
     * content with {@code container.getContents().get(index)}) if that element can be converted to
     * TitleChip. Otherwise, it will return null.
     */
    public static @Nullable TitleChip fromLayoutElement(@NonNull LayoutElement element) {
        if (element instanceof TitleChip) {
            return (TitleChip) element;
        }
        androidx.wear.protolayout.materialcore.Chip coreChip =
                androidx.wear.protolayout.materialcore.Chip.fromLayoutElement(element);
        return coreChip == null ? null : new TitleChip(new Chip(coreChip));
    }

    /** Returns content description of this TitleChip. */
    public @Nullable StringProp getContentDescription() {
        return mElement.getContentDescription();
    }

    @RestrictTo(Scope.LIBRARY_GROUP)
    @Override
    public LayoutElementProto.@NonNull LayoutElement toLayoutElementProto() {
        return mElement.toLayoutElementProto();
    }

    @Override
    @RestrictTo(Scope.LIBRARY_GROUP)
    public @Nullable Fingerprint getFingerprint() {
        return mElement.getFingerprint();
    }
}
