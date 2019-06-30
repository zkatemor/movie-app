package com.zkatemor.movies.util

import android.content.Context
import android.content.SharedPreferences

private const val PREFERENCE_FILE_NAME = "com.zkatemor.movies.PREFERENCE_FILE_KEY"

fun Context.getPreferences(): SharedPreferences = getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)

/**
 * проверка избранного фильма
 * @param id - id фильма
 */
fun SharedPreferences.isLiked(id: Int) = contains(id.toString())

/**
 * сохранение фильма в избранном
 */
fun SharedPreferences.save(id: Int): Unit = edit().putInt(id.toString(), id).apply()

/**
 * удаление фильма из избранного
 */
fun SharedPreferences.remove(id: Int): Unit = edit().remove(id.toString()).apply()