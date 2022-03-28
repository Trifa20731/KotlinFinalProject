package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import java.lang.IllegalArgumentException

class RepresentativeViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {
    /**
     * Create a new instance of the give class.
     * */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if ( modelClass.isAssignableFrom(RepresentativeViewModel::class.java) ) {
            return RepresentativeViewModel(application) as T
        }
        throw IllegalArgumentException( application.getString(R.string.unknown_view_model_exception) )
    }
}