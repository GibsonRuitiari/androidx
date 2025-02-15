/*
 * Copyright 2024 The Android Open Source Project
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

package androidx.wear.protolayout.material3

import androidx.wear.protolayout.DimensionBuilders.ContainerDimension
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.weight
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Box
import androidx.wear.protolayout.LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER
import androidx.wear.protolayout.LayoutElementBuilders.HORIZONTAL_ALIGN_END
import androidx.wear.protolayout.LayoutElementBuilders.HORIZONTAL_ALIGN_START
import androidx.wear.protolayout.LayoutElementBuilders.HorizontalAlignment
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.LayoutElementBuilders.TEXT_ALIGN_START
import androidx.wear.protolayout.ModifiersBuilders.Background
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ModifiersBuilders.Corner
import androidx.wear.protolayout.ModifiersBuilders.Padding
import androidx.wear.protolayout.ModifiersBuilders.SEMANTICS_ROLE_BUTTON
import androidx.wear.protolayout.material3.AppCardDefaults.buildContentForAppCard
import androidx.wear.protolayout.material3.AppCardStyle.Companion.defaultAppCardStyle
import androidx.wear.protolayout.material3.CardDefaults.DEFAULT_CONTENT_PADDING
import androidx.wear.protolayout.material3.CardDefaults.METADATA_TAG
import androidx.wear.protolayout.material3.CardDefaults.filledCardColors
import androidx.wear.protolayout.material3.DataCardDefaults.buildContentForDataCard
import androidx.wear.protolayout.material3.DataCardStyle.Companion.defaultCompactDataCardStyle
import androidx.wear.protolayout.material3.DataCardStyle.Companion.defaultDataCardStyle
import androidx.wear.protolayout.material3.GraphicDataCardDefaults.buildContentForGraphicDataCard
import androidx.wear.protolayout.material3.GraphicDataCardStyle.Companion.defaultGraphicDataCardStyle
import androidx.wear.protolayout.material3.TitleCardDefaults.buildContentForTitleCard
import androidx.wear.protolayout.material3.TitleCardStyle.Companion.defaultTitleCardStyle
import androidx.wear.protolayout.modifiers.LayoutModifier
import androidx.wear.protolayout.modifiers.contentDescription
import androidx.wear.protolayout.modifiers.semanticsRole
import androidx.wear.protolayout.modifiers.toProtoLayoutModifiersBuilder
import androidx.wear.protolayout.types.LayoutColor

/**
 * Opinionated ProtoLayout Material3 title card that offers 1 to 3 slots, usually text based.
 *
 * Those are vertically stacked title and content, and additional side slot for a time.
 *
 * @param onClick Associated [Clickable] for click events. When the card is clicked it will fire the
 *   associated action.
 * @param title A slot for displaying the title of the card, expected to be one or two lines of
 *   text. Uses [CardColors.title] color by default.
 * @param modifier Modifiers to set to this element. It's highly recommended to set a content
 *   description using [contentDescription].
 * @param content The optional body content of the card. Uses [CardColors.content] color by default.
 * @param time An optional slot for displaying the time relevant to the contents of the card,
 *   expected to be a short piece of text. Uses [CardColors.time] color by default.
 * @param height The height of this card. It's highly recommended to set this to [expand] or
 *   [weight]
 * @param shape Defines the card's shape, in other words the corner radius for this card.
 * @param colors The colors to be used for a background and inner content of this card. If the
 *   background image is also specified, the image will be laid out on top of the background color.
 *   In case of the fully opaque background image, then the background color will not be shown.
 *   Specified colors can be [CardDefaults.filledCardColors] for high emphasis card,
 *   [CardDefaults.filledVariantCardColors] for high/medium emphasis card,
 *   [CardDefaults.filledTonalCardColors] for low/medium emphasis card,
 *   [CardDefaults.imageBackgroundCardColors] for card with image as a background or custom built
 *   [CardColors].
 * @param background The background object to be used behind the content in the card. It is
 *   recommended to use the default styling that is automatically provided by only calling
 *   [backgroundImage] with the content. It can be combined with the specified [colors]'s background
 *   color behind it.
 * @param style The style which provides the attribute values required for constructing this title
 *   card and its inner content. It also provides default style for the inner content, that can be
 *   overridden by each content slot.
 * @param contentPadding The inner padding used to prevent inner content from being too close to the
 *   card's edge. It's highly recommended to keep the default.
 * @param horizontalAlignment The horizontal alignment of [title] and [content]. Default to centered
 *   when [time] is not present. When time is present, defaults to start aligned, which is highly
 *   recommended.
 * @sample androidx.wear.protolayout.material3.samples.titleCardSample
 */
