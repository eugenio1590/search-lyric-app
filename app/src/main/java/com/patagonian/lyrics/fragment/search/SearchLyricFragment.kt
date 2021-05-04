package com.patagonian.lyrics.fragment.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.patagonian.lyrics.R

/**
 * A simple [Fragment] subclass that allows searching a lyric
 * based on the author's name and song title.
 */
class SearchLyricFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_lyric, container, false)
    }
}
