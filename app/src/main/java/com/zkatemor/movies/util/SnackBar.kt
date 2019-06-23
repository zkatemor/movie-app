package com.zkatemor.movies.util

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.zkatemor.movies.R

class SnackBar(v: View){
    private val snackbar: Snackbar
    private val snackbar_view: View

    init {
        snackbar = Snackbar.make(v, R.string.text_error_bottom, Snackbar.LENGTH_LONG)
        snackbar_view = snackbar.view
    }

    fun show(context: Context) {
        snackbar_view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSnackBar))

        val snackbar_text = snackbar_view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        snackbar_text.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        snackbar_text.textSize = 14.0f

        snackbar.duration = 3000
        snackbar.show()
    }
}