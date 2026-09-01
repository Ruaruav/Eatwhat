package com.example.eatwhat.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.eatwhat.ui.theme.SegmentColors
import com.example.eatwhat.ui.theme.ShadowColor
import com.example.eatwhat.ui.theme.Surface
import com.example.eatwhat.ui.theme.Warm

@Composable
fun WheelView(
    items: List<String>,
    rotation: Float,
    hubLabel: String,
    hubScale: Float,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            Modifier
                .matchParentSize()
                .graphicsLayer { rotationZ = rotation }
        ) {
            val s = size.minDimension / 200f
            val cx = size.width / 2f
            val cy = size.height / 2f
            val center = Offset(cx, cy)
            drawCircle(Surface, radius = 97f * s, center = center)
            val n = items.size
            if (n > 0) {
                val seg = 360f / n
                val radius = 96f * s
                val oval = Rect(cx - radius, cy - radius, cx + radius, cy + radius)
                for (i in 0 until n) {
                    val path = Path().apply {
                        moveTo(cx, cy)
                        arcTo(oval, i * seg - 90f, seg, false)
                        close()
                    }
                    drawPath(path, SegmentColors[i % SegmentColors.size])
                    drawPath(path, Surface, style = Stroke(width = 1.5f * s))
                }
                val fs = when { n <= 5 -> 11.5f; n <= 8 -> 9.5f; else -> 9f }
                val fsSp = with(density) { (fs * s).toSp() }
                val labelStyle = TextStyle(fontSize = fsSp, fontWeight = FontWeight.SemiBold, color = Color.White)
                for (i in 0 until n) {
                    val mid = i * seg + seg / 2f
                    val left = mid > 180f
                    val th = if (left) mid + 90f else mid - 90f
                    val anchorX = if (left) 66f else 134f
                    val ax = cx + (anchorX - 100f) * s
                    val layout = textMeasurer.measure(AnnotatedString(items[i]), style = labelStyle)
                    val textW = layout.size.width.toFloat()
                    val textH = layout.size.height.toFloat()
                    rotate(degrees = th, pivot = center) {
                        drawText(
                            layout,
                            topLeft = Offset(ax - (if (left) textW else 0f), cy - textH / 2f)
                        )
                    }
                }
            }
        }
        Canvas(Modifier.matchParentSize()) {
            val s = size.minDimension / 200f
            val cx = size.width / 2f
            val cy = size.height / 2f
            drawCircle(Surface, radius = 96f * s, center = Offset(cx, cy), style = Stroke(width = 3f * s))
            drawCircle(ShadowColor.copy(alpha = 0.22f), radius = 27f * s * hubScale, center = Offset(cx, cy + 2f * s))
            drawCircle(Surface, radius = 27f * s * hubScale, center = Offset(cx, cy))
            drawCircle(Warm, radius = 21f * s * hubScale, center = Offset(cx, cy))
            val layout = textMeasurer.measure(
                AnnotatedString(hubLabel),
                TextStyle(fontSize = with(density) { (17f * s).toSp() }, fontWeight = FontWeight.ExtraBold, color = Color.White)
            )
            drawText(layout, topLeft = Offset(cx - layout.size.width / 2f, cy - layout.size.height / 2f + 1.5f * s))
            val ptr = Path().apply {
                moveTo(cx + (88f - 100f) * s, cy + (1f - 100f) * s)
                lineTo(cx + (112f - 100f) * s, cy + (1f - 100f) * s)
                lineTo(cx + (100f - 100f) * s, cy + (25f - 100f) * s)
                close()
            }
            drawPath(ptr, Warm)
            drawPath(ptr, Surface, style = Stroke(width = 3f * s, join = StrokeJoin.Round))
        }
    }
}

@Composable
fun PreviewWheel(items: List<String>, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val s = size.minDimension / 100f
        val cx = size.width / 2f
        val cy = size.height / 2f
        val n = items.size
        if (n > 0) {
            val seg = 360f / n
            val radius = 48f * s
            val oval = Rect(cx - radius, cy - radius, cx + radius, cy + radius)
            for (i in 0 until n) {
                val path = Path().apply {
                    moveTo(cx, cy)
                    arcTo(oval, i * seg - 90f, seg, false)
                    close()
                }
                drawPath(path, SegmentColors[i % SegmentColors.size])
                drawPath(path, Surface, style = Stroke(width = 1f * s))
            }
        }
        drawCircle(ShadowColor.copy(alpha = 0.2f), 13f * s, Offset(cx, cy + 1.2f * s))
        drawCircle(Surface, 13f * s, Offset(cx, cy))
        drawCircle(Warm, 10f * s, Offset(cx, cy))
    }
}
