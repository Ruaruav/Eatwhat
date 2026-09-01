package com.example.eatwhat.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

private fun DrawScope.drawIconPath(
    tint: Color,
    viewBox: Float = 24f,
    strokeWidth: Float = 2f,
    block: Path.() -> Unit
) {
    val sx = size.width / viewBox
    val sy = size.height / viewBox
    val path = Path().apply(block)
    path.transform(Matrix().apply { scale(sx, sy) })
    drawPath(
        path = path,
        color = tint,
        style = Stroke(width = strokeWidth * minOf(sx, sy), cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

@Composable
fun BrandIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier) {
        val s = size.minDimension / 24f
        val c = Offset(size.width / 2f, size.height / 2f)
        drawCircle(tint, radius = 8f * s, center = c, style = Stroke(width = 2f * s, cap = StrokeCap.Round))
        drawCircle(tint, radius = 2.6f * s, center = c)
    }
}

@Composable
fun SlidersIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier) {
        val s = size.minDimension / 24f
        drawIconPath(tint, 24f, 2f) {
            moveTo(4f, 7f); lineTo(13f, 7f)
            moveTo(17f, 7f); lineTo(20f, 7f)
            moveTo(4f, 17f); lineTo(7f, 17f)
            moveTo(11f, 17f); lineTo(20f, 17f)
        }
        drawCircle(tint, 2.4f * s, Offset(15f * s, 7f * s), style = Stroke(width = 2f * s))
        drawCircle(tint, 2.4f * s, Offset(9f * s, 17f * s), style = Stroke(width = 2f * s))
    }
}

@Composable
fun ClockIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier) {
        val s = size.minDimension / 24f
        val c = Offset(size.width / 2f, size.height / 2f)
        drawCircle(tint, radius = 9f * s, center = c, style = Stroke(width = 2f * s, cap = StrokeCap.Round))
        drawIconPath(tint, 24f, 2f) {
            moveTo(12f, 7f); lineTo(12f, 12f); lineTo(15.2f, 14f)
        }
    }
}

@Composable
fun StarIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier) {
        drawIconPath(tint, 24f, 2.4f) {
            moveTo(12f, 3f)
            lineTo(14.5f, 8.5f)
            lineTo(20f, 9.3f)
            lineTo(16f, 13.3f)
            lineTo(17f, 19f)
            lineTo(12f, 16.2f)
            lineTo(7f, 19f)
            lineTo(8f, 13.3f)
            lineTo(4f, 9.3f)
            lineTo(9.5f, 8.5f)
            close()
        }
    }
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier) {
        drawIconPath(tint, 24f, 2.2f) {
            moveTo(6f, 6f); lineTo(18f, 18f)
            moveTo(18f, 6f); lineTo(6f, 18f)
        }
    }
}

@Composable
fun BackIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier) {
        drawIconPath(tint, 24f, 2f) {
            moveTo(19f, 12f); lineTo(5f, 12f)
            moveTo(11f, 18f); lineTo(5f, 12f); lineTo(11f, 6f)
        }
    }
}

@Composable
fun PlusIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier) {
        drawIconPath(tint, 24f, 2.4f) {
            moveTo(12f, 5f); lineTo(12f, 19f)
            moveTo(5f, 12f); lineTo(19f, 12f)
        }
    }
}

@Composable
fun TrashIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier) {
        drawIconPath(tint, 24f, 2f) {
            moveTo(4f, 7f); lineTo(20f, 7f)
            moveTo(10f, 11f); lineTo(10f, 17f)
            moveTo(14f, 11f); lineTo(14f, 17f)
            moveTo(6f, 7f); lineTo(7f, 19.5f); lineTo(8f, 20f); lineTo(16f, 20f); lineTo(17f, 19.5f); lineTo(18f, 7f)
            moveTo(9f, 7f); lineTo(9f, 5.5f); lineTo(10f, 4.5f); lineTo(14f, 4.5f); lineTo(15f, 5.5f); lineTo(15f, 7f)
        }
    }
}