// TODO: b/346958146 - link Card visuals in DAC
// TODO: b/373578620 - Add how corners affects margins in the layout.
public fun MaterialScope.titleCard(
    onClick: Clickable,
    title: (MaterialScope.() -> LayoutElement),
    modifier: LayoutModifier = LayoutModifier,
    content: (MaterialScope.() -> LayoutElement)? = null,
    time: (MaterialScope.() -> LayoutElement)? = null,
    height: ContainerDimension = wrapWithMinTapTargetDimension(),
    shape: Corner =
        if (deviceConfiguration.screenWidthDp.isBreakpoint()) shapes.extraLarge else shapes.large,
    colors: CardColors = filledCardColors(),
    background: (MaterialScope.() -> LayoutElement)? = null,
    style: TitleCardStyle = defaultTitleCardStyle(),
    contentPadding: Padding = style.innerPadding,
    @HorizontalAlignment
    horizontalAlignment: Int = if (time == null) HORIZONTAL_ALIGN_CENTER else HORIZONTAL_ALIGN_START
): LayoutElement =
    card(
        onClick = onClick,
        modifier = modifier,
        width = expand(),
        height = height,
        shape = shape,
        backgroundColor = colors.background,
        background = background,
        contentPadding = contentPadding
    ) {
        buildContentForTitleCard(
            title =
                withStyle(
                        defaultTextElementStyle =
                            TextElementStyle(
                                typography = style.titleTypography,
                                color = colors.title,
                                maxLines = 2,
                                multilineAlignment =
                                    horizontalAlignment.horizontalAlignToTextAlign()
                            )
                    )
                    .title(),
            content =
                content?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.contentTypography,
                                    color = colors.content,
                                    multilineAlignment =
                                        horizontalAlignment.horizontalAlignToTextAlign()
                                )
                        )
                        .it()
                },
            time =
                time?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.timeTypography,
                                    color = colors.time,
                                    multilineAlignment =
                                        horizontalAlignment.horizontalAlignToTextAlign()
                                )
                        )
                        .it()
                },
            horizontalAlignment = horizontalAlignment,
            style = style
        )
    }

/**
 * Opinionated ProtoLayout Material3 app card that offers up to 5 slots, usually text based.
 *
 * Those are vertically stacked title and content, and additional side slot for a time.
 *
 * The first row of the card has three slots:
 * 1) a small optional image, such as [avatarImage]
 * 2) label, which is expected to be a short [text]
 * 3) time, end aligned.
 *
 * The second row shows a title, this is expected to be a single row of start aligned [text].
 *
 * The rest of the [appCard] contains the content which should be [text].
 *
 * @param onClick Associated [Clickable] for click events. When the card is clicked it will fire the
 *   associated action.
 * @param title A slot for displaying the title of the card, expected to be one line of text. Uses
 *   [CardColors.title] color by default.
 * @param modifier Modifiers to set to this element. It's highly recommended to set a content
 *   description using [contentDescription].
 * @param content The optional body content of the card. Uses [CardColors.content] color by default.
 * @param avatar An optional slot in header for displaying small image, such as [avatarImage].
 * @param label An optional slot in header for displaying short, label text. Uses [CardColors.label]
 *   color by default.
 * @param time An optional slot for displaying the time relevant to the contents of the card,
 *   expected to be a short piece of text. Uses [CardColors.time] color by default.
 * @param height The height of this card. It's highly recommended to leave this with default value
 *   as `wrap` if there's only 1 card on the screen. If there are two cards, it is highly
 *   recommended to set this to [expand] and use the smaller styles.
 * @param shape Defines the card's shape, in other words the corner radius for this card.
 * @param colors The colors to be used for a background and inner content of this card. If the
 *   background image is also specified, the image will be laid out on top of the background color.
 *   In case of the fully opaque background image, then the background color will not be shown.
 *   Specified colors can be [CardDefaults.filledCardColors] for high emphasis card,
 *   [CardDefaults.filledVariantCardColors] for high/medium emphasis card,
 *   [CardDefaults.filledTonalCardColors] for low/medium emphasis card,
 *   [CardDefaults.imageBackgroundCardColors] for card with image as a background or custom built
 *   [CardColors].
 * @param background The background object to be used behind the content in the card. It is
 *   recommended to use the default styling that is automatically provided by only calling
 *   [backgroundImage] with the content. It can be combined with the specified [colors]'s background
 *   color behind it.
 * @param style The style which provides the attribute values required for constructing this title
 *   card and its inner content. It also provides default style for the inner content, that can be
 *   overridden by each content slot.
 * @param contentPadding The inner padding used to prevent inner content from being too close to the
 *   card's edge. It's highly recommended to keep the default.
 * @sample androidx.wear.protolayout.material3.samples.appCardSample
 */
