package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionId
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

/**
 *
 *
 * Note:
 *     -- Election State:
 *         -- 0: Unsaved Election.
 *         -- 1: Saved Election.
 * */
class VoterInfoViewModel(
    private val application: Application,
    private val dataSource: ElectionDatabase
    ) : ViewModel() {

    companion object {
        const val LOG_TAG: String = "VoterInfoViewModel"
        const val ELECTION_STATE_UNSAVED: Int = 0
        const val ELECTION_STATE_SAVED: Int = 1
    }

    lateinit var election: LiveData<Election>
    private val electionsRepo: ElectionsRepository = ElectionsRepository(dataSource)

    // Add live data to hold voter info
    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse


    private val _electionState = MutableLiveData<Int>()
    val electionState: LiveData<Int>
        get() = _electionState


//--------------------------------------------------------------------------------------------------


    fun getElectionInfo(electionId: Int, division: Division, state: Int) {
        election = dataSource.electionDao.selectElectionBySingleId(electionId)
        Log.d(LOG_TAG, "The division is ${division.country}, ${division.state}")
        _electionState.value = state
    }


//--------------------------------------------------------------------------------------------------


    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    // Add var and methods to save and remove elections to local database
    fun insertOrDeleteSaveElection(electionId: Int) {
        when (_electionState.value) {
            ELECTION_STATE_UNSAVED -> {
                insertSavedElection(electionId)
                _electionState.value = ELECTION_STATE_SAVED
            }
            ELECTION_STATE_SAVED -> {
                deleteSavedElection(electionId)
                _electionState.value = ELECTION_STATE_UNSAVED
            }
        }
    }


    private fun insertSavedElection(electionId:Int) {
        val toBeInsertedElectionId = ElectionId(electionId)
        viewModelScope.launch {
            try {
                Log.d(LOG_TAG, "Insert the electionId the id is ${toBeInsertedElectionId.id}")
                electionsRepo.insertSavedElection(electionId)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Get error message in insert electionId.")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }

    private fun deleteSavedElection(electionId: Int) {
        viewModelScope.launch {
            try {
                Log.d(LOG_TAG, "Delete the electionId the id is ${electionId}")
                electionsRepo.deleteSavedElection(electionId)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Get error message in delete electionId.")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }

}