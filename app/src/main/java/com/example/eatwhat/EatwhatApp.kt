package com.example.eatwhat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.eatwhat.data.MIN_ITEMS
import com.example.eatwhat.data.PrefsStore
import com.example.eatwhat.ui.EditScreen
import com.example.eatwhat.ui.HomeScreen
import com.example.eatwhat.ui.ResultSheet
import com.example.eatwhat.ui.Snackbar
import com.example.eatwhat.ui.theme.Bg
import com.example.eatwhat.ui.theme.Fg
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.random.Random

@Composable
fun EatwhatApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var items by remember { mutableStateOf(PrefsStore.loadItems(context)) }
    var history by remember { mutableStateOf(PrefsStore.loadHistory(context)) }
    val rotationAnim = remember { Animatable(0f) }
    var spinning by remember { mutableStateOf(false) }
    var hubLabel by remember { mutableStateOf("转") }
    var sheetOpen by remember { mutableStateOf(false) }
    var sheetName by remember { mutableStateOf("") }
    var sheetCount by remember { mutableStateOf(0) }
    var editOpen by remember { mutableStateOf(false) }
    var snackbar by remember { mutableStateOf<String?>(null) }
    var snackbarJob by remember { mutableStateOf<Job?>(null) }

    var statusText by remember {
        mutableStateOf(buildAnnotatedString { append("想吃啥？点转盘马上决定") })
    }

    fun persistItems(newItems: List<String>) {
        items = newItems
        PrefsStore.saveItems(context, newItems)
    }

    fun showSnack(msg: String) {
        snackbarJob?.cancel()
        snackbar = msg
        snackbarJob = scope.launch {
            delay(2200)
            if (snackbar == msg) snackbar = null
        }
    }

    fun spin() {
        if (spinning || items.size < MIN_ITEMS) return
        sheetOpen = false
        spinning = true
        hubLabel = "…"
        statusText = buildAnnotatedString { append("正在帮你选…") }
        val turns = 5 + Random.nextInt(3)
        val start = rotationAnim.value
        val target = start + turns * 360f + Random.nextFloat() * 360f
        scope.launch {
            rotationAnim.animateTo(
                targetValue = target,
                animationSpec = tween(4400, easing = CubicBezierEasing(0.15f, 0.62f, 0.12f, 1f))
            )
            if (!isActive) return@launch
            spinning = false
            hubLabel = "转"
            val n = items.size
            val norm = (360f - (rotationAnim.value % 360f + 360f) % 360f) % 360f
            val idx = floor(norm / (360f / n)).toInt() % n
            val name = items[idx]
            val newHistory = (listOf(name) + history).take(6)
            history = newHistory
            PrefsStore.saveHistory(context, newHistory)
            sheetName = name
            sheetCount = newHistory.size
            sheetOpen = true
            statusText = buildAnnotatedString {
                append("今天吃")
                withStyle(SpanStyle(color = Fg, fontWeight = FontWeight.Bold)) { append("「$name」") }
            }
        }
    }

    fun closeSheet() {
        sheetOpen = false
    }

    fun onOk() {
        closeSheet()
        hubLabel = "转"
        statusText = buildAnnotatedString {
            append("祝你好胃口！想换口味就")
            withStyle(SpanStyle(color = Fg, fontWeight = FontWeight.Bold)) { append("再转一次") }
        }
    }

    fun onSpinAgain() {
        closeSheet()
        scope.launch {
            delay(120)
            spin()
        }
    }

    fun openEditor() {
        closeSheet()
        editOpen = true
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        HomeScreen(
            items = items,
            history = history,
            rotation = rotationAnim.value,
            hubLabel = hubLabel,
            spinning = spinning,
            status = statusText,
            onSpin = ::spin,
            onEdit = ::openEditor
        )

        ResultSheet(
            visible = sheetOpen,
            name = sheetName,
            subtitle = if (sheetCount > 1) "别纠结，就它了。 这是今天第 $sheetCount 次选择。" else "别纠结，就它了。",
            onDismiss = ::closeSheet,
            onConfirm = ::onOk,
            onSpinAgain = ::onSpinAgain
        )

        AnimatedVisibility(
            visible = editOpen,
            enter = slideInVertically(
                animationSpec = tween(400, easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)),
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(tween(350)) { it },
            modifier = Modifier.fillMaxSize()
        ) {
            EditScreen(
                items = items,
                onClose = { editOpen = false },
                onItemsChange = ::persistItems,
                onShowSnack = ::showSnack
            )
        }

        Snackbar(
            message = snackbar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 96.dp)
        )
    }
}
