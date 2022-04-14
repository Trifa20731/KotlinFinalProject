package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.isNetworkAvailable
import kotlinx.coroutines.launch
import java.lang.Exception

class RepresentativeViewModel(
    private val application: Application
): ViewModel() {

    companion object {
        const val LOG_TAG: String = "RepresentativeViewModel"
    }

    // Live data for representatives and address
    private val _representativesList = MutableLiveData<List<Representative>>()
    val representativeList: LiveData<List<Representative>>
        get() = _representativesList
    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address


//------------------------------------- Initialization ---------------------------------------------


    /**
     * Init Block: To get data from Internet.
     * */
    init {
        // getAppDataProperty()
    }
    // val responseRepresentativeList = representativeRepo.representatives


//------------------------------------- Data Retrieve Functions ------------------------------------


    private fun getAppDataProperty() {
        Log.d(LOG_TAG, "getAppDataProperty: run.")
        if (isNetworkAvailable(application)) {
            viewModelScope.launch {
                try {
                    Log.d(LOG_TAG, "Fetching Representative Data.")
                } catch (e: Exception) {
                    Log.d(LOG_TAG, "Fetching Fail.")
                }
            }
        } else {

        }
    }

    fun refreshListData() {

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
