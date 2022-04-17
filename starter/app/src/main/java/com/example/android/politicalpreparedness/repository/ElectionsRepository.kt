package com.example.android.politicalpreparedness.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * The repository hides the complexity of managing the interactions between the database and the
 * networking code.
 * */
class ElectionsRepository(private val database: ElectionDatabase) {

    companion object {
        const val LOG_TAG: String = "ElectionsRepository"
    }

    val elections: LiveData<List<Election>> = database.electionDao.selectElections()
    val followedElections: LiveData<List<FollowedElection>> = database.followedElectionDao.selectFollowedElections()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            try {
                Log.d(LOG_TAG, "refreshElection, try to get the election from Internet.")
                val tmpResponse = CivicsApi.retrofitService.getElectionsListAsync()
                database.electionDao.insertAllElections(*(tmpResponse.elections).toTypedArray())
            } catch (e: Exception) {
                Log.e(LOG_TAG, "refreshElection, get error message")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }

    suspend fun insertFollowedElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(LOG_TAG, "insertFollowedElection, try to insert followed election.")
                val followedElection = FollowedElection(electionId)
                database.followedElectionDao.insertFollowedElection(followedElection)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "insertFollowedElection, get error message")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }
}