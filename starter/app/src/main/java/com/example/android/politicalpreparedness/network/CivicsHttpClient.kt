package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class CivicsHttpClient: OkHttpClient() {

    companion object {

        // Place your API Key Here.
        const val API_KEY = "AIzaSyCKszBAAQdCHa3dYU0Iswb2UESTdMMPB4c"

        fun getClient(): OkHttpClient {
            return Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original
                        .url()
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()
                        chain.proceed(request)
                    }
                    .build()
        }

    }

}