package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.utils.isNetworkAvailable
import kotlinx.coroutines.launch

// Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val application: Application,
    private val dataSource: ElectionDatabase
) : ViewModel() {

    companion object {
        const val LOG_TAG: String = "ElectionsViewModel"
    }

    private val electionsRepo: ElectionsRepository = ElectionsRepository(dataSource)

    // The navigation action to detail page.
    private val _navigateToDetail = MutableLiveData<Election?>()
    val navigateToDetail: LiveData<Election?>
        get() = _navigateToDetail

    private val _upcomingElectionShowingList = MutableLiveData<List<Election>>()
    val upcomingElectionsShowingList: LiveData<List<Election>>
        get() = _upcomingElectionShowingList

    private val _savedElectionShowingList = MutableLiveData<List<Election>>()
    val savedElectionShowingList: LiveData<List<Election>>
        get() = _savedElectionShowingList

    private val _stateInfoShowing = MutableLiveData<String>()
    val stateInfoShowing: LiveData<String>
        get() = _stateInfoShowing


//------------------------------------- Initialization ---------------------------------------------


    /**
     * Init Block: To get data from Internet.
     * */
    init {
        getElectionList()
    }
    val rawElectionsResponseList = electionsRepo.elections
    val savedElectionResponseList = electionsRepo.savedElections
    val unsavedElectionResponseList = electionsRepo.unsavedElections


//------------------------------------- Data Retrieve Functions ------------------------------------


    private fun getElectionList() {
        Log.d(LOG_TAG, "getElectionList: run.")
        if (isNetworkAvailable(application)) {
            viewModelScope.launch {
                try {
                    Log.d(LOG_TAG, "Fetching Internet Data.")
                    _stateInfoShowing.value = "Fetching Internet Data......"
                    electionsRepo.refreshElections()
                } catch (e: Exception) {
                    Log.d(LOG_TAG, "Fetching Fail.")
                    _stateInfoShowing.value = "Error Happen In Fetching Internet Data."
                }
            }
        } else {
            _stateInfoShowing.value = "No Internet Connection."
        }
    }

    /**
     * The function to refresh the data will be shown in the UI list.
     * */
    fun refreshElectionListData() {
        Log.d(LOG_TAG, "refreshElectionListData, run.")
        _upcomingElectionShowingList.value = rawElectionsResponseList.value
    }

    fun refreshUpcomingElectionListData() {
        Log.d(LOG_TAG, "refreshUpcomingElectionListData, run")
        _upcomingElectionShowingList.value = unsavedElectionResponseList.value
    }

    fun refreshSavedElectionListData() {
        Log.d(LOG_TAG, "refreshSavedElectionListData, run.")
        _savedElectionShowingList.value = savedElectionResponseList.value
    }


//------------------------------------- Event Trigger Function -------------------------------------

    
    /**
     * Function will be called when list item has been clicked.
     * */
    fun onElectionClicked(election: Election) {
        _navigateToDetail.value = election
    }

    /**
     * Reset the parameter after navigation.
     * */
    fun doneNavigation() {
        _navigateToDetail.value = null
    }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}