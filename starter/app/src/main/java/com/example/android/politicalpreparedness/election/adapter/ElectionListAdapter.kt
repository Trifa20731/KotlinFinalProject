package com.example.android.politicalpreparedness.election.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(val clickListener: ElectionClickListener):
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    // Bind View Holder.
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

}
