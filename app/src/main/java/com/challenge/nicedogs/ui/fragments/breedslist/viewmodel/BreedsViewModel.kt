package com.challenge.nicedogs.ui.fragments.breedslist.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.nicedogs.data.api.repositories.Resource
import com.challenge.nicedogs.domain.usecases.UseCaseGetBreedsByPage
import com.challenge.nicedogs.ui.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class BreedsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val breedsByPageUseCase: UseCaseGetBreedsByPage
) : ViewModel(), BreedsViewModelContract.ViewModel {

    private var page = 0
    private var totalBreeds = 0

    override fun invokeAction(action: BreedsViewModelContract.Action) {
        when (action) {
            is BreedsViewModelContract.Action.GetBreeds -> getBreeds(action.lastCompletelyVisibleItemPosition)
            is BreedsViewModelContract.Action.NavigateToBreedDetails -> _breedsEvents.postValue(BreedsViewModelContract.Event.NavigationToBreedDetail(action.id))
        }
    }

    private val _breedsStates = MediatorLiveData<BreedsViewModelContract.State>()
    override val breedsState = _breedsStates

    private val _breedsEvents = SingleLiveEvent<BreedsViewModelContract.Event>()
    override val breedsEvent = _breedsEvents

    private fun getBreeds(lastCompletelyVisibleItemPosition: Int = 0) {
        viewModelScope.launch(dispatcher) {
            if (shouldFetchNextPage(lastCompletelyVisibleItemPosition)) {
                breedsByPageUseCase(page).collect { resource ->
                    resource.totalBreeds?.also { totalBreeds = it }

                    if (page > 0 && (resource is Resource.Success || resource is Resource.Error))
                        _breedsEvents.postValue(BreedsViewModelContract.Event.PaginationLoading(false))

                    when (resource) {
                        is Resource.Loading ->
                            if (page == 0) _breedsStates.postValue(BreedsViewModelContract.State.LoadingState)
                            else _breedsEvents.postValue(BreedsViewModelContract.Event.PaginationLoading(true))

                        is Resource.Error ->
                            if (page == 0) _breedsStates.postValue(BreedsViewModelContract.State.ErrorState(resource.message))
                            else _breedsEvents.postValue(BreedsViewModelContract.Event.PaginationError(resource.message))

                        is Resource.Success ->
                            if (page == 0 && resource.data.isNullOrEmpty()) _breedsStates.postValue(BreedsViewModelContract.State.EmptyState)
                            else _breedsStates.postValue(BreedsViewModelContract.State.SuccessState(resource.data))
                    }

                    if (resource is Resource.Success)
                        page++
                }
            }
        }
    }

    private fun shouldFetchNextPage(lastCompletelyVisibleItemPosition: Int) = lastCompletelyVisibleItemPosition >= totalBreeds
}