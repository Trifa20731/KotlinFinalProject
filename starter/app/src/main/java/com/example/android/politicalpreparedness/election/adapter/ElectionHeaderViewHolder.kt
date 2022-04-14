package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ListHeaderElectionsBinding

class ElectionHeaderViewHolder private constructor(val binding: ListHeaderElectionsBinding): RecyclerView.ViewHolder(binding.root){
    fun bind() {
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ElectionHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListHeaderElectionsBinding.inflate(layoutInflater, parent, false)
            return ElectionHeaderViewHolder(binding)
        }
    }

}