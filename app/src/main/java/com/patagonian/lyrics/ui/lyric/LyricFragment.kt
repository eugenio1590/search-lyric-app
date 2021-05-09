package com.patagonian.lyrics.ui.lyric

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.patagonian.lyrics.databinding.FragmentLyricBinding
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.ui.util.changeTitle

/**
 * A simple [Fragment] subclass that displays the lyric of a song.
 */
class LyricFragment : Fragment() {

    private val args: LyricFragmentArgs by navArgs()
    private val song: Song by lazy { args.song }
    private lateinit var binding: FragmentLyricBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLyricBinding.inflate(inflater, container, false)
        binding.song = song
        binding.executePendingBindings()
        requireActivity().changeTitle(song.title)
        return binding.root
    }
}
