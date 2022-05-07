package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.*
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

    private val _electionState = MutableLiveData<Int>()
    val electionState: LiveData<Int>
        get() = _electionState

    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse?>()
    val voterInfoResponse: LiveData<VoterInfoResponse?>
        get() = _voterInfoResponse

    private val _voterInfoState = MutableLiveData<State>()
    val voterInfoState: LiveData<State>
        get() = _voterInfoState

//--------------------------------------------------------------------------------------------------


    fun getElectionInfo(electionId: Int, division: Division, state: Int) {

        election = dataSource.electionDao.selectElectionBySingleId(electionId)
        _electionState.value = state
        val addressString = "${division.country} ${division.state}"
        getVoterInfo(addressString, electionId.toLong())

    }

    private fun getVoterInfo(address: String, electionId: Long) {

        viewModelScope.launch {
            electionsRepo.refreshVoterInfo(address, electionId)?.let {
                _voterInfoResponse.postValue(it)
            }?:let {
                Log.w(LOG_TAG, "return null voter response from Internet.")
            }
        }

    }

    fun updateState(voterInfo: VoterInfoResponse) {

        voterInfo.state?.let {
            if (it.isNotEmpty()) {
                val tmpVoterInfoState = it[0]
                if (
                    tmpVoterInfoState.electionAdministrationBody.ballotInfoUrl!=null &&
                    tmpVoterInfoState.electionAdministrationBody.votingLocationFinderUrl!=null
                ) {
                    _voterInfoState.value = it[0]
                } else {
                    Log.w(LOG_TAG, "Null Url String in Voter Info State")
                }
            } else {
                Log.w(LOG_TAG, "Empty state list.")
            }
        }?:let {
            Log.w(LOG_TAG, "return null voter state from Internet.")
        }

    }


//--------------------------------------------------------------------------------------------------


    /** The method executes after clicking the save toggle button. */
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

    /** The method save the election into data base. */
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

    /** The method delete the saved election in the election id table. */
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