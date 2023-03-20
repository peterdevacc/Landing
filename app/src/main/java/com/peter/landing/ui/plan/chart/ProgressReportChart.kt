package com.peter.landing.ui.plan.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.ui.theme.LandingAppTheme


@Composable
fun ProgressReportChart(todayProgress: List<Float>) {
    val todayChart = @Composable {
        ProgressChart(todayProgress)
    }

    Layout(
        contents = listOf(todayChart),
    ) { (todayChartMeasure), constraints ->

        val todayChartPlaceable = todayChartMeasure.first().measure(constraints)
        val totalHeight = todayChartPlaceable.height
        val totalWidth = todayChartPlaceable.width

        layout(totalWidth, totalHeight) {
            todayChartPlaceable.place(0, 0, 1f)
        }

    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ProgressChart(todayProgress: List<Float>) {
    val height = 218.dp
    val density = LocalDensity.current
    val heightPx = with(density) { height.toPx() }
    val frameColor = MaterialTheme.colorScheme.onBackground
    val strokeWidth = 4f

    val textMeasurer = rememberTextMeasurer()
    val sectionTextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
    val sectionTextLayout: (String) -> TextLayoutResult = {
        textMeasurer.measure(
            text = AnnotatedString(it),
            style = sectionTextStyle,
        )
    }

    val titleTextLayoutResult = textMeasurer.measure(
        text = AnnotatedString("最新课时进度"),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        ),
    )
    val titleTextSize = titleTextLayoutResult.size

    val mottoTextLayout = textMeasurer.measure(
        text = AnnotatedString("Work Smart"),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFEC5C5C)
        ),
    )
    val mottoTextSize = mottoTextLayout.size

    val chartDataList = listOf(
        Triple("学习", todayProgress[0], Color(0xFFe6933c)),
        Triple("单选", todayProgress[1], Color(0xFF6cb66f)),
        Triple("默写", todayProgress[2], Color(0xFF64abef)),
    )

    val dp16Px = with(density) { 16.dp.toPx() }
    val dp48Px = with(density) { 48.dp.toPx() }

    val textIndent = dp16Px * 1.6f
    val lineGap = dp48Px * 1.1f

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val fullProgressWidth = this.size.width - dp48Px * 2.4f

        drawRect(
            color = frameColor,
            topLeft = Offset.Zero,
            size = Size(this.size.width, heightPx),
            style = Stroke(width = strokeWidth)
        )

        drawText(
            textLayoutResult = titleTextLayoutResult,
            topLeft = Offset(
                (this.size.width / 2f) - (titleTextSize.width / 2f),
                dp16Px * 1.2f - (titleTextSize.height / 2f)
            ),
        )

        chartDataList.forEachIndexed { index, section ->
            val sectionTextLayoutResult = sectionTextLayout(section.first)
            val sectionTextSize = sectionTextLayoutResult.size
            val sectionTextStartX = textIndent - (sectionTextSize.width / 2f)
            val sectionTextStartY = dp48Px * 1.2f - (sectionTextSize.height / 2f) + (index * lineGap)
            val sectionProgressStartX = dp16Px * 1.4f + sectionTextSize.width * 0.8f
            val sectionProgressStartY = dp48Px * 1.12f - (sectionTextSize.height / 2f) + (index * lineGap)
            val progressWidth = fullProgressWidth * (section.second / 100f)

            drawText(
                textLayoutResult = sectionTextLayoutResult,
                topLeft = Offset(sectionTextStartX, sectionTextStartY),
            )

            drawRoundRect(
                color = section.third,
                topLeft = Offset(sectionProgressStartX, sectionProgressStartY),
                size = Size(
                    width = progressWidth,
                    height = sectionTextSize.height * 1.3f
                )
            )

            val progressTextLayoutResult = sectionTextLayout("${section.second.toInt()} %")
            val progressTextSize = progressTextLayoutResult.size
            val progressTextStartX = textIndent + sectionTextSize.width + progressWidth + dp16Px - (progressTextSize.width / 2f)
            val progressTextStartY = dp48Px * 1.18f - (progressTextSize.height / 2f) + (index * lineGap)

            drawText(
                textLayoutResult = progressTextLayoutResult,
                topLeft = Offset(progressTextStartX, progressTextStartY),
            )
        }

        drawText(
            textLayoutResult = mottoTextLayout,
            topLeft = Offset(
                (this.size.width / 2f) - (mottoTextSize.width / 2f),
                heightPx - dp16Px * 1.1f - (mottoTextSize.height / 2f)
            ),
        )
    }
}

@Preview(name = "Progress Report Chart", showBackground = true)
@Composable
fun ProgressReportChartPreview() {
    LandingAppTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.padding(8.dp)) {
                ProgressReportChart(listOf(100f, 100f, 100f))
            }
        }
    }
}
