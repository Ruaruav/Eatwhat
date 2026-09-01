package com.example.eatwhat.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatwhat.ui.theme.AccentDeep
import com.example.eatwhat.ui.theme.AccentTint
import com.example.eatwhat.ui.theme.Bg
import com.example.eatwhat.ui.theme.Border
import com.example.eatwhat.ui.theme.Fg
import com.example.eatwhat.ui.theme.Muted
import com.example.eatwhat.ui.theme.Surface
import com.example.eatwhat.ui.theme.WarmDeep
import com.example.eatwhat.ui.theme.WarmTint

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    items: List<String>,
    history: List<String>,
    rotation: Float,
    hubLabel: String,
    spinning: Boolean,
    status: AnnotatedString,
    onSpin: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hubPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1300), RepeatMode.Reverse),
        label = "hubScale"
    )
    val hubScale = if (spinning) 1f else scale

    Column(
        modifier = modifier.fillMaxSize().background(Bg).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeAppBar(onEdit = onEdit)

        Spacer(Modifier.height(8.dp))

        Text(
            text = status,
            color = Muted,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp).height(21.dp)
        )

        Spacer(Modifier.height(16.dp))

        BoxWithConstraints(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            val wheelSize = minOf(maxWidth, maxHeight, 344.dp)
            WheelView(
                items = items,
                rotation = rotation,
                hubLabel = hubLabel,
                hubScale = hubScale,
                enabled = !spinning && items.size >= 2,
                onClick = onSpin,
                modifier = Modifier.size(wheelSize)
            )
        }

        HistorySection(history = history, modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 20.dp))
    }
}

@Composable
private fun HomeAppBar(onEdit: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(WarmTint),
            contentAlignment = Alignment.Center
        ) {
            BrandIcon(Modifier.size(22.dp), WarmDeep)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = "今天吃什么",
                color = Fg,
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 25.sp
            )
            Text(
                text = "让转盘替你决定这一餐",
                color = Muted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Border, RoundedCornerShape(14.dp))
                .background(Surface)
                .clickable(onClick = onEdit),
            contentAlignment = Alignment.Center
        ) {
            SlidersIcon(Modifier.size(21.dp), Fg)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HistorySection(history: List<String>, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = "最近的结果",
            color = Muted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        if (history.isEmpty()) {
            Text(
                text = "转一次，结果会记在这里",
                color = Muted,
                fontSize = 13.sp
            )
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                history.forEachIndexed { index, name ->
                    HistoryChip(name = name, latest = index == 0)
                }
            }
        }
    }
}

@Composable
private fun HistoryChip(name: String, latest: Boolean) {
    Row(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(if (latest) AccentTint else Surface)
            .border(
                if (latest) BorderStroke(0.dp, AccentTint)
                else BorderStroke(1.dp, Border),
                RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ClockIcon(Modifier.size(14.dp), if (latest) AccentDeep else Muted)
        Text(
            text = name,
            color = if (latest) AccentDeep else Fg,
            fontSize = 13.sp,
            fontWeight = if (latest) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
