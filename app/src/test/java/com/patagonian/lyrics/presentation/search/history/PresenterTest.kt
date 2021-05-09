package com.patagonian.lyrics.presentation.search.history

import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.EmptyRecordList
import com.patagonian.lyrics.domain.interactor.search.history.SearchSongHistory
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coJustRun
import io.mockk.coVerifyOrder
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class PresenterTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val viewModel: ViewModel = mockk()
    val useCase: SearchSongHistory = mockk()
    val presenter = Presenter(viewModel, useCase)

    describe("${Presenter::class.java.canonicalName}") {
        it("search() should call the use case") {
            justRun { viewModel.setAction(ViewModel.Action.SEARCH_HISTORY) }
            coJustRun { useCase.execute(presenter) }

            presenter.search()

            coVerifyOrder {
                viewModel.setAction(ViewModel.Action.SEARCH_HISTORY)
                useCase.execute(presenter)
            }
        }

        context("with a successful result") {
            val song = Song(id = 1, title = "song title", author = "author name", lyric = "lyric")
            val result = listOf(song)

            it("success() should update the viewModel data") {
                justRun { viewModel.success(result) }

                presenter.success(result)

                verify { viewModel.success(result) }
            }
        }

        context("with a failure result") {
            val result = EmptyRecordList

            it("failure() should update the viewModel data") {
                justRun { viewModel.failure(result) }

                presenter.failure(result)

                verify { viewModel.failure(result) }
            }
        }
    }
})