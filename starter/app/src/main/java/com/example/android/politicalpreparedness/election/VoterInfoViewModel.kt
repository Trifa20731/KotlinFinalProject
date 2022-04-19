package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionId
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val application: Application,
    private val dataSource: ElectionDatabase
    ) : ViewModel() {

    companion object {
        const val LOG_TAG: String = "VoterInfoViewModel"
    }

    lateinit var election: LiveData<Election>
    private val electionsRepo: ElectionsRepository = ElectionsRepository(dataSource)

    // Add live data to hold voter info
    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse


//--------------------------------------------------------------------------------------------------


    fun getElectionInfo(electionId: Int) {
        election = dataSource.electionDao.selectElectionBySingleId(electionId)
    }


//--------------------------------------------------------------------------------------------------


    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    // Add var and methods to save and remove elections to local database
    fun insertElectionId(electionId: Int) {
        val toBeInsertedElectionId = ElectionId(electionId)
        viewModelScope.launch {
            try {
                Log.d(LOG_TAG, "Insert the electionId the id is ${toBeInsertedElectionId.id}")
                electionsRepo.insertElectionId(electionId)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Get error message in insert electionId.")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }


    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status


}