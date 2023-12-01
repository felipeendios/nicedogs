package com.challenge.nicedogs.domain.repository

import android.content.Context
import com.challenge.nicedogs.data.api.models.BreedModel
import com.challenge.nicedogs.data.api.repositories.BreedsRepository
import com.challenge.nicedogs.data.api.repositories.Resource
import com.challenge.nicedogs.data.api.services.RetrofitConfigService
import com.challenge.nicedogs.data.database.NiceDogsDatabase
import com.challenge.nicedogs.domain.asBreedDTOList
import com.challenge.nicedogs.domain.asBreedDomainModel
import com.challenge.nicedogs.domain.asListBreedDomainModel
import com.challenge.nicedogs.domain.models.BreedDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class BreedsDomainRepository(
    private val niceDogsDatabase: NiceDogsDatabase,
    private val breedsRemoteDataSource: BreedsRepository
) : BreedsDomainRepositoryContract {

    private suspend fun FlowCollector<Resource<List<BreedDomainModel>>>.emitResourceFromRemote(
        resource: Resource<List<BreedModel>>,
        page: Int = 0,
        query: String? = null
    ) {
        if (resource.data.isNullOrEmpty()) {
            niceDogsDatabase.breedDAO().insertBreeds(resource.data!!.asBreedDTOList(page))
        }
        if (resource.data.asBreedDTOList(page).isEmpty() && !resource.message.isNullOrEmpty()) {
            emit(Resource.Error(resource.message))
        } else {
            emit(
                Resource.Success(
                    data = resource.data.asBreedDTOList(page).asListBreedDomainModel(),
                    totalBreeds = resource.totalBreeds
                )
            )
        }
    }

    override suspend fun getBreedsByPage(page: Int): Flow<Resource<List<BreedDomainModel>>> =
        flow {
            emit(Resource.Loading())

            val resource = breedsRemoteDataSource.getBreeds(page)
            emitResourceFromRemote(resource, page)
        }

    override suspend fun getBreedsBySearchQuery(query: String): Flow<Resource<List<BreedDomainModel>>> =
        flow {
            emit(Resource.Loading())

            val resource = breedsRemoteDataSource.searchBreeds(query)
            emitResourceFromRemote(resource, 0, query)
        }

    override suspend fun getBreedByID(id: Int): BreedDomainModel =
        niceDogsDatabase.breedDAO().getBreedById(id).asBreedDomainModel()

    companion object {
        private var INSTANCE: BreedsDomainRepository? = null

        fun getInstance(context: Context): BreedsDomainRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BreedsDomainRepository(
                    NiceDogsDatabase.getInstance(context),
                    BreedsRepository(RetrofitConfigService.getConfigService())
                ).also { INSTANCE = it }
            }
    }
}
