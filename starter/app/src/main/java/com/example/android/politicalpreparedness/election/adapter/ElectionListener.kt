package com.example.android.politicalpreparedness.election.adapter

import com.example.android.politicalpreparedness.network.models.Election

class ElectionListener(val clickListener: (electionId: Int) -> Unit) {
    fun onClick(election: Election) = clickListener(election.id)
}