package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository

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

    // Create live data val for upcoming elections
    private val _upcomingElections =  MutableLiveData<List<Election>>()
    val upcomingElection: LiveData<List<Election>>
        get() = _upcomingElections

    // Create live data val for saved elections
    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections


//------------------------------------- Initialization ---------------------------------------------


    /**
     * Init Block: To get data from Internet.
     * */
    init {
        getAppDataProperty()
    }
    val responseAsteroidList = electionsRepo.elections


//------------------------------------- Data Retrieve Functions ------------------------------------




//------------------------------------- Event Trigger Function -------------------------------------


    /**
     * Function will be called when list item has been clicked.
     * */
    fun onAsteroidClicked(election: Election) {
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