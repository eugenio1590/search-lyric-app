package com.patagonian.lyrics.ui.util

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorText")
fun setError(textInputLayout: TextInputLayout, error: String?) {
    textInputLayout.isErrorEnabled = error != null
    textInputLayout.error = error
}