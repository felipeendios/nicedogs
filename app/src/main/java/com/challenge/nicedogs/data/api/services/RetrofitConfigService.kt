package com.challenge.nicedogs.data.api.services

import com.challenge.nicedogs.data.api.retrofit.APIConstants.API_KEY
import com.challenge.nicedogs.data.api.retrofit.APIConstants.BASE_URL
import com.challenge.nicedogs.data.api.retrofit.APIEndpoints
import com.github.simonpercic.oklog3.OkLogInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfigService {

    fun getConfigService(): APIEndpoints = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(APIEndpoints::class.java)

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createHeaderInterceptor())
            .addInterceptor(createLoggingInterceptor())
            .addInterceptor(createOkLogInterceptor())
            .build()
    }

    private fun createHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
                .addHeader("x-api-key", API_KEY)
            chain.proceed(builder.build())
        }
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun createOkLogInterceptor(): Interceptor {
        return OkLogInterceptor.builder()
            .withProtocol(true)
            .withRequestContentType(true)
            .withRequestHeaders(true)
            .withResponseUrl(true)
            .withResponseHeaders(true)
            .shortenInfoUrl(true)
            .build()
    }
}
