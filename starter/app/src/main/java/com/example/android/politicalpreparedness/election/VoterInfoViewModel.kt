package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection
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
    lateinit var followedElectionList: LiveData<List<FollowedElection>>
    private val electionsRepo: ElectionsRepository = ElectionsRepository(dataSource)

    // Add live data to hold voter info
    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse


//--------------------------------------------------------------------------------------------------


    fun getElectionInfo(electionId: Int) {
        election = dataSource.electionDao.selectElection(electionId)
        followedElectionList = electionsRepo.followedElections
    }


//--------------------------------------------------------------------------------------------------


    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    // Add var and methods to save and remove elections to local database
    fun insertFollowElection(electionId: Int) {
        val followedElection = FollowedElection(electionId)
        viewModelScope.launch {
            try {
                Log.d(LOG_TAG, "Insert the followed election the id is ${followedElection.id}")
                electionsRepo.insertFollowedElection(electionId)
                checkFollowedElectionList()
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Get error message in insert followed election.")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }

    private fun checkFollowedElectionList() {
        followedElectionList.value?.let {
            for (election in it) {
                Log.d(LOG_TAG, "The election id is ${election.id}")
            }
        }
    }

    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status


}