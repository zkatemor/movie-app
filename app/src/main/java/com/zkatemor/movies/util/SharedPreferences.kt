package com.zkatemor.movies.util

import android.content.Context
import android.content.SharedPreferences

private const val PREFERENCE_FILE_NAME = "com.zkatemor.movies.PREFERENCE_FILE_KEY"

fun Context.getPreferences(): SharedPreferences = getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)

fun SharedPreferences.isLiked(id: Int) = contains(id.toString())

fun SharedPreferences.save(id: Int): Unit = edit().putInt(id.toString(), id).apply()

fun SharedPreferences.remove(id: Int): Unit = edit().remove(id.toString()).apply()