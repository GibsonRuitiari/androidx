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

package androidx.wear.tiles.material.layouts;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.Dimension;

import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TestCasesGenerator {
    private TestCasesGenerator() {}

    public static final String NORMAL_SCALE_SUFFIX = "";
    public static final String XXXL_SCALE_SUFFIX = "_xxxl";

    /**
     * This function will append goldenSuffix on the name of the golden images that should be
     * different for different user font sizes. Note that some of the golden will have the same name
     * as it should point on the same size independent image.
     */
    static @NonNull Map<String, androidx.wear.tiles.LayoutElementBuilders.LayoutElement>
            generateTestCases(
                    @NonNull Context context,
                    androidx.wear.tiles.DeviceParametersBuilders.@NonNull DeviceParameters
                            deviceParameters,
                    @NonNull String goldenSuffix) {
        androidx.wear.tiles.ModifiersBuilders.Clickable clickable =
                new androidx.wear.tiles.ModifiersBuilders.Clickable.Builder()
                        .setOnClick(
                                new androidx.wear.tiles.ActionBuilders.LaunchAction.Builder()
                                        .build())
                        .setId("action_id")
                        .build();
        HashMap<String, androidx.wear.tiles.LayoutElementBuilders.LayoutElement> testCases =
                new HashMap<>();

        androidx.wear.tiles.material.TitleChip content =
                new androidx.wear.tiles.material.TitleChip.Builder(
                                context, "Action", clickable, deviceParameters)
                        .build();
        androidx.wear.tiles.material.CompactChip.Builder primaryChipBuilder =
                new androidx.wear.tiles.material.CompactChip.Builder(
                        context, "Action", clickable, deviceParameters);

        testCases.put(
                "default_empty_primarychiplayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .build());
        testCases.put(
                "default_longtext_primarychiplayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(
                                new androidx.wear.tiles.material.CompactChip.Builder(
                                                context,
                                                "Too_long_textToo_long_textToo_long_text",
                                                clickable,
                                                deviceParameters)
                                        .build())
                        .build());
        testCases.put(
                "coloredbox_primarylabel_primarychiplayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(buildColoredBoxPLL(Color.YELLOW))
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .build());
        testCases.put(
                "coloredbox_primarylabel_secondarylabel_primarychiplayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(buildColoredBoxPLL(Color.YELLOW))
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .setSecondaryLabelTextContent(buildTextLabel(context, "Secondary label"))
                        .build());
        testCases.put(
                "coloredbox_secondarylabel_primarychiplayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(buildColoredBoxPLL(Color.YELLOW))
                        .setSecondaryLabelTextContent(buildTextLabel(context, "Secondary label"))
                        .build());

        testCases.put(
                "custom_primarychiplayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(content)
                        .setPrimaryChipContent(
                                primaryChipBuilder
                                        .setChipColors(
                                                new androidx.wear.tiles.material.ChipColors(
                                                        Color.YELLOW, Color.GREEN))
                                        .build())
                        .build());
        testCases.put(
                "coloredbox_primarychiplayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(buildColoredBoxPLL(Color.YELLOW))
                        .build());
        testCases.put(
                "two_chips_content_primarychiplayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new androidx.wear.tiles.LayoutElementBuilders.Column.Builder()
                                        .setWidth(androidx.wear.tiles.DimensionBuilders.expand())
                                        .setHeight(androidx.wear.tiles.DimensionBuilders.wrap())
                                        .addContent(
                                                new androidx.wear.tiles.material.Chip.Builder(
                                                                context,
                                                                clickable,
                                                                deviceParameters)
                                                        .setPrimaryLabelContent("First chip")
                                                        .setWidth(
                                                                androidx.wear.tiles
                                                                        .DimensionBuilders.expand())
                                                        .build())
                                        .addContent(
                                                new androidx.wear.tiles.LayoutElementBuilders.Spacer
                                                                .Builder()
                                                        .setHeight(
                                                                androidx.wear.tiles
                                                                        .DimensionBuilders.dp(4))
                                                        .build())
                                        .addContent(
                                                new androidx.wear.tiles.material.Chip.Builder(
                                                                context,
                                                                clickable,
                                                                deviceParameters)
                                                        .setPrimaryLabelContent("Second chip")
                                                        .setWidth(
                                                                androidx.wear.tiles
                                                                        .DimensionBuilders.expand())
                                                        .build())
                                        .build())
                        .build());

        primaryChipBuilder =
                new androidx.wear.tiles.material.CompactChip.Builder(
                        context, "Action", clickable, deviceParameters);
        testCases.put(
                "coloredbox_1_chip_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .build())
                        .build());
        testCases.put(
                "coloredbox_2_chip_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .build())
                        .build());
        testCases.put(
                "coloredbox_3_chip_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .addSlotContent(buildColoredBoxMSL(Color.MAGENTA))
                                        .build())
                        .build());
        testCases.put(
                "coloredbox_2_chip_primary_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .build());
        testCases.put(
                "coloredbox_2_chip_primary_secondary_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .setSecondaryLabelTextContent(buildTextLabel(context, "Secondary label"))
                        .build());
        testCases.put(
                "coloredbox_2_columnslayout_golden" + NORMAL_SCALE_SUFFIX,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .build())
                        .build());
        testCases.put(
                "coloredbox_2_primary_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .build());
        testCases.put(
                "coloredbox_2_primary_secondary_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .setSecondaryLabelTextContent(buildTextLabel(context, "Secondary label"))
                        .build());
        testCases.put(
                "coloredbox_3_chip_primary_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .addSlotContent(buildColoredBoxMSL(Color.MAGENTA))
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .build());
        testCases.put(
                "coloredbox_3_chip_primary_secondary_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .addSlotContent(buildColoredBoxMSL(Color.MAGENTA))
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .setSecondaryLabelTextContent(buildTextLabel(context, "Secondary label"))
                        .build());
        testCases.put(
                "coloredbox_3_columnslayout_golden" + NORMAL_SCALE_SUFFIX,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .addSlotContent(buildColoredBoxMSL(Color.MAGENTA))
                                        .build())
                        .build());
        testCases.put(
                "coloredbox_3_primary_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .addSlotContent(buildColoredBoxMSL(Color.MAGENTA))
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .build());
        testCases.put(
                "coloredbox_3_primary_secondary_columnslayout_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .addSlotContent(buildColoredBoxMSL(Color.MAGENTA))
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .setSecondaryLabelTextContent(buildTextLabel(context, "Secondary label"))
                        .build());
        testCases.put(
                "custom_spacer_coloredbox_3_chip_primary_secondary_columnslayout_golden"
                        + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setContent(
                                new MultiSlotLayout.Builder()
                                        .addSlotContent(buildColoredBoxMSL(Color.YELLOW))
                                        .addSlotContent(buildColoredBoxMSL(Color.BLUE))
                                        .addSlotContent(buildColoredBoxMSL(Color.MAGENTA))
                                        .setHorizontalSpacerWidth(2)
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .setSecondaryLabelTextContent(buildTextLabel(context, "Secondary label"))
                        .setVerticalSpacerHeight(1)
                        .build());

        androidx.wear.tiles.material.CircularProgressIndicator.Builder progressIndicatorBuilder =
                new androidx.wear.tiles.material.CircularProgressIndicator.Builder()
                        .setProgress(0.3f);
        androidx.wear.tiles.material.Text textContent =
                new androidx.wear.tiles.material.Text.Builder(context, "Text")
                        .setColor(color(Color.WHITE))
                        .setTypography(androidx.wear.tiles.material.Typography.TYPOGRAPHY_DISPLAY1)
                        .build();
        testCases.put(
                "default_text_progressindicatorlayout_golden" + goldenSuffix,
                new EdgeContentLayout.Builder(deviceParameters)
                        .setEdgeContent(progressIndicatorBuilder.build())
                        .setPrimaryLabelTextContent(
                                new androidx.wear.tiles.material.Text.Builder(
                                                context, "Primary label")
                                        .setTypography(
                                                androidx.wear.tiles.material.Typography
                                                        .TYPOGRAPHY_CAPTION1)
                                        .setColor(
                                                color(androidx.wear.tiles.material.Colors.PRIMARY))
                                        .build())
                        .setContent(textContent)
                        .setSecondaryLabelTextContent(
                                new androidx.wear.tiles.material.Text.Builder(
                                                context, "Secondary label")
                                        .setTypography(
                                                androidx.wear.tiles.material.Typography
                                                        .TYPOGRAPHY_CAPTION1)
                                        .setColor(
                                                color(
                                                        androidx.wear.tiles.material.Colors
                                                                .ON_SURFACE))
                                        .build())
                        .build());
        testCases.put(
                "default_empty_progressindicatorlayout_golden" + NORMAL_SCALE_SUFFIX,
                new EdgeContentLayout.Builder(deviceParameters)
                        .setEdgeContent(progressIndicatorBuilder.build())
                        .build());
        testCases.put(
                "custom_progressindicatorlayout_golden" + goldenSuffix,
                new EdgeContentLayout.Builder(deviceParameters)
                        .setContent(textContent)
                        .setEdgeContent(
                                progressIndicatorBuilder
                                        .setCircularProgressIndicatorColors(
                                                new androidx.wear.tiles.material
                                                        .ProgressIndicatorColors(
                                                        Color.YELLOW, Color.GREEN))
                                        .build())
                        .build());
        testCases.put(
                "coloredbox_progressindicatorlayout_golden" + NORMAL_SCALE_SUFFIX,
                new EdgeContentLayout.Builder(deviceParameters)
                        .setEdgeContent(
                                progressIndicatorBuilder
                                        .setCircularProgressIndicatorColors(
                                                androidx.wear.tiles.material
                                                        .ProgressIndicatorDefaults.DEFAULT_COLORS)
                                        .build())
                        .setContent(
                                new androidx.wear.tiles.LayoutElementBuilders.Box.Builder()
                                        .setWidth(androidx.wear.tiles.DimensionBuilders.dp(500))
                                        .setHeight(androidx.wear.tiles.DimensionBuilders.dp(500))
                                        .setModifiers(
                                                new androidx.wear.tiles.ModifiersBuilders.Modifiers
                                                                .Builder()
                                                        .setBackground(
                                                                new androidx.wear.tiles
                                                                                .ModifiersBuilders
                                                                                .Background
                                                                                .Builder()
                                                                        .setColor(
                                                                                color(Color.YELLOW))
                                                                        .build())
                                                        .build())
                                        .build())
                        .build());

        androidx.wear.tiles.material.Button button1 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("1")
                        .build();
        androidx.wear.tiles.material.Button button2 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("2")
                        .build();
        androidx.wear.tiles.material.Button button3 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("3")
                        .build();
        androidx.wear.tiles.material.Button button4 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("4")
                        .build();
        androidx.wear.tiles.material.Button button5 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("5")
                        .build();
        androidx.wear.tiles.material.Button button6 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("6")
                        .build();
        androidx.wear.tiles.material.Button button7 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("7")
                        .build();
        androidx.wear.tiles.material.Button largeButton1 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("1")
                        .setSize(androidx.wear.tiles.material.ButtonDefaults.LARGE_SIZE)
                        .build();
        androidx.wear.tiles.material.Button largeButton2 =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("2")
                        .setSize(androidx.wear.tiles.material.ButtonDefaults.LARGE_SIZE)
                        .build();
        androidx.wear.tiles.material.Button extraLargeButton =
                new androidx.wear.tiles.material.Button.Builder(context, clickable)
                        .setTextContent("1")
                        .setSize(androidx.wear.tiles.material.ButtonDefaults.EXTRA_LARGE_SIZE)
                        .build();
        testCases.put(
                "multibutton_layout_1button_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(extraLargeButton)
                                        .build())
                        .build());
        testCases.put(
                "multibutton_layout_1button_chip_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(extraLargeButton)
                                        .build())
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .build());
        testCases.put(
                "multibutton_layout_2button_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(largeButton1)
                                        .addButtonContent(largeButton2)
                                        .build())
                        .build());
        testCases.put(
                "multibutton_layout_2button_chip_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(largeButton1)
                                        .addButtonContent(largeButton2)
                                        .build())
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .build());
        testCases.put(
                "multibutton_layout_2button_primarylabel_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(largeButton1)
                                        .addButtonContent(largeButton2)
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .build());
        testCases.put(
                "multibutton_layout_2button_chip_primarylabel_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(largeButton1)
                                        .addButtonContent(largeButton2)
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .build());
        testCases.put(
                "multibutton_layout_3button_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .build())
                        .build());
        testCases.put(
                "multibutton_layout_3button_chip_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .build())
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .build());
        testCases.put(
                "multibutton_layout_3button_primarylabel_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .build());
        testCases.put(
                "multibutton_layout_3button_chip_primarylabel_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .build())
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .setPrimaryLabelTextContent(buildTextLabel(context, "Primary label"))
                        .build());
        testCases.put(
                "multibutton_layout_4button_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .addButtonContent(button4)
                                        .build())
                        .build());
        testCases.put(
                "multibutton_layout_4button_chip_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .addButtonContent(button4)
                                        .build())
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .build());
        testCases.put(
                "multibutton_layout_5button_top_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .addButtonContent(button4)
                                        .addButtonContent(button5)
                                        .setFiveButtonDistribution(
                                                MultiButtonLayout
                                                        .FIVE_BUTTON_DISTRIBUTION_TOP_HEAVY)
                                        .build())
                        .build());
        testCases.put(
                "multibutton_layout_5button_bottom_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .addButtonContent(button4)
                                        .addButtonContent(button5)
                                        .setFiveButtonDistribution(
                                                MultiButtonLayout
                                                        .FIVE_BUTTON_DISTRIBUTION_BOTTOM_HEAVY)
                                        .build())
                        .build());
        testCases.put(
                "multibutton_layout_5button_bottom_chip_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .addButtonContent(button4)
                                        .addButtonContent(button5)
                                        .setFiveButtonDistribution(
                                                MultiButtonLayout
                                                        .FIVE_BUTTON_DISTRIBUTION_BOTTOM_HEAVY)
                                        .build())
                        .setPrimaryChipContent(primaryChipBuilder.build())
                        .build());
        testCases.put(
                "multibutton_layout_6button_golden" + goldenSuffix,
                new PrimaryLayout.Builder(deviceParameters)
                        .setContent(
                                new MultiButtonLayout.Builder()
                                        .addButtonContent(button1)
                                        .addButtonContent(button2)
                                        .addButtonContent(button3)
                                        .addButtonContent(button4)
                                        .addButtonContent(button5)
                                        .addButtonContent(button6)
                                        .build())
                        .build());
        testCases.put(
                "multibutton_layout_7button_golden" + goldenSuffix,
                new MultiButtonLayout.Builder()
                        .addButtonContent(button1)
                        .addButtonContent(button2)
                        .addButtonContent(button3)
                        .addButtonContent(button4)
                        .addButtonContent(button5)
                        .addButtonContent(button6)
                        .addButtonContent(button7)
                        .build());

        return testCases;
    }

    private static androidx.wear.tiles.ColorBuilders.@NonNull ColorProp color(int yellow) {
        return androidx.wear.tiles.ColorBuilders.argb(yellow);
    }

    private static androidx.wear.tiles.material.@NonNull Text buildTextLabel(
            @NonNull Context context, @NonNull String text) {
        return new androidx.wear.tiles.material.Text.Builder(context, text)
                .setTypography(androidx.wear.tiles.material.Typography.TYPOGRAPHY_CAPTION1)
                .setColor(color(Color.WHITE))
                .build();
    }

    private static androidx.wear.tiles.LayoutElementBuilders.@NonNull Box buildColoredBoxMSL(
            int color) {
        return new androidx.wear.tiles.LayoutElementBuilders.Box.Builder()
                .setWidth(androidx.wear.tiles.DimensionBuilders.dp(60))
                .setHeight(androidx.wear.tiles.DimensionBuilders.dp(60))
                .setModifiers(
                        new androidx.wear.tiles.ModifiersBuilders.Modifiers.Builder()
                                .setBackground(
                                        new androidx.wear.tiles.ModifiersBuilders.Background
                                                        .Builder()
                                                .setColor(color(color))
                                                .build())
                                .build())
                .build();
    }

    private static androidx.wear.tiles.LayoutElementBuilders.@NonNull Box buildColoredBoxPLL(
            int color) {
        return new androidx.wear.tiles.LayoutElementBuilders.Box.Builder()
                .setWidth(androidx.wear.tiles.DimensionBuilders.expand())
                .setHeight(androidx.wear.tiles.DimensionBuilders.dp(60))
                .setModifiers(
                        new androidx.wear.tiles.ModifiersBuilders.Modifiers.Builder()
                                .setBackground(
                                        new androidx.wear.tiles.ModifiersBuilders.Background
                                                        .Builder()
                                                .setColor(color(color))
                                                .build())
                                .build())
                .build();
    }

    @Dimension(unit = Dimension.DP)
    static int pxToDp(int px, float scale) {
        return (int) ((px - 0.5f) / scale);
    }
}
