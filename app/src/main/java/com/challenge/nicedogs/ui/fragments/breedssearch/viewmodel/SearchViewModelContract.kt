package com.challenge.nicedogs.ui.fragments.breedssearch.viewmodel

import androidx.lifecycle.LiveData
import com.challenge.nicedogs.domain.models.BreedDomainModel

class SearchViewModelContract {

    interface ViewModel {
        val searchState: LiveData<State>
        val searchEvent: LiveData<Event>
        fun invokeAction(action: Action)
    }

    sealed class Action {
        data class SearchBreeds(val query: String) : Action()
        data class NavigateToBreedDetails(val id: Int) : Action()
    }

    sealed class State {
        object LoadingState : State()
        object EmptyState : State()
        data class ErrorState(val message: String?) : State()
        data class SuccessState(val breeds: List<BreedDomainModel>?) : State()
    }

    sealed class Event {
        data class GoToBreedDetail(val id: Int) : Event()
    }
}