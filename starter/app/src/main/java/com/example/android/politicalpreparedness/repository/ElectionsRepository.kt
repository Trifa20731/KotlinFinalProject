package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election

/**
 * The repository hides the complexity of managing the interactions between the database and the
 * networking code.
 * */
class ElectionsRepository(private val database: ElectionDatabase) {

    val elections: LiveData<List<Election>> = database.electionDao.selectElections()

    suspend fun getElections() { }
    suspend fun getElectionById() {  }
    suspend fun getVoterInfo() { }
    suspend fun getRepresentative() { }

    suspend fun saveElection() { }
    suspend fun deleteElection() { }
}