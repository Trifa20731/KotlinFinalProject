package com.example.android.politicalpreparedness.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "election_id_table")
data class ElectionId(@PrimaryKey val id: Int)
