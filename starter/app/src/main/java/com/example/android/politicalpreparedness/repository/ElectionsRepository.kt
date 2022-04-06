package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
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

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            try {
                val tmpResponse = CivicsApi.retrofitService.getElectionsListAsync()
                database.electionDao.insertAllElections(*(tmpResponse.elections).toTypedArray())
            } catch (e: Exception) {
                Log.d(LOG_TAG, e.message!!)
            }
        }
    }
}