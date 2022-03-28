package com.example.android.politicalpreparedness.election.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    // Bind View Holder.
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    // Companion object to inflate ViewHolder (from)
    companion object {
        const val LOG_TAG: String = "ElectionListAdapter"
    }

}
