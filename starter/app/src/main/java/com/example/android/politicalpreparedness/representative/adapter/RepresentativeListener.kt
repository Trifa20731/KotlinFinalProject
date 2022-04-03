package com.example.android.politicalpreparedness.representative.adapter

import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListener(val clickListener: (representativeId: String) -> Unit) {
    fun onCLick(representative: Representative) = clickListener(representative.office.division.id)
}