// TODO: b/346958146 - link Card visuals in DAC
// TODO: b/373578620 - Add how corners affects margins in the layout.
public fun MaterialScope.appCard(
    onClick: Clickable,
    title: (MaterialScope.() -> LayoutElement),
    modifier: LayoutModifier = LayoutModifier,
    content: (MaterialScope.() -> LayoutElement)? = null,
    avatar: (MaterialScope.() -> LayoutElement)? = null,
    label: (MaterialScope.() -> LayoutElement)? = null,
    time: (MaterialScope.() -> LayoutElement)? = null,
    height: ContainerDimension = wrapWithMinTapTargetDimension(),
    shape: Corner =
        if (deviceConfiguration.screenWidthDp.isBreakpoint()) shapes.extraLarge else shapes.large,
    colors: CardColors = filledCardColors(),
    background: (MaterialScope.() -> LayoutElement)? = null,
    style: AppCardStyle = defaultAppCardStyle(),
    contentPadding: Padding = style.innerPadding,
): LayoutElement =
    card(
        onClick = onClick,
        modifier = modifier,
        width = expand(),
        height = height,
        shape = shape,
        backgroundColor = colors.background,
        background = background,
        contentPadding = contentPadding
    ) {
        buildContentForAppCard(
            title =
                withStyle(
                        defaultTextElementStyle =
                            TextElementStyle(
                                typography = style.titleTypography,
                                color = colors.title,
                                multilineAlignment = TEXT_ALIGN_START
                            )
                    )
                    .title(),
            content =
                content?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.contentTypography,
                                    color = colors.content,
                                    multilineAlignment = TEXT_ALIGN_START
                                )
                        )
                        .it()
                },
            time =
                time?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.timeTypography,
                                    color = colors.time,
                                )
                        )
                        .it()
                },
            label =
                label?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.labelTypography,
                                    color = colors.label,
                                    multilineAlignment = TEXT_ALIGN_START
                                )
                        )
                        .it()
                },
            avatar =
                avatar?.let {
                    withStyle(
                            defaultAvatarImageStyle =
                                AvatarImageStyle(
                                    width = style.avatarSize.toDp(),
                                    height = style.avatarSize.toDp(),
                                )
                        )
                        .it()
                },
            style = style
        )
    }

