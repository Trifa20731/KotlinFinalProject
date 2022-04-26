package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.isNetworkAvailable
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    private val application: Application,
    private val dataSource: ElectionDatabase
): ViewModel() {

    companion object {
        const val LOG_TAG: String = "RepresentativeViewModel"
    }

    private val electionRepo: ElectionsRepository = ElectionsRepository(dataSource)

    // Live data for representatives and address
    private val _representativesShowingList = MutableLiveData<List<Representative>>()
    val representativeShowingList: LiveData<List<Representative>>
        get() = _representativesShowingList

    private val _representativeResponse = MutableLiveData<RepresentativeResponse>()
    val representativeResponse: LiveData<RepresentativeResponse>
        get() = _representativeResponse

//------------------------------------- Data Retrieve Functions ------------------------------------


    /** The method fetch representatives from API from a provided address */
    fun retrieveRepresentativeListByAddress(address: Address) {
        val addressString = address.toFormattedString()
        val fakeAddress: String = "us la"
        Log.d(LOG_TAG, "retrieveRepresentativeListByAddress: run.")
        if (isNetworkAvailable(application)) {
            viewModelScope.launch {
                electionRepo.refreshRepresentative(fakeAddress)?.let {
                    _representativeResponse.postValue(it)
                }
            }
        } else {
            Log.w(LOG_TAG, "No Network Connection.")
        }
    }

    /** The method update the list after get the Internet Representative Response. */
    fun updateRepresentativeList(response: RepresentativeResponse) {
        Log.d(LOG_TAG, "updateRepresentativeList: Run.")
        val representativeList = arrayListOf<Representative>()
        val officialList = response.officials
        val officeList = response.offices
        officeList.forEach {
            representativeList.addAll(it.getRepresentatives(officialList))
        }
        representativeList.forEach{
            Log.d(LOG_TAG, "The office ${it.office.name} has official ${it.official.name}")
        }
    }



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
