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

package androidx.wear.compose.material3

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max
import androidx.wear.compose.material3.tokens.IconToggleButtonTokens
import androidx.wear.compose.material3.tokens.MotionTokens
import androidx.wear.compose.material3.tokens.ShapeTokens
import androidx.wear.compose.materialcore.animateSelectionColor

/**
 * Wear Material [IconToggleButton] is a filled icon toggle button which switches between primary
 * colors and tonal colors depending on [checked] value, and offers a single slot for icon or image.
 *
 * Set the size of the [IconToggleButton] with Modifier.[touchTargetAwareSize] to ensure that the
 * background padding will correctly reach the edge of the minimum touch target. The recommended
 * icon toggle button sizes are [IconToggleButtonDefaults.DefaultButtonSize],
 * [IconToggleButtonDefaults.SmallButtonSize], [IconToggleButtonDefaults.LargeButtonSize] and
 * [IconToggleButtonDefaults.ExtraLargeButtonSize].
 *
 * Use [IconToggleButtonDefaults.iconSizeFor] to determine the icon size for a given
 * [IconToggleButton] size, or refer to icon sizes, [IconToggleButtonDefaults.DefaultIconSize],
 * [IconToggleButtonDefaults.LargeIconSize], [IconToggleButtonDefaults.ExtraLargeIconSize] directly.
 *
 * [IconToggleButton] can be enabled or disabled. A disabled button will not respond to click
 * events. When enabled, the checked and unchecked events are propagated by [onCheckedChange].
 *
 * A simple icon toggle button using the default colors, animated when pressed.
 *
 * @sample androidx.wear.compose.material3.samples.IconToggleButtonSample
 *
 * A simple icon toggle button using the default colors, animated when pressed, with different
 * shapes and icons for the checked and unchecked states.
 *
 * @sample androidx.wear.compose.material3.samples.IconToggleButtonVariantSample
 * @param checked Boolean flag indicating whether this toggle button is currently checked.
 * @param onCheckedChange Callback to be invoked when this toggle button is clicked.
 * @param modifier Modifier to be applied to the toggle button.
 * @param enabled Controls the enabled state of the toggle button. When `false`, this toggle button
 *   will not be clickable.
 * @param colors [IconToggleButtonColors] that will be used to resolve the container and content
 *   color for this toggle button.
 * @param interactionSource an optional hoisted [MutableInteractionSource] for observing and
 *   emitting [Interaction]s for this button. You can use this to change the button's appearance or
 *   preview the button in different states. Note that if `null` is provided, interactions will
 *   still happen internally.
 * @param shapes Defines the shape for this toggle button. Defaults to a static shape based on
 *   [IconToggleButtonDefaults.shape], but animated versions are available through
 *   [IconToggleButtonDefaults.animatedShapes] and [IconToggleButtonDefaults.variantAnimatedShapes].
 * @param border Optional [BorderStroke] for the [IconToggleButton].
 * @param content The content to be drawn inside the toggle button.
 */
@Composable
public fun IconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconToggleButtonColors = IconToggleButtonDefaults.iconToggleButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    shapes: IconToggleButtonShapes = IconToggleButtonDefaults.shapes(),
    border: BorderStroke? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val (finalShape, finalInteractionSource) =
        animateToggleButtonShape(
            uncheckedShape = shapes.unchecked,
            checkedShape = shapes.checked,
            uncheckedPressedShape = shapes.uncheckedPressed,
            checkedPressedShape = shapes.checkedPressed,
            onPressAnimationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
            onReleaseAnimationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
            checked = checked,
            interactionSource = interactionSource
        )

    androidx.wear.compose.materialcore.ToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.minimumInteractiveComponentSize(),
        enabled = enabled,
        backgroundColor = { isEnabled, isChecked ->
            colors.containerColor(enabled = isEnabled, checked = isChecked)
        },
        border = { _, _ -> border },
        toggleButtonSize = IconToggleButtonDefaults.DefaultButtonSize,
        interactionSource = finalInteractionSource,
        shape = finalShape,
        ripple = ripple(),
        content =
            provideScopeContent(colors.contentColor(enabled = enabled, checked = checked), content)
    )
}

/** Contains the default values used by [IconToggleButton]. */
public object IconToggleButtonDefaults {

    /** Recommended [Shape] for [IconToggleButton]. */
    public val shape: RoundedCornerShape
        @Composable get() = ShapeTokens.CornerFull

    /** Recommended pressed [Shape] for [IconToggleButton]. */
    public val pressedShape: CornerBasedShape
        @Composable get() = MaterialTheme.shapes.small