/**
 * Opinionated ProtoLayout Material3 data card that offers up to 3 vertically stacked slots, usually
 * text or numeral based.
 *
 * This card works well in [buttonGroup] with cards [width] and [height] is set to [expand].
 *
 * @param onClick Associated [Clickable] for click events. When the card is clicked it will fire the
 *   associated action.
 * @param modifier Modifiers to set to this element. It's highly recommended to set a content
 *   description using [contentDescription].
 * @param title A slot for displaying the title of the card, expected to be one line of text. Uses
 *   [CardColors.title] color by default.
 * @param content The optional body content of the card. Uses [CardColors.content] color by default.
 * @param secondaryText An optional slot for displaying short, secondary text. Uses
 *   [CardColors.secondaryText] color by default.
 * @param width The width of this card. It's highly recommended to set this to [expand] or [weight]
 *   for the most optimal experience across different screen sizes.
 * @param height The height of this card. It's highly recommended to set this to [expand] for the
 *   most optimal experience across different screen sizes.
 * @param shape Defines the card's shape, in other words the corner radius for this card.
 * @param colors The colors to be used for a background and inner content of this card. If the
 *   background image is also specified, the image will be laid out on top of the background color.
 *   In case of the fully opaque background image, then the background color will not be shown.
 *   Specified colors can be [CardDefaults.filledCardColors] for high emphasis card,
 *   [CardDefaults.filledVariantCardColors] for high/medium emphasis card,
 *   [CardDefaults.filledTonalCardColors] for low/medium emphasis card,
 *   [CardDefaults.imageBackgroundCardColors] for card with image as a background or custom built
 *   [CardColors].
 * @param background The background object to be used behind the content in the card. It is
 *   recommended to use the default styling that is automatically provided by only calling
 *   [backgroundImage] with the content. It can be combined with the specified [colors]'s background
 *   color behind it.
 * @param style The style which provides the attribute values required for constructing this data
 *   card and its inner content. It also provides default style for the inner content, that can be
 *   overridden by each content slot. It is highly recommended to use one of
 *   [DataCardStyle.smallDataCardStyle], [DataCardStyle.defaultDataCardStyle],
 *   [DataCardStyle.largeDataCardStyle] or [DataCardStyle.extraLargeDataCardStyle] styles when
 *   either [icon] or [secondaryText] are present. If they are not present, it's highly recommended
 *   to use [DataCardStyle.smallCompactDataCardStyle], [DataCardStyle.defaultCompactDataCardStyle]
 *   or [DataCardStyle.largeCompactDataCardStyle].
 * @param contentPadding The inner padding used to prevent inner content from being too close to the
 *   card's edge. It's highly recommended to keep the default.
 * @sample androidx.wear.protolayout.material3.samples.dataCardSample
 */
// TODO: b/346958146 - link Card visuals in DAC
// TODO: b/373578620 - Add how corners affects margins in the layout.
public fun MaterialScope.textDataCard(
    onClick: Clickable,
    title: (MaterialScope.() -> LayoutElement),
    modifier: LayoutModifier = LayoutModifier,
    content: (MaterialScope.() -> LayoutElement)? = null,
    secondaryText: (MaterialScope.() -> LayoutElement)? = null,
    width: ContainerDimension = wrapWithMinTapTargetDimension(),
    height: ContainerDimension = wrapWithMinTapTargetDimension(),
    shape: Corner = shapes.large,
    colors: CardColors = filledCardColors(),
    background: (MaterialScope.() -> LayoutElement)? = null,
    style: DataCardStyle =
        if (secondaryText == null) defaultCompactDataCardStyle() else defaultDataCardStyle(),
    contentPadding: Padding = style.innerPadding,
): LayoutElement =
    card(
        onClick = onClick,
        modifier = modifier,
        width = width,
        height = height,
        shape = shape,
        backgroundColor = colors.background,
        background = background,
        contentPadding = contentPadding
    ) {
        buildContentForDataCard(
            title =
                withStyle(
                        defaultTextElementStyle =
                            TextElementStyle(
                                typography = style.titleTypography,
                                color = colors.title
                            )
                    )
                    .title(),
            content =
                content?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.contentTypography,
                                    color = colors.content
                                )
                        )
                        .it()
                },
            secondaryText =
                secondaryText?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.secondaryLabelTypography,
                                    color = colors.secondaryText
                                )
                        )
                        .it()
                },
            style = style,
        )
    }

