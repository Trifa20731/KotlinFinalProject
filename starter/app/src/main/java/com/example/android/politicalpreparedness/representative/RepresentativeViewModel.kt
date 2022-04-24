package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.isNetworkAvailable
import kotlinx.coroutines.launch
import java.lang.Exception

class RepresentativeViewModel(
    private val application: Application,
    private val dataSource: ElectionDatabase
): ViewModel() {

    companion object {
        const val LOG_TAG: String = "RepresentativeViewModel"
    }

    private val electionRepo: ElectionsRepository = ElectionsRepository(dataSource)

    // Live data for representatives and address
    private val _representativesList = MutableLiveData<List<Representative>>()
    val representativeList: LiveData<List<Representative>>
        get() = _representativesList

//------------------------------------- Data Retrieve Functions ------------------------------------


    fun retrieveRepresentativeListByAddress(address: Address) {
        val addressString = address.toFormattedString()
        val fakeAddress: String = "us la"
        Log.d(LOG_TAG, "retrieveRepresentativeListByAddress: run.")
        if (isNetworkAvailable(application)) {
            viewModelScope.launch {
                try {
                    Log.d(LOG_TAG, "Fetching Representative Data.")
                    CivicsApi.retrofitService.getRepresentativesByAddressAsync(fakeAddress)
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Fetching Fail.")
                    Log.e(LOG_TAG, e.message!!)
                }
            }
        } else {
            Log.w(LOG_TAG, "No Network Connection.")
        }
    }


    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
