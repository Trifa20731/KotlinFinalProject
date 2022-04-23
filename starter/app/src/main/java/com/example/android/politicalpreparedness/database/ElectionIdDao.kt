package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionId

@Dao
interface ElectionIdDao {

    /**
     * Insert the election Id. If the Election already exists, replace it.
     *
     * @param electionId The id of the election.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElectionId(electionId: ElectionId)

    /**
     * Select the elections whose id are in the election_id_table.
     *
     * @return Saved Election.
     * */
    @Query("SELECT * FROM election_table WHERE id IN (SELECT * FROM election_id_table) ")
    fun selectSavedElections(): LiveData<List<Election>>

    /**
     * Select the elections whose id are not in the election_id_table.
     *
     * @return Unsaved Election.
     * */
    @Query("SELECT * FROM election_table WHERE id NOT IN (SELECT * FROM election_id_table)")
    fun selectUnsavedElections(): LiveData<List<Election>>

    /**
     * Select all election ID query.
     *
     * @return all Election ID.
     * */
    @Query("SELECT * FROM election_id_table WHERE id = :electionId")
    fun selectSavedElectionId(electionId: Int): LiveData<ElectionId>


    /**
     * Delete all election ids.
     * */
    @Query("DELETE FROM election_id_table")
    suspend fun deleteSavedElectionIds()

    /**
     * Delete a election id by id.
     *
     * @return the number of election id deleted. This should always be 1.
     * */
    @Query("DELETE FROM election_id_table WHERE id = :electionId" )
    suspend fun deletedSavedElectionIdById(electionId: Int): Int

}