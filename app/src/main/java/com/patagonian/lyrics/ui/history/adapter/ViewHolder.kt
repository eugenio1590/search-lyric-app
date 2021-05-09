package com.patagonian.lyrics.ui.history.adapter

import androidx.recyclerview.widget.RecyclerView
import com.patagonian.lyrics.databinding.SongItemBinding
import com.patagonian.lyrics.domain.entity.Song

class ViewHolder(
    private val binding: SongItemBinding,
    private val navigationListener: NavigationListener
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(song: Song) {
        binding.song = song
        binding.executePendingBindings()
        binding.root.setOnClickListener {
            navigationListener.displayLyric(song)
        }
    }

    interface NavigationListener {
        fun displayLyric(song: Song)
    }
}