package com.patagonian.lyrics.fragment.lyric

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.patagonian.lyrics.R

/**
 * A simple [Fragment] subclass that displays the lyric of a song.
 */
class LyricFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lyric, container, false)
    }
}