/**
 * Opinionated ProtoLayout Material3 data card that offers up to 3 vertically stacked slots, usually
 * text or numeral based, with icon.
 *
 * Slots can have multiple placements, depending on their presence and [titleContentPlacement]:
 * * If [secondaryIcon] are set, it will be placed first when [titleContentPlacement] is set to
 *   [TitleContentPlacementInDataCard.Bottom], or last if [titleContentPlacement] is set to
 *   [TitleContentPlacementInDataCard.Top].
 * * If [secondaryIcon] is not set, this [textDataCard] is considered as `compact` data card, with
 *   title and content only.
 *
 * This card works well in [buttonGroup] with cards [width] and [height] set to [expand].
 *
 * @param onClick Associated [Clickable] for click events. When the card is clicked it will fire the
 *   associated action.
 * @param modifier Modifiers to set to this element. It's highly recommended to set a content
 *   description using [contentDescription].
 * @param title A slot for displaying the title of the card, expected to be one line of text. Uses
 *   [CardColors.title] color by default.
 * @param content The optional body content of the card. Uses [CardColors.content] color by default.
 * @param secondaryIcon An optional slot for displaying small icon, such as [secondaryIcon]. Uses
 *   [CardColors.secondaryIcon] tint color by default.
 * @param width The width of this card. It's highly recommended to set this to [expand] or [weight]
 *   for the most optimal experience across different screen sizes.
 * @param height The height of this card. It's highly recommended to set this to [expand] for the
 *   most optimal experience across different screen sizes.
 * @param shape Defines the card's shape, in other words the corner radius for this card.
 * @param colors The colors to be used for a background and inner content of this card. If the
 *   background image is also specified, the image will be laid out on top of the background color.
 *   In case of the fully opaque background image, then the background color will not be shown.
 *   Specified colors can be [CardDefaults.filledCardColors] for high emphasis card,
 *   [CardDefaults.filledVariantCardColors] for high/medium emphasis card,
 *   [CardDefaults.filledTonalCardColors] for low/medium emphasis card,
 *   [CardDefaults.imageBackgroundCardColors] for card with image as a background or custom built
 *   [CardColors].
 * @param background The background object to be used behind the content in the card. It is
 *   recommended to use the default styling that is automatically provided by only calling
 *   [backgroundImage] with the content. It can be combined with the specified [colors]'s background
 *   color behind it.
 * @param style The style which provides the attribute values required for constructing this data
 *   card and its inner content. It also provides default style for the inner content, that can be
 *   overridden by each content slot. It is highly recommended to use one of
 *   [DataCardStyle.smallDataCardStyle], [DataCardStyle.defaultDataCardStyle],
 *   [DataCardStyle.largeDataCardStyle] or [DataCardStyle.extraLargeDataCardStyle] styles when
 *   [secondaryIcon]is present. If it's not present, it's highly recommended to use
 *   [DataCardStyle.smallCompactDataCardStyle], [DataCardStyle.defaultCompactDataCardStyle] or
 *   [DataCardStyle.largeCompactDataCardStyle].
 * @param titleContentPlacement The placement of the [title] and [content] slots, relative to the
 *   given [secondaryIcon].
 * @param contentPadding The inner padding used to prevent inner content from being too close to the
 *   card's edge. It's highly recommended to keep the default.
 * @sample androidx.wear.protolayout.material3.samples.dataCardSample
 */
// TODO: b/346958146 - link Card visuals in DAC
// TODO: b/373578620 - Add how corners affects margins in the layout.
public fun MaterialScope.iconDataCard(
    onClick: Clickable,
    title: (MaterialScope.() -> LayoutElement),
    modifier: LayoutModifier = LayoutModifier,
    content: (MaterialScope.() -> LayoutElement)? = null,
    secondaryIcon: (MaterialScope.() -> LayoutElement)? = null,
    width: ContainerDimension = wrapWithMinTapTargetDimension(),
    height: ContainerDimension = wrapWithMinTapTargetDimension(),
    shape: Corner = shapes.large,
    colors: CardColors = filledCardColors(),
    background: (MaterialScope.() -> LayoutElement)? = null,
    style: DataCardStyle =
        if (secondaryIcon == null) defaultCompactDataCardStyle() else defaultDataCardStyle(),
    titleContentPlacement: TitleContentPlacementInDataCard = TitleContentPlacementInDataCard.Bottom,
    contentPadding: Padding = style.innerPadding,
): LayoutElement =
    card(
        onClick = onClick,
        modifier = modifier,
        width = width,
        height = height,
        shape = shape,
        backgroundColor = colors.background,
        background = background,
        contentPadding = contentPadding
    ) {
        buildContentForDataCard(
            title =
                withStyle(
                        defaultTextElementStyle =
                            TextElementStyle(
                                typography = style.titleTypography,
                                color = colors.title
                            )
                    )
                    .title(),
            content =
                content?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.contentTypography,
                                    color = colors.content
                                )
                        )
                        .it()
                },
            secondaryIcon =
                secondaryIcon?.let {
                    withStyle(
                            defaultIconStyle =
                                IconStyle(
                                    size = style.iconSize.toDp(),
                                    tintColor = colors.secondaryIcon
                                )
                        )
                        .it()
                },
            style = style,
            titleContentPlacement = titleContentPlacement
        )
    }

