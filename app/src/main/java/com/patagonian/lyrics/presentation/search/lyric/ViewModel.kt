package com.patagonian.lyrics.presentation.search.lyric

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.ui.UIState

class ViewModel : androidx.lifecycle.ViewModel() {

    private val _state = MutableLiveData<UIState<Song>>(UIState.Initial)
    val state: LiveData<UIState<Song>> = _state

    val isLoading: LiveData<Boolean> = Transformations.map(state) { it is UIState.Loading }

    val songTitle = MutableLiveData("")
    private val _songTitleError = MutableLiveData<String?>()
    val songTitleError: LiveData<String?> = _songTitleError

    val artistName = MutableLiveData("")
    private val _artistNameError = MutableLiveData<String?>()
    val artistNameError: LiveData<String?> = _artistNameError

    private val action = MutableLiveData(Action.NONE)

    enum class Action {
        NONE,
        SEARCH_SONG
    }

    fun reset() {
        setAction(Action.NONE)
        songTitle.postValue("")
        artistName.postValue("")
        clearFieldErrors()
    }

    private fun clearFieldErrors() {
        setFieldError(Song::title.name, null)
        setFieldError(Song::author.name, null)
    }

    fun setFieldError(field: String, error: String?) {
        when (field) {
            Song::title.name -> _songTitleError.postValue(error)
            Song::author.name -> _artistNameError.postValue(error)
        }
        setAction(Action.NONE)
    }

    fun setAction(action: Action) {
        this.action.postValue(action)
        when (action) {
            Action.NONE -> _state.postValue(UIState.Initial)
            Action.SEARCH_SONG -> {
                clearFieldErrors()
                _state.postValue(UIState.Loading)
            }
        }
    }

    fun success(song: Song) {
        _state.postValue(UIState.Success(song))
    }

    fun failure(error: Throwable) {
        _state.postValue(UIState.Failure(error))
    }
}