package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

/**
 * Data Access Object for the task table.
 * */
@Dao
interface ElectionDao {

    /**
     * Insert Election. If the Election already exists, replace it.
     *
     * @param election the election to be inserted.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    /**
     * Select all election query.
     *
     * @return all Election.
     * */
    @Query("SELECT * FROM election_table")
    fun selectElections(): LiveData<List<Election>>

    /**
     * Select single election query.
     *
     * @param electionId the election id.
     * @return the election with electionId.
     * */
    @Query("SELECT * FROM election_table WHERE id = :electionId")
    fun selectElection(electionId: Int): LiveData<Election>

    /**
     * Delete all elections.
     * */
    @Query("DELETE FROM election_table")
    suspend fun deleteElections()

    /**
     * Delete a election by id.
     *
     * @return the number of election deleted. This should always be 1.
     * */
    @Query("DELETE FROM election_table WHERE id = :electionId")
    suspend fun deleteElectionById(electionId: Int): Int

}