// TODO: b/368272767 - Link directly to progress indicator when available and mention icon in it.
/**
 * Opinionated ProtoLayout Material3 graphic data card that offers a slot for graphic data such as
 * progress indicator and up to 2 vertically stacked slots, usually for textual description.
 *
 * @param onClick Associated [Clickable] for click events. When the card is clicked it will fire the
 *   associated action.
 * @param title A slot for displaying the title of the card, expected to be one line of text. Uses
 *   [CardColors.title] color by default.
 * @param modifier Modifiers to set to this element. It's highly recommended to set a content
 *   description using [contentDescription].
 * @param content The optional body content of the card. Uses [CardColors.content] color by default.
 * @param graphic A slot for displaying graphic data, such as progress indicator.
 * @param height The width of this card. It's highly recommended to set this to [expand] for the
 *   most optimal experience across different screen sizes.
 * @param shape Defines the card's shape, in other words the corner radius for this card.
 * @param colors The colors to be used for a background and inner content of this card. Specified
 *   colors can be [CardDefaults.filledCardColors] for high emphasis card,
 *   [CardDefaults.filledVariantCardColors] for high/medium emphasis card,
 *   [CardDefaults.filledTonalCardColors] for low/medium emphasis card,
 *   [CardDefaults.imageBackgroundCardColors] for card with image as a background or custom built
 *   [CardColors].
 * @param style The style which provides the attribute values required for constructing this data
 *   card and its inner content. It also provides default style for the inner content, that can be
 *   overridden by each content slot. It is highly recommended to use one of
 *   [GraphicDataCardStyle.defaultGraphicDataCardStyle] or
 *   [GraphicDataCardStyle.largeGraphicDataCardStyle].
 * @param horizontalAlignment The horizontal placement of the [graphic] content. This can be either
 *   [HORIZONTAL_ALIGN_START] (when [graphic] is on the start side), or [HORIZONTAL_ALIGN_END] (when
 *   it is on the end side).
 * @param contentPadding The inner padding used to prevent inner content from being too close to the
 *   card's edge. It's highly recommended to keep the default.
 * @sample androidx.wear.protolayout.material3.samples.graphicDataCardSample
 */
// TODO: b/346958146 - link Card visuals in DAC
// TODO: b/373578620 - Add how corners affects margins in the layout.
public fun MaterialScope.graphicDataCard(
    onClick: Clickable,
    // TODO: b/368272767 - Potentially add helper for CPI and icon and link in KDocs.
    graphic: (MaterialScope.() -> LayoutElement),
    title: (MaterialScope.() -> LayoutElement),
    modifier: LayoutModifier = LayoutModifier,
    content: (MaterialScope.() -> LayoutElement)? = null,
    height: ContainerDimension = wrapWithMinTapTargetDimension(),
    shape: Corner = shapes.full,
    colors: CardColors = filledCardColors(),
    style: GraphicDataCardStyle = defaultGraphicDataCardStyle(),
    @HorizontalAlignment horizontalAlignment: Int = HORIZONTAL_ALIGN_START,
    contentPadding: Padding = style.innerPadding,
): LayoutElement =
    card(
        onClick = onClick,
        modifier = modifier,
        width = expand(),
        height = height,
        shape = shape,
        backgroundColor = colors.background,
        contentPadding = contentPadding
    ) {
        buildContentForGraphicDataCard(
            title =
                withStyle(
                        defaultTextElementStyle =
                            TextElementStyle(
                                typography = style.titleTypography,
                                color = colors.title
                            )
                    )
                    .title(),
            content =
                content?.let {
                    withStyle(
                            defaultTextElementStyle =
                                TextElementStyle(
                                    typography = style.contentTypography,
                                    color = colors.content
                                )
                        )
                        .it()
                },
            // TODO: b/368272767 - Rethink if we need separate style for graphic.
            graphic = graphic(),
            style = style,
            height = height,
            // Only support start and end align.
            horizontalAlignment =
                if (
                    horizontalAlignment != HORIZONTAL_ALIGN_START &&
                        horizontalAlignment != HORIZONTAL_ALIGN_END
                )
                    HORIZONTAL_ALIGN_START
                else horizontalAlignment
        )
    }

