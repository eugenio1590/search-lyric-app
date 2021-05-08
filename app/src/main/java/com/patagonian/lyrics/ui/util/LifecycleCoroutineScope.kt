package com.patagonian.lyrics.ui.util

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun LifecycleCoroutineScope.runInBackground(block: suspend () -> Unit) {
    launch {
        withContext(Dispatchers.IO) {
            block()
        }
    }
}