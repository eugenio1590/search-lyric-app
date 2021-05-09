package com.patagonian.lyrics.ui.history

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.patagonian.lyrics.R
import com.patagonian.lyrics.databinding.FragmentHistoryBinding
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.EmptyRecordList
import com.patagonian.lyrics.domain.interactor.search.history.SearchSongHistory
import com.patagonian.lyrics.presentation.search.history.ViewModel
import com.patagonian.lyrics.ui.UIState
import com.patagonian.lyrics.ui.history.adapter.Adapter
import com.patagonian.lyrics.ui.history.adapter.ViewHolder
import com.patagonian.lyrics.ui.util.runInBackground
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass that contains a list of the previously retrieved songs.
 */
class HistoryFragment : Fragment(), ViewHolder.NavigationListener {

    companion object {
        private val TAG = HistoryFragment::class.java.canonicalName
    }

    private val viewModel: ViewModel by inject()
    private val presenter: SearchSongHistory.Presenter by inject { parametersOf(viewModel) }

    private lateinit var binding: FragmentHistoryBinding
    private val songListAdapter = Adapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        initUI()
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_search_lyric -> {
            searchLyric()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun displayLyric(song: Song) {
        findNavController().navigate(HistoryFragmentDirections.actionHistoryToLyric(song))
    }

    private fun initUI() {
        binding.songHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songListAdapter
        }

        binding.refreshLayout.setOnRefreshListener {
            loadHistory()
        }

        binding.btnActionSearch.setOnClickListener {
            searchLyric()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> Log.d(TAG, "Loading...")
                is UIState.Success -> loadSongs(it.data)
                is UIState.Failure -> handleErrors(it.exception)
                else -> Log.d(TAG, "Initial State")
            }
        }

        loadHistory()
    }

    private fun searchLyric() {
        findNavController().navigate(HistoryFragmentDirections.actionHistoryToSearchLyric())
    }

    private fun loadHistory() {
        lifecycleScope.runInBackground {
            presenter.search()
        }
    }

    private fun loadSongs(songs: List<Song>) {
        binding.apply {
            refreshLayout.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        }
        songListAdapter.submitList(songs)
    }

    private fun handleErrors(error: Throwable) {
        when (error) {
            is EmptyRecordList -> binding.apply {
                refreshLayout.visibility = View.GONE
                emptyState.visibility = View.VISIBLE
            }
        }
    }
}
