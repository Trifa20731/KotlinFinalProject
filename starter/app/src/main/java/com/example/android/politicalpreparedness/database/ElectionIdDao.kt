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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElectionId(electionId: ElectionId)

    @Query("SELECT * FROM election_table WHERE id IN (SELECT * FROM election_id_table) ")
    fun selectSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id NOT IN (SELECT * FROM election_id_table)")
    fun selectUnsavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_id_table WHERE id = :electionId")
    fun selectSavedElectionId(electionId: Int): LiveData<ElectionId>

    @Query("DELETE FROM election_id_table")
    suspend fun deleteSavedElectionIds()

    @Query("DELETE FROM election_id_table WHERE id = :electionId" )
    suspend fun deletedSavedElectionIdById(electionId: Int): Int

}