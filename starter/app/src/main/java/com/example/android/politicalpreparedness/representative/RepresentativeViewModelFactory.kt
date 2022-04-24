package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import java.lang.IllegalArgumentException

class RepresentativeViewModelFactory(
    private val application: Application,
    private val dataSource: ElectionDatabase
): ViewModelProvider.Factory {
    /**
     * Create a new instance of the give class.
     * */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if ( modelClass.isAssignableFrom(RepresentativeViewModel::class.java) ) {
            return RepresentativeViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException( application.getString(R.string.unknown_view_model_exception) )
    }
}