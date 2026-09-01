package com.example.eatwhat.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatwhat.ui.theme.Accent
import com.example.eatwhat.ui.theme.BorderStrong
import com.example.eatwhat.ui.theme.Fg
import com.example.eatwhat.ui.theme.Muted
import com.example.eatwhat.ui.theme.ShadowColor
import com.example.eatwhat.ui.theme.Surface
import com.example.eatwhat.ui.theme.WarmDeep

@Composable
fun ResultSheet(
    visible: Boolean,
    name: String,
    subtitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onSpinAgain: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(250)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(ShadowColor.copy(alpha = 0.45f))
                    .clickable(onClick = onDismiss)
            )
        }
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(380, easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)),
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(tween(300)) { it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            SheetContent(
                name = name,
                subtitle = subtitle,
                onDismiss = onDismiss,
                onConfirm = onConfirm,
                onSpinAgain = onSpinAgain
            )
        }
    }
}

@Composable
private fun SheetContent(
    name: String,
    subtitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onSpinAgain: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
            .shadow(
                elevation = 32.dp,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
                ambientColor = ShadowColor.copy(alpha = 0.18f),
                spotColor = ShadowColor.copy(alpha = 0.18f)
            )
            .background(Surface)
            .navigationBarsPadding()
            .padding(start = 24.dp, end = 24.dp, bottom = 30.dp)
    ) {
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 14.dp, bottom = 16.dp)
                .size(width = 38.dp, height = 4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(BorderStrong)
        )
        Box(
            Modifier
                .align(Alignment.End)
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            CloseIcon(Modifier.size(20.dp), Muted)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StarIcon(Modifier.size(16.dp), WarmDeep)
            Text(
                text = "今日推荐",
                color = WarmDeep,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
        }
        Text(
            text = name,
            color = Fg,
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 48.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
        )
        Text(
            text = subtitle,
            color = Muted,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 22.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(1.5.dp, BorderStrong, RoundedCornerShape(15.dp))
                    .background(Surface)
                    .clickable(onClick = onSpinAgain),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "再转一次",
                    color = Fg,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Box(
                Modifier
                    .weight(1.4f)
                    .height(52.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Accent)
                    .clickable(onClick = onConfirm),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "好，就吃这个",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