/**
 * ProtoLayout Material3 clickable component card that offers a single slot to take any content.
 *
 * It can be used as the container for more opinionated Card components that take specific content
 * such as icons, images, primary label, secondary label, etc.
 *
 * The Card is Rectangle shaped with some rounded corners by default. It is highly recommended to
 * set its width and height to fill the available space, by [expand] or [weight] for optimal
 * experience across different screen sizes.
 *
 * It can be used for displaying any clickable container with additional data, text or graphics.
 *
 * @param onClick Associated [Clickable] for click events. When the card is clicked it will fire the
 *   associated action.
 * @param modifier Modifiers to set to this element. It's highly recommended to set a content
 *   description using [contentDescription].
 * @param shape Defines the card's shape, in other words the corner radius for this card.
 * @param backgroundColor The color to be used as a background of this card. If the background image
 *   is also specified, the image will be laid out on top of this color. In case of the fully opaque
 *   background image, then this background color will not be shown.
 * @param background The background object to be used behind the content in the card. It is
 *   recommended to use the default styling that is automatically provided by only calling
 *   [backgroundImage] with the content. It can be combined with the specified [backgroundColor]
 *   behind it.
 * @param width The width of this card. It's highly recommended to set this to [expand] or [weight]
 * @param height The height of this card. It's highly recommended to set this to [expand] or
 *   [weight]
 * @param contentPadding The inner padding used to prevent inner content from being too close to the
 *   card's edge. It's highly recommended to keep the default.
 * @param content The inner content to be put inside of this card.
 * @sample androidx.wear.protolayout.material3.samples.cardSample
 */
// TODO: b/346958146 - link Card visuals in DAC
// TODO: b/373578620 - Add how corners affects margins in the layout.
public fun MaterialScope.card(
    onClick: Clickable,
    modifier: LayoutModifier = LayoutModifier,
    width: ContainerDimension = wrapWithMinTapTargetDimension(),
    height: ContainerDimension = wrapWithMinTapTargetDimension(),
    shape: Corner = shapes.large,
    backgroundColor: LayoutColor? = null,
    background: (MaterialScope.() -> LayoutElement)? = null,
    contentPadding: Padding = Padding.Builder().setAll(DEFAULT_CONTENT_PADDING.toDp()).build(),
    content: (MaterialScope.() -> LayoutElement)
): LayoutElement {
    val backgroundBuilder = Background.Builder().setCorner(shape)

    backgroundColor?.let { backgroundBuilder.setColor(it.prop) }

    val defaultModifier = LayoutModifier.semanticsRole(SEMANTICS_ROLE_BUTTON) then modifier
    val modifiers =
        defaultModifier
            .toProtoLayoutModifiersBuilder()
            .setClickable(onClick)
            .setMetadata(METADATA_TAG.toElementMetadata())
            .setBackground(backgroundBuilder.build())

    val cardContainer = Box.Builder().setHeight(height).setWidth(width).addContent(content())

    if (background == null) {
        modifiers.setPadding(contentPadding)
        cardContainer.setModifiers(modifiers.build())
        return cardContainer.build()
    }

    return Box.Builder()
        .setModifiers(modifiers.build())
        .addContent(
            withStyle(
                    defaultBackgroundImageStyle =
                        BackgroundImageStyle(
                            width = expand(),
                            height = expand(),
                            overlayColor = colorScheme.primary.withOpacity(0.6f),
                            overlayWidth = width,
                            overlayHeight = height,
                            shape = shape,
                            contentScaleMode = LayoutElementBuilders.CONTENT_SCALE_MODE_FILL_BOUNDS
                        )
                )
                .background()
        )
        .setWidth(width)
        .setHeight(height)
        .addContent(
            cardContainer
                // Padding in this case is needed on the inner content, not the whole card.
                .setModifiers(contentPadding.toModifiers())
                .build()
        )
        .build()
}
