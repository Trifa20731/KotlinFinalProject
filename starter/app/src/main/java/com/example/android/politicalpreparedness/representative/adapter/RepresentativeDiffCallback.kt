package com.example.android.politicalpreparedness.representative.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeDiffCallback: DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem.office.division.id == newItem.office.division.id
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }
}