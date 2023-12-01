package com.challenge.nicedogs.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.challenge.nicedogs.domain.repository.BreedsDomainRepository
import com.challenge.nicedogs.domain.usecases.UseCaseGetBreedsByPage
import com.challenge.nicedogs.domain.usecases.UseCaseSearchBreeds
import com.challenge.nicedogs.ui.fragments.breedslist.viewmodel.BreedsViewModel
import com.challenge.nicedogs.ui.fragments.breedssearch.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers

class AppViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(BreedsViewModel::class.java) ->
                    BreedsViewModel(
                        Dispatchers.IO,
                        UseCaseGetBreedsByPage(
                            BreedsDomainRepository.getInstance(context)
                        )
                    )

                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(
                        Dispatchers.IO,
                        UseCaseSearchBreeds(
                            BreedsDomainRepository.getInstance(context)
                        )
                    )

                else -> throw IllegalArgumentException("IllegalArgumentException in this view model class: ${modelClass.name}")
            }
        } as T

    companion object {
        private var INSTANCE: ViewModelProvider.NewInstanceFactory? = null

        operator fun invoke(context: Context) =
            INSTANCE ?: synchronized(AppViewModelFactory::class.java) {
                INSTANCE ?: AppViewModelFactory(context).also {
                    INSTANCE = it
                }
            }
    }
}
