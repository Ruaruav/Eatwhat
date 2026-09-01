package com.example.eatwhat.data

import android.content.Context
import org.json.JSONArray

const val MIN_ITEMS = 2
const val MAX_ITEMS = 12
const val MAX_NAME_LEN = 5
const val HISTORY_LIMIT = 6

val DEFAULT_ITEMS = listOf("火锅", "烧烤", "麻辣烫", "寿司", "汉堡", "饺子", "兰州拉面", "沙县小吃")

object PrefsStore {
    private const val PREF_NAME = "eatwhat_prefs"
    private const val KEY_ITEMS = "items"
    private const val KEY_HISTORY = "history"

    fun loadItems(context: Context): List<String> =
        readStringList(context, KEY_ITEMS, DEFAULT_ITEMS).let {
            if (it.size < MIN_ITEMS) DEFAULT_ITEMS else it
        }

    fun saveItems(context: Context, items: List<String>) {
        writeStringList(context, KEY_ITEMS, items)
    }

    fun loadHistory(context: Context): List<String> =
        readStringList(context, KEY_HISTORY, emptyList()).take(HISTORY_LIMIT)

    fun saveHistory(context: Context, history: List<String>) {
        writeStringList(context, KEY_HISTORY, history.take(HISTORY_LIMIT))
    }

    private fun readStringList(context: Context, key: String, default: List<String>): List<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(key, null) ?: return default
        return try {
            val arr = JSONArray(raw)
            val list = ArrayList<String>(arr.length())
            for (i in 0 until arr.length()) list.add(arr.getString(i))
            if (list.isEmpty()) default else list
        } catch (e: Exception) {
            default
        }
    }

    private fun writeStringList(context: Context, key: String, list: List<String>) {
        val arr = JSONArray()
        list.forEach { arr.put(it) }
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(key, arr.toString())
            .apply()
    }
}
