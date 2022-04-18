package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection

@Dao
interface FollowedElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowedElection(followedElection: FollowedElection)

    @Query("SELECT * FROM election_table WHERE id IN (SELECT * FROM followed_election_table) ")
    fun selectFollowedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM followed_election_table WHERE id = :followedElectionId")
    fun selectFollowedElection(followedElectionId: Int): LiveData<FollowedElection>

    @Query("DELETE FROM followed_election_table")
    suspend fun deleteFollowedElections()

    @Query("DELETE FROM followed_election_table WHERE id = :followedElectionId" )
    suspend fun deletedFollowedElectionsById(followedElectionId: Int): Int

}