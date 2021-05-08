package com.patagonian.lyrics.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.patagonian.lyrics.R
import com.patagonian.lyrics.databinding.FragmentSearchLyricBinding
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.NetworkException.NetworkNotAvailable
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.RecordNotFound
import com.patagonian.lyrics.domain.interactor.search.lyric.SearchLyric
import com.patagonian.lyrics.presentation.search.lyric.ViewModel
import com.patagonian.lyrics.ui.UIState
import com.patagonian.lyrics.ui.util.hideKeyboard
import com.patagonian.lyrics.ui.util.isNetworkConnectivityAvailable
import com.patagonian.lyrics.ui.util.runInBackground
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import com.patagonian.lyrics.presentation.search.lyric.View as SearchLyricView

/**
 * A simple [Fragment] subclass that allows searching a lyric
 * based on the author's name and song title.
 */
class SearchLyricFragment : Fragment(), SearchLyricView {

    companion object {
        private val TAG = SearchLyricFragment::class.java.canonicalName
    }

    private val view: SearchLyricView = this
    private val viewModel: ViewModel by inject()
    private val presenter: SearchLyric.Presenter by inject { parametersOf(view, viewModel) }
    private lateinit var binding: FragmentSearchLyricBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchLyricBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        initUI()
        return binding.root
    }

    override val mandatoryFieldError: String
        get() = getString(R.string.error_mandatory_field)

    override fun isNetworkConnectivityAvailable(): Boolean =
        requireContext().isNetworkConnectivityAvailable()

    private fun initUI() {
        binding.btnSearch.setOnClickListener {
            requireActivity().hideKeyboard()
            binding.root.requestFocus()
            lifecycleScope.runInBackground {
                presenter.search()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> Log.d(TAG, "Loading...")
                is UIState.Success -> displayLyric(it.data)
                is UIState.Failure -> handleErrors(it.exception)
                else -> Log.d(TAG, "Initial state")
            }
        }
    }

    private fun displayLyric(song: Song) {
        findNavController().navigate(SearchLyricFragmentDirections.actionSearchLyricToLyric(song))
        presenter.reset()
    }

    private fun handleErrors(error: Throwable) {
        when (error) {
            is NetworkNotAvailable -> handleNetworkNotAvailable()
            is RecordNotFound -> handleSongNotFound()
        }
    }

    private fun handleSongNotFound() {
        binding.showErrorMessage(R.string.error_song_not_found)
    }

    private fun handleNetworkNotAvailable() {
        binding.showErrorMessage(R.string.error_network_not_available)
    }

    private fun FragmentSearchLyricBinding.showErrorMessage(@StringRes resId: Int) {
        Snackbar.make(root, resId, Snackbar.LENGTH_LONG).show()
    }
}
