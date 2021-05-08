package com.patagonian.lyrics.presentation.search.lyric

import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.NetworkException.NetworkNotAvailable
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.RecordNotFound
import com.patagonian.lyrics.domain.exception.DomainException.ValidationException.BlankField
import com.patagonian.lyrics.domain.interactor.search.lyric.SearchLyric
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class PresenterTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    val view: View = mockk()
    val viewModel: ViewModel = mockk(relaxed = true)
    val useCase: SearchLyric = mockk()
    val presenter = Presenter(view, viewModel, useCase)

    given("a song title") {
        val title = "song title"

        and("an author name") {
            val author = "author name"

            and("the network connectivity is available") {
                every { view.isNetworkConnectivityAvailable() } returns true
                every { viewModel.songTitle.value } returns title
                every { viewModel.artistName.value } returns author
                justRun { viewModel.setAction(ViewModel.Action.SEARCH_SONG) }
                coJustRun { useCase.execute(title, author, presenter) }

                `when`("searching is performed") {
                    presenter.search()

                    then("the use case should be called") {
                        verify(exactly = 1) { view.isNetworkConnectivityAvailable() }
                        verify(exactly = 1) { viewModel.setAction(ViewModel.Action.SEARCH_SONG) }
                        coVerify(exactly = 1) { useCase.execute(title, author, presenter) }
                    }
                }
            }

            and("the network connectivity is not available") {
                val error = NetworkNotAvailable
                every { view.isNetworkConnectivityAvailable() } returns false
                justRun { viewModel.failure(error) }

                `when`("searching is performed") {
                    presenter.search()

                    then("an error should be displayed") {
                        verify(exactly = 1) { view.isNetworkConnectivityAvailable() }
                        verify(exactly = 1) { viewModel.failure(error) }
                    }
                }
            }

            and("a successful response") {
                val result = Song(title = title, author = author, lyric = "lyric")
                justRun { viewModel.success(result) }

                `when`("the success method is called") {
                    presenter.success(result)

                    then("the lyric should be displayed") {
                        verify(exactly = 1) { viewModel.success(result) }
                    }
                }
            }
        }
    }

    given("a validation error for a blank song title param") {
        val error = "Mandatory field"
        val response = BlankField(Song::title.name)
        every { view.mandatoryFieldError } returns error
        justRun { viewModel.setFieldError(response.field, error) }

        `when`("the failure method is called") {
            presenter.failure(response)

            then("the title error should be updated") {
                verify(exactly = 1) { viewModel.setFieldError(response.field, error) }
            }
        }
    }

    given("a validation error for a blank author name param") {
        val error = "Mandatory field"
        val response = BlankField(Song::author.name)
        every { view.mandatoryFieldError } returns error
        justRun { viewModel.failure(response) }
        justRun { viewModel.setFieldError(response.field, error) }

        `when`("the failure method is called") {
            presenter.failure(response)

            then("the author name error should be updated") {
                verify(exactly = 1) { viewModel.setFieldError(response.field, error) }
            }
        }
    }

    given("given a record not found transaction error") {
        val response = RecordNotFound
        justRun { viewModel.failure(response) }

        `when`("the failure method is called") {
            presenter.failure(response)

            then("the song not found error should be displayed") {
                verify(exactly = 1) { viewModel.failure(response) }
            }
        }
    }
})