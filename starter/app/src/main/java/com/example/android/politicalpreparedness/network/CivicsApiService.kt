package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.DateAdapter
import com.example.android.politicalpreparedness.utils.Constants
import com.example.android.politicalpreparedness.network.jsonadapter.DevisionAdapter
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
// The adapter can help us to convert the json object data to the data we want.
private val moshi = Moshi.Builder()
    .add(DevisionAdapter())
    .add(DateAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    // Add elections API Cal
    @GET(Constants.ELECTIONS_QUERY_PATH)
    suspend fun getElectionsListAsync(): ElectionResponse

    // Add voter info API Call
    @GET(Constants.VOTER_INFO_QUERY_PATH)
    suspend fun getVoterInfoListAsync(
        @Query("address") address: String,
        @Query("electionId") electionId: Long
    ): VoterInfoResponse

    // Add representatives API Call
    @GET(Constants.REPRESENTATIVE_PATH)
    suspend fun getRepresentativesByAddressAsync(
        @Query("address") address: String
    ): RepresentativeResponse

}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}