package com.example.android.politicalpreparedness.election.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

class ElectionListAdapter(
    private val electionHeader: String,
    private val clickListener: ElectionClickListener):
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ElectionDiffCallback()) {

    companion object {
        const val TYPE_HEADER: Int = 0
        const val TYPE_LIST_ITEM: Int = 1
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Election>?) {
        adapterScope.launch {
            val items = when(list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.ElectionItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> { ElectionHeaderViewHolder.from(parent) }
            TYPE_LIST_ITEM -> { ElectionItemViewHolder.from(parent) }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    // Bind View Holder.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ElectionHeaderViewHolder) {
            holder.bind(electionHeader)
        } else if ( holder is ElectionItemViewHolder ) {
            val electionItem = getItem(position) as DataItem.ElectionItem
            holder.bind(clickListener, electionItem.election)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_LIST_ITEM
        }
    }
}

sealed class DataItem {

    data class ElectionItem(val election: Election): DataItem() {
        override val id: Long = election.id.toLong()
    }

    object Header: DataItem() {
        override val id: Long = Long.MIN_VALUE
    }

    abstract val id: Long

}
