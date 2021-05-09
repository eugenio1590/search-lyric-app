package com.patagonian.lyrics.ui.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun Activity.hideKeyboard() {
    val view: View? = findViewById(android.R.id.content)
    if (view != null) {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.changeTitle(title: String) {
    this.title = title
    (this as? AppCompatActivity)?.supportActionBar?.title = title
}