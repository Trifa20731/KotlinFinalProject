package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase

// Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(
    private val application: Application,
    private val dataSource: ElectionDatabase
) : ViewModelProvider.Factory {
    /**
     * Create a new instance of the give class.
     * */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException(application.getString(R.string.unknown_view_model_exception))
    }
}