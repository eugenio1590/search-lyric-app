package com.patagonian.lyrics.fragment.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.patagonian.lyrics.R

/**
 * A simple [Fragment] subclass that contains a list of the previously retrieved songs.
 */
class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }
}