    /** Recommended checked [Shape] for [IconToggleButton]. */
    public val checkedShape: CornerBasedShape
        @Composable get() = MaterialTheme.shapes.medium

    /**
     * Recommended pressed shape corner fraction for [variantAnimatedShapes]. This fraction will be
     * applied to checked and unchecked shapes to generate the checkedPressed and uncheckedPressed
     * shapes.
     */
    public val PressedShapeCornerSizeFraction: Float = 0.66f

    /**
     * The recommended size of an icon when used inside an icon toggle button with size
     * [SmallButtonSize]. Use [iconSizeFor] to easily determine the icon size.
     */
    public val SmallIconSize: Dp = IconToggleButtonTokens.IconSmallSize

    /**
     * The default size of an icon when used inside an icon toggle button of size DefaultButtonSize.
     * Use [iconSizeFor] to easily determine the icon size.
     */
    public val DefaultIconSize: Dp = IconToggleButtonTokens.IconDefaultSize

    /**
     * The size of an icon when used inside an icon toggle button with size [LargeButtonSize]. Use
     * [iconSizeFor] to easily determine the icon size.
     */
    public val LargeIconSize: Dp = IconToggleButtonTokens.IconLargeSize

    /**
     * The size of an icon when used inside an icon toggle button with size [ExtraLargeButtonSize].
     * Use [iconSizeFor] to easily determine the icon size.
     */
    public val ExtraLargeIconSize: Dp = IconToggleButtonTokens.IconExtraLargeSize

    /**
     * The recommended size for a small button. It is recommended to apply this size using
     * Modifier.touchTargetAwareSize.
     */
    public val SmallButtonSize: Dp = IconToggleButtonTokens.ContainerSmallSize

    /**
     * The default size applied for icon toggle buttons. It is recommended to apply this size using
     * Modifier.touchTargetAwareSize.
     */
    public val DefaultButtonSize: Dp = IconToggleButtonTokens.ContainerDefaultSize

    /**
     * The recommended size for a large icon toggle button. It is recommended to apply this size
     * using Modifier.touchTargetAwareSize.
     */
    public val LargeButtonSize: Dp = IconToggleButtonTokens.ContainerLargeSize

    /**
     * The recommended size for an extra icon large toggle button. It is recommended to apply this
     * size using Modifier.touchTargetAwareSize.
     */
    public val ExtraLargeButtonSize: Dp = IconToggleButtonTokens.ContainerExtraLargeSize

    /**
     * Recommended icon size for a given icon toggle button size.
     *
     * Ensures that the minimum recommended icon size is applied.
     *
     * Examples: for size [SmallButtonSize], returns [SmallIconSize], for size
     * [ExtraLargeButtonSize] returns [ExtraLargeIconSize].
     *
     * @param buttonSize The size of the icon toggle button
     */
    public fun iconSizeFor(buttonSize: Dp): Dp =
        if (buttonSize >= LargeButtonSize) {
            max(LargeIconSize, buttonSize / 2f)
        } else {
            max(SmallIconSize, buttonSize / 2f)
        }

    /**
     * Creates a [IconToggleButtonShapes] with a static [shape].
     *
     * @param shape The normal shape of the IconToggleButton.
     */
    @Composable
    public fun shapes(
        shape: Shape = IconToggleButtonDefaults.shape,
    ): IconToggleButtonShapes = IconToggleButtonShapes(unchecked = shape)

    /**
     * Creates a [Shape] with a animation between two CornerBasedShapes.
     *
     * A simple icon toggle button using the default colors, animated when pressed.
     *
     * @sample androidx.wear.compose.material3.samples.IconToggleButtonSample
     * @param shape The normal shape of the IconToggleButton.
     * @param pressedShape The pressed shape of the IconToggleButton.
     */
    @Composable
    public fun animatedShapes(
        shape: CornerBasedShape = IconToggleButtonDefaults.shape,
        pressedShape: CornerBasedShape = IconToggleButtonDefaults.pressedShape,
    ): IconToggleButtonShapes = IconToggleButtonShapes(shape, null, pressedShape, null)

