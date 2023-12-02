package com.challenge.nicedogs.ui.fragments.breedslist.viewmodel

import androidx.lifecycle.LiveData
import com.challenge.nicedogs.domain.models.BreedDomainModel

sealed class BreedsViewModelContract {

    interface ViewModel {
        val breedsState: LiveData<State>
        val breedsEvent: LiveData<Event>
        fun invokeAction(action: Action)
    }

    sealed class Action {
        data class GetBreeds(val lastCompletelyVisibleItemPosition: Int = 0) : Action()
        data class NavigateToBreedDetails(val id: Int) : Action()
        object ChangeViewStyle : Action()
        object ChangeAlphabeticalOrder : Action()
    }

    sealed class State {
        object LoadingState : State()
        object EmptyState : State()
        data class ErrorState(val message: String?) : State()
        data class SuccessState(val breeds: List<BreedDomainModel>?) : State()
    }

    sealed class Event {
        data class PaginationLoading(val isVisible: Boolean) : Event()
        data class PaginationError(val message: String?) : Event()
        data class NavigationToBreedDetail(val id: Int) : Event()
        data class ChangeViewStyle(val isListView: Boolean) : Event()
        data class ChangeAlphabeticalOrder(val isAZSort: Boolean) : Event()
    }
}
