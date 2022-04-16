package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val application: Application,
    private val dataSource: ElectionDatabase
    ) : ViewModel() {

    companion object {
        const val LOG_TAG: String = "VoterInfoViewModel"
    }

    lateinit var election: LiveData<Election>

    // Add live data to hold voter info
    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse




//--------------------------------------------------------------------------------------------------


    fun getElectionInfo(electionId: Int) {
        election = dataSource.electionDao.selectElection(electionId)
    }


//--------------------------------------------------------------------------------------------------


    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    // Add var and methods to save and remove elections to local database
    fun followElection(electionId: Int) {
        val followedElection = FollowedElection(electionId)
        viewModelScope.launch {
            dataSource.followedElectionDao.insertFollowedElection(followedElection)
        }
    }

    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status


}