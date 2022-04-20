package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionId
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
    val electionIds: LiveData<List<Election>> = database.electionIdDao.selectElectionIds()

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

    suspend fun insertElectionId(electionId: Int) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(LOG_TAG, "insertElectionId, try to insert election id.")
                val toBeInsertedElectionId = ElectionId(electionId)
                database.electionIdDao.insertElectionId(toBeInsertedElectionId)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "insertElectionId, get error message")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }
}