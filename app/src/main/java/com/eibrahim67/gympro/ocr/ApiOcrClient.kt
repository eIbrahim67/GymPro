package com.eibrahim67.gympro.ocr

import com.eibrahim67.gympro.core.utils.VALS
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiOcrClient {
    private const val BASE_URL = "http://"+ VALS.IP_CONFIG + ":3000/"

    val ocrApiService: OcrApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        retrofit.create(OcrApiService::class.java)
    }
}