package com.example.android.politicalpreparedness.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "followed_election_table")
data class FollowedElection(@PrimaryKey val id: Int)
