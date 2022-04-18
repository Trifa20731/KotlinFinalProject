package com.example.android.politicalpreparedness.election.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(val electionHeader: String, val clickListener: ElectionClickListener):
    ListAdapter<Election, RecyclerView.ViewHolder>(ElectionDiffCallback()) {

    companion object {
        const val TYPE_HEADER: Int = 0
        const val TYPE_LIST_ITEM: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> { ElectionHeaderViewHolder.from(parent) }
            TYPE_LIST_ITEM -> { ElectionItemViewHolder.from(parent) }
            else -> { ElectionItemViewHolder.from(parent) }
        }
    }

    // Bind View Holder.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ElectionHeaderViewHolder) {
            holder.bind(electionHeader)
        } else if ( holder is ElectionItemViewHolder ) {
            holder.bind(clickListener, getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_LIST_ITEM
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}
