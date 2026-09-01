package com.example.eatwhat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatwhat.data.MAX_ITEMS
import com.example.eatwhat.data.MAX_NAME_LEN
import com.example.eatwhat.data.MIN_ITEMS
import com.example.eatwhat.ui.theme.Accent
import com.example.eatwhat.ui.theme.AccentDeep
import com.example.eatwhat.ui.theme.AccentTint
import com.example.eatwhat.ui.theme.Bg
import com.example.eatwhat.ui.theme.Border
import com.example.eatwhat.ui.theme.BorderStrong
import com.example.eatwhat.ui.theme.Fg
import com.example.eatwhat.ui.theme.Hover
import com.example.eatwhat.ui.theme.Muted
import com.example.eatwhat.ui.theme.SegmentColors
import com.example.eatwhat.ui.theme.Surface

@Composable
fun EditScreen(
    items: List<String>,
    onClose: () -> Unit,
    onItemsChange: (List<String>) -> Unit,
    onShowSnack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var addText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        EditAppBar(count = items.size, onBack = onClose)

        PreviewCard(items = items)

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, Border, RoundedCornerShape(20.dp))
                .background(Surface)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(count = items.size, key = { it }) { index ->
                    ItemRow(
                        index = index,
                        name = items[index],
                        items = items,
                        onRename = { i, newName -> rename(items, i, newName, onItemsChange) },
                        onDelete = { i -> delete(items, i, onItemsChange, onShowSnack) },
                        onShowSnack = onShowSnack
                    )
                }
            }
        }

        AddBar(
            addText = addText,
            onAddTextChange = { if (it.length <= MAX_NAME_LEN) addText = it },
            onAdd = {
                add(items, addText.trim(), onItemsChange, onShowSnack).let { ok ->
                    if (ok) addText = ""
                    ok
                }
            }
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 22.dp)
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable {
                        onItemsChange(DEFAULT)
                        onShowSnack("已恢复默认选项")
                    }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("恢复默认", color = Muted, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Accent)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Text("完成", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

private val DEFAULT = listOf("火锅", "烧烤", "麻辣烫", "寿司", "汉堡", "饺子", "兰州拉面", "沙县小吃")

@Composable
private fun EditAppBar(count: Int, onBack: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Border, RoundedCornerShape(14.dp))
                .background(Surface)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            BackIcon(Modifier.size(21.dp), Fg)
        }
        Text(
            text = "自定义转盘",
            color = Fg,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.weight(1f)
        )
        Box(
            Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(AccentTint)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text("$count 个选项", color = AccentDeep, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PreviewCard(items: List<String>) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Border, RoundedCornerShape(20.dp))
            .background(Surface)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        PreviewWheel(items = items, modifier = Modifier.size(110.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${items.size}",
                    color = Fg,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 30.sp
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "个选项",
                    color = Muted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
            Text(
                text = "点按名称可直接修改，颜色按顺序自动分配。改动立即保存并应用到首页转盘。",
                color = Muted,
                fontSize = 13.sp,
                lineHeight = 21.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun ItemRow(
    index: Int,
    name: String,
    items: List<String>,
    onRename: (Int, String) -> Unit,
    onDelete: (Int) -> Unit,
    onShowSnack: (String) -> Unit
) {
    var value by remember(index) { mutableStateOf(name) }
    var lastValid by remember(index) { mutableStateOf(name) }
    var focused by remember { mutableStateOf(false) }
    val canDelete = items.size > MIN_ITEMS

    LaunchedEffect(name) {
        if (value != name) {
            value = name
            lastValid = name
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (focused) Hover else Surface)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SegmentColors[index % SegmentColors.size])
        )
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= MAX_NAME_LEN) {
                    value = newValue
                    onRename(index, newValue)
                }
            },
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .onFocusChanged { state ->
                    focused = state.isFocused
                    if (!state.isFocused) {
                        val v = value.trim()
                        if (v.isEmpty()) {
                            onShowSnack("选项名称不能为空")
                            value = lastValid
                            onRename(index, lastValid)
                        } else {
                            val dup = items.indexOfFirst { it == v }
                            if (dup != -1 && dup != index) {
                                onShowSnack("和第${dup + 1}项重名了")
                                value = lastValid
                                onRename(index, lastValid)
                            } else {
                                lastValid = v
                                onRename(index, v)
                            }
                        }
                    }
                }
                .drawBehind {
                    if (focused) {
                        drawLine(
                            color = Accent,
                            start = androidx.compose.ui.geometry.Offset(0f, size.height - 1.dp.toPx()),
                            end = androidx.compose.ui.geometry.Offset(size.width, size.height - 1.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                },
            singleLine = true,
            textStyle = TextStyle(color = Fg, fontSize = 15.sp, fontWeight = FontWeight.Medium),
            cursorBrush = SolidColor(Accent)
        )
        Box(
            Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(enabled = canDelete) { onDelete(index) },
            contentAlignment = Alignment.Center
        ) {
            TrashIcon(
                Modifier.size(19.dp),
                if (canDelete) Muted else Muted.copy(alpha = 0.35f)
            )
        }
    }
}

private fun rename(items: List<String>, index: Int, newName: String, onItemsChange: (List<String>) -> Unit) {
    val copy = items.toMutableList()
    copy[index] = newName
    onItemsChange(copy)
}

private fun delete(
    items: List<String>,
    index: Int,
    onItemsChange: (List<String>) -> Unit,
    onShowSnack: (String) -> Unit
) {
    if (items.size <= MIN_ITEMS) {
        onShowSnack("至少保留 $MIN_ITEMS 个选项")
        return
    }
    val removed = items[index]
    val copy = items.toMutableList().apply { removeAt(index) }
    onItemsChange(copy)
    onShowSnack("已删除「$removed」")
}

private fun add(
    items: List<String>,
    v: String,
    onItemsChange: (List<String>) -> Unit,
    onShowSnack: (String) -> Unit
): Boolean {
    if (v.isEmpty()) {
        onShowSnack("先输入选项名称")
        return false
    }
    if (v.length > MAX_NAME_LEN) {
        onShowSnack("名称最多 $MAX_NAME_LEN 个字")
        return false
    }
    if (items.contains(v)) {
        onShowSnack("已经有这个选项了")
        return false
    }
    if (items.size >= MAX_ITEMS) {
        onShowSnack("最多 $MAX_ITEMS 个选项")
        return false
    }
    onItemsChange(items + v)
    onShowSnack("已添加「$v」")
    return true
}

@Composable
private fun AddBar(
    addText: String,
    onAddTextChange: (String) -> Unit,
    onAdd: () -> Boolean
) {
    var focused by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 14.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(14.dp))
                .border(1.5.dp, if (focused) Accent else BorderStrong, RoundedCornerShape(14.dp))
                .background(Surface)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (addText.isEmpty()) {
                Text(
                    text = "输入新选项，如「轻食沙拉」",
                    color = Muted,
                    fontSize = 15.sp
                )
            }
            BasicTextField(
                value = addText,
                onValueChange = onAddTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focused = it.isFocused },
                singleLine = true,
                textStyle = TextStyle(color = Fg, fontSize = 15.sp, fontWeight = FontWeight.Medium),
                cursorBrush = SolidColor(Accent),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onAdd() })
            )
        }
        Box(
            Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Accent)
                .clickable { onAdd() },
            contentAlignment = Alignment.Center
        ) {
            PlusIcon(Modifier.size(22.dp), Color.White)
        }
    }
}
