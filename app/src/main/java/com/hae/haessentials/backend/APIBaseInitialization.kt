package com.hae.haessentials.backend


import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object APIBaseInitialization {

    @JvmStatic
    internal fun initApi(retrofit: Retrofit):APICalls{
        return retrofit.create(APICalls::class.java)
    }

    @JvmStatic
    internal fun initRetrofit():Retrofit{
        val logApiCalls: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val headersApi = Interceptor {
            val request = it.request().newBuilder()
                .addHeader("type","ANDROID")
                .build()
            it.proceed(request)
        }

        val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        clientBuilder.readTimeout(180, TimeUnit.SECONDS)
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        clientBuilder.addInterceptor(logApiCalls)
        clientBuilder.addInterceptor(headersApi)

        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(clientBuilder.build())
            .build()
    }
}