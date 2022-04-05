package com.example.android.politicalpreparedness.repository

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

    val elections: LiveData<List<Election>> = database.electionDao.selectElections()

    suspend fun getElections() {
        withContext(Dispatchers.IO) {
            val electionList: List<Election> = CivicsApi.retrofitService.getElectionsListAsync().elections
            if (electionList.isNotEmpty()) {
                electionList.forEach { database.electionDao.insertElection(it) }
            }
        }
    }
    suspend fun getElectionById() {
        withContext(Dispatchers.IO) {

        }
    }
    suspend fun getVoterInfo(address: String, electionId: Long, officialOnly: Boolean) {
        withContext(Dispatchers.IO) {
            val voterInfoResponse: VoterInfoResponse =
                CivicsApi.retrofitService.getVoterInfoListAsync(
                    address,
                    electionId,
                    officialOnly
                )

        }
    }
    suspend fun getRepresentative() { }

    suspend fun saveElection() { }
    suspend fun deleteElection() { }
}