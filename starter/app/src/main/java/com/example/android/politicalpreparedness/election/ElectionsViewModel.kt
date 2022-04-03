package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election

// Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val application: Application,
    private val dataSource: ElectionDatabase
) : ViewModel() {

    companion object {
        const val LOG_TAG: String = "ElectionsViewModel"
    }

    // Create live data val for upcoming elections
    private val _upcomingElections =  MutableLiveData<List<Election>>()
    val upcomingElection: LiveData<List<Election>>
        get() = _upcomingElections

    // Create live data val for saved elections
    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}