package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionId
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
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
    val savedElections: LiveData<List<Election>> = database.electionIdDao.selectSavedElections()
    val unsavedElections: LiveData<List<Election>> = database.electionIdDao.selectUnsavedElections()


//------------------------------------- Election ---------------------------------------------------


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

    suspend fun insertSavedElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(LOG_TAG, "insertSavedElection, try to insert election id.")
                val toBeInsertedElectionId = ElectionId(electionId)
                database.electionIdDao.insertElectionId(toBeInsertedElectionId)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "insertSavedElection, get error message")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }

    suspend fun deleteSavedElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(LOG_TAG, "deleteSavedElection, try to delete election id.")
                database.electionIdDao.deletedSavedElectionIdById(electionId)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "deleteSavedElection, get error message")
                Log.e(LOG_TAG, e.message!!)
            }
        }
    }


//------------------------------------- VoterInfo --------------------------------------------------


    suspend fun refreshVoterInfo(address: String, electionId: Long): VoterInfoResponse? =
        withContext(Dispatchers.IO) {
            try {
                Log.d(LOG_TAG, "refreshVoterInfo, try to get the voter info from Internet.")
                CivicsApi.retrofitService.getVoterInfoListAsync(address, electionId)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "refreshVoterInfo, get error message.")
                Log.e(LOG_TAG, e.message!!)
                null
            }
        }
}