    /**
     * Creates a [Shape] with an animation between three [CornerSize]s based on the pressed state
     * and checked/unchecked.
     *
     * A simple icon toggle button using the default colors, animated on Press and Check/Uncheck:
     *
     * @sample androidx.wear.compose.material3.samples.IconToggleButtonVariantSample
     * @param uncheckedShape the unchecked shape.
     * @param checkedShape the checked shape.
     * @param pressedShapeCornerSizeFraction The fraction to apply to the uncheckedShape and
     *   checkedShape corner sizes, when the button is pressed. For example, the button shape
     *   animates from uncheckedShape to a new [CornerBasedShape] with corner size =
     *   uncheckedShape's corner size * pressedCornerShapeFraction. By default, the button corners
     *   are reduced in size when pressed, so that the button becomes more square.
     */
    @Composable
    public fun variantAnimatedShapes(
        uncheckedShape: CornerBasedShape = shape,
        checkedShape: CornerBasedShape = this.checkedShape,
        pressedShapeCornerSizeFraction: Float = PressedShapeCornerSizeFraction,
    ): IconToggleButtonShapes =
        IconToggleButtonShapes(
            unchecked = uncheckedShape,
            checked = checkedShape,
            uncheckedPressed =
                uncheckedShape.fractionalRoundedCornerShape(pressedShapeCornerSizeFraction),
            checkedPressed =
                checkedShape.fractionalRoundedCornerShape(pressedShapeCornerSizeFraction)
        )

    /**
     * Creates an [IconToggleButtonColors] for a [IconToggleButton]
     * - by default, a colored background with a contrasting content color.
     *
     * If the button is disabled, then the colors will have an alpha ([DisabledContentAlpha] and
     * [DisabledContainerAlpha]) value applied.
     */
    @Composable
    public fun iconToggleButtonColors(): IconToggleButtonColors =
        MaterialTheme.colorScheme.defaultIconToggleButtonColors

    /**
     * Creates a [IconToggleButtonColors] for a [IconToggleButton]
     * - by default, a colored background with a contrasting content color.
     *
     * If the button is disabled, then the colors will have an alpha ([DisabledContentAlpha] and
     * [DisabledContainerAlpha]) value applied.
     *
     * @param checkedContainerColor The container color of this [IconToggleButton] when enabled and
     *   checked
     * @param checkedContentColor The content color of this [IconToggleButton] when enabled and
     *   checked
     * @param uncheckedContainerColor The container color of this [IconToggleButton] when enabled
     *   and unchecked
     * @param uncheckedContentColor The content color of this [IconToggleButton] when enabled and
     *   unchecked
     * @param disabledCheckedContainerColor The container color of this [IconToggleButton] when
     *   checked and not enabled
     * @param disabledCheckedContentColor The content color of this [IconToggleButton] when checked
     *   and not enabled
     * @param disabledUncheckedContainerColor The container color of this [IconToggleButton] when
     *   unchecked and not enabled
     * @param disabledUncheckedContentColor The content color of this [IconToggleButton] when
     *   unchecked and not enabled
     */
    @Composable
    public fun iconToggleButtonColors(
        checkedContainerColor: Color = Color.Unspecified,
        checkedContentColor: Color = Color.Unspecified,
        uncheckedContainerColor: Color = Color.Unspecified,
        uncheckedContentColor: Color = Color.Unspecified,
        disabledCheckedContainerColor: Color = Color.Unspecified,
        disabledCheckedContentColor: Color = Color.Unspecified,
        disabledUncheckedContainerColor: Color = Color.Unspecified,
        disabledUncheckedContentColor: Color = Color.Unspecified,
    ): IconToggleButtonColors =
        MaterialTheme.colorScheme.defaultIconToggleButtonColors.copy(
            checkedContainerColor = checkedContainerColor,
            checkedContentColor = checkedContentColor,
            uncheckedContainerColor = uncheckedContainerColor,
            uncheckedContentColor = uncheckedContentColor,
            disabledCheckedContainerColor = disabledCheckedContainerColor,
            disabledCheckedContentColor = disabledCheckedContentColor,
            disabledUncheckedContainerColor = disabledUncheckedContainerColor,
            disabledUncheckedContentColor = disabledUncheckedContentColor,
        )

    private val ColorScheme.defaultIconToggleButtonColors: IconToggleButtonColors
        get() {
            return defaultIconToggleButtonColorsCached
                ?: IconToggleButtonColors(
                        checkedContainerColor =
                            fromToken(IconToggleButtonTokens.CheckedContainerColor),
                        checkedContentColor = fromToken(IconToggleButtonTokens.CheckedContentColor),
                        uncheckedContainerColor =
                            fromToken(IconToggleButtonTokens.UncheckedContainerColor),
                        uncheckedContentColor =
                            fromToken(IconToggleButtonTokens.UncheckedContentColor),
                        disabledCheckedContainerColor =
                            fromToken(IconToggleButtonTokens.DisabledCheckedContainerColor)
                                .toDisabledColor(
                                    disabledAlpha =
                                        IconToggleButtonTokens.DisabledCheckedContainerOpacity
                                ),
                        disabledCheckedContentColor =
                            fromToken(IconToggleButtonTokens.DisabledCheckedContentColor)
                                .toDisabledColor(
                                    disabledAlpha =
                                        IconToggleButtonTokens.DisabledCheckedContentOpacity
                                ),
                        disabledUncheckedContainerColor =
                            fromToken(IconToggleButtonTokens.DisabledUncheckedContainerColor)
                                .toDisabledColor(
                                    disabledAlpha =
                                        IconToggleButtonTokens.DisabledUncheckedContainerOpacity
                                ),
                        disabledUncheckedContentColor =
                            fromToken(IconToggleButtonTokens.DisabledUncheckedContentColor)
                                .toDisabledColor(
                                    disabledAlpha =
                                        IconToggleButtonTokens.DisabledUncheckedContentOpacity
                                ),
                    )
                    .also { defaultIconToggleButtonColorsCached = it }
        }
}

