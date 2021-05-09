package com.patagonian.lyrics.ui.util

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date

@BindingAdapter("errorText")
fun setError(textInputLayout: TextInputLayout, error: String?) {
    textInputLayout.isErrorEnabled = error != null
    textInputLayout.error = error
}

@BindingAdapter("dateText")
@SuppressLint("SimpleDateFormat")
fun setDate(textView: TextView, date: Date?) = date?.let{
    val dateFormat = SimpleDateFormat("MM/dd/yyyy - hh:mm a")
    textView.text = dateFormat.format(it)
}