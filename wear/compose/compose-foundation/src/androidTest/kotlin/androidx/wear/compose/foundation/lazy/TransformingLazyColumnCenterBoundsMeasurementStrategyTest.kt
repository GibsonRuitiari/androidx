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

package androidx.wear.compose.foundation.lazy

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.wear.compose.foundation.lazy.layout.LazyLayoutKeyIndexMap
import com.google.common.truth.Truth.assertThat
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class TransformingLazyColumnCenterBoundsMeasurementStrategyTest {
    private val screenHeight = 100
    private val screenWidth = 120
    private val density = 1f
    private val containerConstraints =
        Constraints(
            minWidth = screenWidth,
            maxWidth = screenWidth,
            minHeight = screenHeight,
            maxHeight = screenHeight
        )

    @Test
    fun emptyList_emptyResult() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()

        val result = strategy.measure(listOf())

        assertThat(result.visibleItems).isEmpty()
    }

    @Test
    fun fullScreenItem_takesFullHeight() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()

        val result = strategy.measure(listOf(screenHeight))

        assertThat(result.visibleItems.size).isEqualTo(1)

        assertThat(result.visibleItems.first().index).isEqualTo(0)
        assertThat(result.visibleItems.first().offset).isEqualTo(0)
        assertThat(result.visibleItems.first().measuredHeight).isEqualTo(screenHeight)
        assertThat(result.visibleItems.first().transformedHeight).isEqualTo(screenHeight)
    }

    @Test
    fun fullScreenItem_scrollsBackToCenter() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()

        val result =
            strategy.measure(
                listOf(screenHeight),
                // Scroll is ignored as the item constrained by the screen.
                scrollToBeConsumed = 25f
            )

        assertThat(result.visibleItems.size).isEqualTo(1)

        assertThat(result.visibleItems.first().index).isEqualTo(0)
        assertThat(result.visibleItems.first().offset).isEqualTo(0)
        assertThat(result.visibleItems.first().measuredHeight).isEqualTo(screenHeight)
        assertThat(result.visibleItems.first().transformedHeight).isEqualTo(screenHeight)
    }

    @Test
    fun halfScreenItem_takesHalfHeightAndCentered() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()

        val result = strategy.measure(listOf(screenHeight / 2))

        assertThat(result.visibleItems.size).isEqualTo(1)

        assertThat(result.visibleItems.first().index).isEqualTo(0)
        assertThat(result.visibleItems.first().offset).isEqualTo(screenHeight / 4)
    }

    @Test
    fun twoItemsWithFirstOneCentered_measuredWithCorrectOffsets() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()

        val result =
            strategy.measure(
                listOf(
                    // Is centered.
                    screenHeight / 2,
                    screenHeight / 2,
                )
            )

        assertThat(result.visibleItems.size).isEqualTo(2)
        assertThat(result.visibleItems.map { it.offset })
            .isEqualTo(
                listOf(
                    screenHeight / 4,
                    screenHeight * 3 / 4,
                )
            )
    }

    @Test
    fun threeHalfScreenItemsWithFirstOneCentered_pushesLastItemOffscreen() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()

        val result =
            strategy.measure(
                listOf(
                    // Is centered.
                    screenHeight / 2,
                    screenHeight / 2,
                    // Offscreen item.
                    screenHeight / 2,
                )
            )

        assertThat(result.visibleItems.size).isEqualTo(2)
    }

    @Test
    fun threeItemsWithSecondOneCentered_measuredWithCorrectOffsets() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()

        val result =
            strategy.measure(
                listOf(
                    screenHeight / 2,
                    // Is centered.
                    screenHeight / 2,
                    // Offscreen item.
                    screenHeight / 2,
                ),
                anchorItemIndex = 1,
            )

        assertThat(result.visibleItems.size).isEqualTo(3)
        assertThat(result.visibleItems.map { it.offset })
            .isEqualTo(listOf(-screenHeight / 4, screenHeight / 4, screenHeight * 3 / 4))
    }

    @Test
    fun threeItemsWithSecondOneCenteredAndOffset_measuredWithCorrectOffsets() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()
        val tinyOffset = 5
        val result =
            strategy.measure(
                listOf(
                    screenHeight / 2,
                    // Is centered with the offset.
                    screenHeight / 2,
                    screenHeight / 2,
                ),
                anchorItemIndex = 1,
                anchorItemScrollOffset = tinyOffset,
            )

        assertThat(result.visibleItems.size).isEqualTo(3)
        assertThat(result.visibleItems.map { it.offset })
            .isEqualTo(
                listOf(
                    -screenHeight / 4 + tinyOffset,
                    screenHeight / 4 + tinyOffset,
                    screenHeight * 3 / 4 + tinyOffset
                )
            )
    }

    @Test
    fun threeItemsWithSecondOneCenteredAndScrolled_measuredWithCorrectOffsets() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()
        val scrollAmount = 5
        val result =
            strategy.measure(
                listOf(
                    screenHeight / 2,
                    // Is centered with the scroll.
                    screenHeight / 2,
                    // Offscreen item.
                    screenHeight / 2,
                ),
                anchorItemIndex = 1,
                scrollToBeConsumed = scrollAmount.toFloat(),
            )

        assertThat(result.visibleItems.size).isEqualTo(3)
        assertThat(result.visibleItems.map { it.offset })
            .isEqualTo(
                listOf(
                    -screenHeight / 4 + scrollAmount,
                    screenHeight / 4 + scrollAmount,
                    screenHeight * 3 / 4 + scrollAmount
                )
            )
    }

    @Test
    fun fullScreenItemWithTransformedHeight_takesHalfOfHeight() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()
        val result =
            strategy.measure(
                listOf(
                    // Center item that appears half of the size.
                    screenHeight,
                ),
                transformedHeight = { measuredHeight, _ -> measuredHeight / 2 }
            )

        assertThat(result.visibleItems.size).isEqualTo(1)
        assertThat(result.visibleItems.first().offset).isEqualTo(screenHeight / 4)
        assertThat(result.visibleItems.first().measuredHeight).isEqualTo(screenHeight)
        assertThat(result.visibleItems.first().transformedHeight).isEqualTo(screenHeight / 2)
    }

    @Test
    fun dynamicHeightItems_measuredWithCorrectOffsets() {
        val strategy = TransformingLazyColumnCenterBoundsMeasurementStrategy()
        val result =
            strategy.measure(
                listOf(
                    // Will be half of the size and therefore placed at 0 offset.
                    screenHeight / 2,
                    // Centered.
                    screenHeight / 2,
                ),
                anchorItemIndex = 1,
                transformedHeight = { measuredHeight, scrollProgression ->
                    if (scrollProgression.topOffsetFraction < 0.25f) {
                        measuredHeight / 2
                    } else measuredHeight
                }
            )

        assertThat(result.visibleItems.size).isEqualTo(2)
        assertThat(result.visibleItems.map { it.offset }).isEqualTo(listOf(0, screenHeight / 4))
    }

    private fun TransformingLazyColumnMeasurementStrategy.measure(
        itemHeights: List<Int>,
        transformedHeight: ((Int, TransformingLazyColumnItemScrollProgress) -> Int)? = null,
        itemSpacing: Int = 0,
        anchorItemIndex: Int = 0,
        anchorItemScrollOffset: Int = 0,
        lastMeasuredAnchorItemHeight: Int = Int.MIN_VALUE,
        scrollToBeConsumed: Float = 0f,
    ): TransformingLazyColumnMeasureResult =
        measure(
            itemsCount = itemHeights.size,
            measuredItemProvider = makeMeasuredItemProvider(itemHeights, transformedHeight),
            keyIndexMap = LazyLayoutKeyIndexMap.Empty,
            itemSpacing = itemSpacing,
            containerConstraints = containerConstraints,
            anchorItemIndex = anchorItemIndex,
            anchorItemScrollOffset = anchorItemScrollOffset,
            lastMeasuredAnchorItemHeight = lastMeasuredAnchorItemHeight,
            scrollToBeConsumed = scrollToBeConsumed,
            coroutineScope = CoroutineScope(EmptyCoroutineContext),
            density = Density(density),
            layout = { width, height, _ ->
                object : MeasureResult {
                    override val width = width
                    override val height = height
                    override val alignmentLines
                        get() = TODO("Not yet implemented")

                    override fun placeChildren() {}
                }
            },
        )

    private class EmptyPlaceable(
        width: Int,
        height: Int,
        val transformedHeight: ((Int, TransformingLazyColumnItemScrollProgress) -> Int)?
    ) : Placeable() {
        init {
            measuredSize = IntSize(width, height)
        }

        override fun get(alignmentLine: AlignmentLine): Int = AlignmentLine.Unspecified

        override fun placeAt(
            position: IntOffset,
            zIndex: Float,
            layerBlock: (GraphicsLayerScope.() -> Unit)?
        ) {}

        override val parentData: Any?
            get() = transformedHeight?.let { TransformingLazyColumnParentData(it) }
    }

    private fun makeMeasuredItemProvider(
        itemHeights: List<Int>,
        transformedHeight: ((Int, TransformingLazyColumnItemScrollProgress) -> Int)? = null
    ) = MeasuredItemProvider { index, offset, progressProvider ->
        TransformingLazyColumnMeasuredItem(
            index = index,
            offset = offset,
            placeable =
                EmptyPlaceable(
                    width = screenWidth,
                    height = itemHeights[index],
                    transformedHeight = transformedHeight
                ),
            containerConstraints = containerConstraints,
            leftPadding = 0,
            rightPadding = 0,
            measureScrollProgress = progressProvider(itemHeights[index]),
            horizontalAlignment = Alignment.CenterHorizontally,
            layoutDirection = LayoutDirection.Ltr,
            key = index,
            contentType = null,
        )
    }
}