/**
 * Represents the different container and content colors used for [IconToggleButton] in various
 * states, that are checked, unchecked, enabled and disabled.
 *
 * @param checkedContainerColor Container or background color when the toggle button is checked
 * @param checkedContentColor Color of the content (icon) when the toggle button is checked
 * @param uncheckedContainerColor Container or background color when the toggle button is unchecked
 * @param uncheckedContentColor Color of the content (icon) when the toggle button is unchecked
 * @param disabledCheckedContainerColor Container or background color when the toggle button is
 *   disabled and checked
 * @param disabledCheckedContentColor Color of content (icon) when the toggle button is disabled and
 *   checked
 * @param disabledUncheckedContainerColor Container or background color when the toggle button is
 *   disabled and unchecked
 * @param disabledUncheckedContentColor Color of the content (icon) when the toggle button is
 *   disabled and unchecked
 */
@Immutable
public class IconToggleButtonColors(
    public val checkedContainerColor: Color,
    public val checkedContentColor: Color,
    public val uncheckedContainerColor: Color,
    public val uncheckedContentColor: Color,
    public val disabledCheckedContainerColor: Color,
    public val disabledCheckedContentColor: Color,
    public val disabledUncheckedContainerColor: Color,
    public val disabledUncheckedContentColor: Color,
) {
    /**
     * Returns a copy of this IconToggleButtonColors optionally overriding some of the values.
     *
     * @param checkedContainerColor Container or background color when the toggle button is checked
     * @param checkedContentColor Color of the content (text or icon) when the toggle button is
     *   checked
     * @param uncheckedContainerColor Container or background color when the toggle button is
     *   unchecked
     * @param uncheckedContentColor Color of the content (text or icon) when the toggle button is
     *   unchecked
     * @param disabledCheckedContainerColor Container or background color when the toggle button is
     *   disabled and checked
     * @param disabledCheckedContentColor Color of content (text or icon) when the toggle button is
     *   disabled and checked
     * @param disabledUncheckedContainerColor Container or background color when the toggle button
     *   is disabled and unchecked
     * @param disabledUncheckedContentColor Color of the content (text or icon) when the toggle
     *   button is disabled and unchecked
     */
    public fun copy(
        checkedContainerColor: Color = this.checkedContainerColor,
        checkedContentColor: Color = this.checkedContentColor,
        uncheckedContainerColor: Color = this.uncheckedContainerColor,
        uncheckedContentColor: Color = this.uncheckedContentColor,
        disabledCheckedContainerColor: Color = this.disabledCheckedContainerColor,
        disabledCheckedContentColor: Color = this.disabledCheckedContentColor,
        disabledUncheckedContainerColor: Color = this.disabledUncheckedContainerColor,
        disabledUncheckedContentColor: Color = this.disabledUncheckedContentColor,
    ): IconToggleButtonColors =
        IconToggleButtonColors(
            checkedContainerColor = checkedContainerColor.takeOrElse { this.checkedContainerColor },
            checkedContentColor = checkedContentColor.takeOrElse { this.checkedContentColor },
            uncheckedContainerColor =
                uncheckedContainerColor.takeOrElse { this.uncheckedContainerColor },
            uncheckedContentColor = uncheckedContentColor.takeOrElse { this.uncheckedContentColor },
            disabledCheckedContainerColor =
                disabledCheckedContainerColor.takeOrElse { this.disabledCheckedContainerColor },
            disabledCheckedContentColor =
                disabledCheckedContentColor.takeOrElse { this.disabledCheckedContentColor },
            disabledUncheckedContainerColor =
                disabledUncheckedContainerColor.takeOrElse { this.disabledUncheckedContainerColor },
            disabledUncheckedContentColor =
                disabledUncheckedContentColor.takeOrElse { this.disabledUncheckedContentColor },
        )

    /**
     * Determines the container color based on whether the toggle button is [enabled] and [checked].
     *
     * @param enabled Whether the toggle button is enabled
     * @param checked Whether the toggle button is checked
     */
    @Composable
    internal fun containerColor(enabled: Boolean, checked: Boolean): State<Color> =
        animateSelectionColor(
            enabled = enabled,
            checked = checked,
            checkedColor = checkedContainerColor,
            uncheckedColor = uncheckedContainerColor,
            disabledCheckedColor = disabledCheckedContainerColor,
            disabledUncheckedColor = disabledUncheckedContainerColor,
            animationSpec = COLOR_ANIMATION_SPEC
        )

    /**
     * Determines the content color based on whether the toggle button is [enabled] and [checked].
     *
     * @param enabled Whether the toggle button is enabled
     * @param checked Whether the toggle button is checked
     */
    @Composable
    internal fun contentColor(enabled: Boolean, checked: Boolean): State<Color> =
        animateSelectionColor(
            enabled = enabled,
            checked = checked,
            checkedColor = checkedContentColor,
            uncheckedColor = uncheckedContentColor,
            disabledCheckedColor = disabledCheckedContentColor,
            disabledUncheckedColor = disabledUncheckedContentColor,
            animationSpec = COLOR_ANIMATION_SPEC
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false

        other as IconToggleButtonColors

        if (checkedContainerColor != other.checkedContainerColor) return false
        if (checkedContentColor != other.checkedContentColor) return false
        if (uncheckedContainerColor != other.uncheckedContainerColor) return false
        if (uncheckedContentColor != other.uncheckedContentColor) return false
        if (disabledCheckedContainerColor != other.disabledCheckedContainerColor) return false
        if (disabledCheckedContentColor != other.disabledCheckedContentColor) return false
        if (disabledUncheckedContainerColor != other.disabledUncheckedContainerColor) return false
        if (disabledUncheckedContentColor != other.disabledUncheckedContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = checkedContainerColor.hashCode()
        result = 31 * result + checkedContentColor.hashCode()
        result = 31 * result + uncheckedContainerColor.hashCode()
        result = 31 * result + uncheckedContentColor.hashCode()
        result = 31 * result + disabledCheckedContainerColor.hashCode()
        result = 31 * result + disabledCheckedContentColor.hashCode()
        result = 31 * result + disabledUncheckedContainerColor.hashCode()
        result = 31 * result + disabledUncheckedContentColor.hashCode()
        return result
    }
}

/**
 * Represents the shapes used for [IconToggleButton] in various states.
 *
 * [IconToggleButtonShapes] offers flexibility in shape-morphing the IconToggleButton according to
 * the checked and pressed states. See [IconToggleButtonDefaults.shapes] (which maintains a fixed
 * shape for all states), [IconToggleButtonDefaults.animatedShapes] (which applies a shape-morph
 * when pressed) and [IconToggleButtonDefaults.variantAnimatedShapes] (which applies different
 * shapes for checked/unchecked and an additional morph to the current shape when pressed).
 *
 * @param unchecked the shape of the [IconToggleButton] when unchecked
 * @param checked the shape of the [IconToggleButton] when checked
 * @param uncheckedPressed the shape of the [IconToggleButton] when unchecked and pressed
 * @param checkedPressed the shape of the [IconToggleButton] when checked and pressed
 */
public class IconToggleButtonShapes(
    public val unchecked: Shape,
    public val checked: Shape? = null,
    public val uncheckedPressed: Shape? = null,
    public val checkedPressed: Shape? = uncheckedPressed
) {
    public fun copy(
        unchecked: Shape = this.unchecked,
        checked: Shape? = this.checked,
        uncheckedPressed: Shape? = this.uncheckedPressed,
        checkedPressed: Shape? = this.checkedPressed,
    ): IconToggleButtonShapes =
        IconToggleButtonShapes(unchecked, checked, uncheckedPressed, checkedPressed)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is IconToggleButtonShapes) return false

        if (unchecked != other.unchecked) return false
        if (checked != other.checked) return false
        if (uncheckedPressed != other.uncheckedPressed) return false
        if (checkedPressed != other.checkedPressed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = unchecked.hashCode()
        result = 31 * result + checked.hashCode()
        result = 31 * result + uncheckedPressed.hashCode()
        result = 31 * result + checkedPressed.hashCode()

        return result
    }
}

private val COLOR_ANIMATION_SPEC: AnimationSpec<Color> =
    tween(MotionTokens.DurationMedium1, 0, MotionTokens.EasingStandardDecelerate)
