package com.patagonian.lyrics.presentation.search.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.ui.UIState

class ViewModel : androidx.lifecycle.ViewModel() {

    private val _state = MutableLiveData<UIState<List<Song>>>(UIState.Initial)
    val state: LiveData<UIState<List<Song>>> = _state

    val isLoading: LiveData<Boolean> = Transformations.map(state) { it is UIState.Loading }

    private val action = MutableLiveData(Action.NONE)

    enum class Action {
        NONE,
        SEARCH_HISTORY
    }

    fun reset() {
        setAction(Action.NONE)
    }

    fun setAction(action: Action) {
        this.action.postValue(action)
        when (action) {
            Action.NONE -> _state.postValue(UIState.Initial)
            Action.SEARCH_HISTORY -> _state.postValue(UIState.Loading)
        }
    }

    fun success(songs: List<Song>) {
        _state.postValue(UIState.Success(songs))
    }

    fun failure(error: Throwable) {
        _state.postValue(UIState.Failure(error))
    }
}