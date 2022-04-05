package com.example.android.politicalpreparedness.representative.adapter

import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListener(val clickListener: (representative: Representative) -> Unit) {
    fun onCLick(representative: Representative) = clickListener(representative)
}