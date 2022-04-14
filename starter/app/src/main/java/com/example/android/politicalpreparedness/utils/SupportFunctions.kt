package com.example.android.politicalpreparedness.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.android.politicalpreparedness.network.models.ElectionResponse

/**
 * The support functions object help us to check the process in the app running.
 * */
object SupportFunctions {

    const val LOG_TAG_TMP_ELECTIONS_RESPONSE: String = "TempElectionResponse"

    fun logElectionResponse(electionResponse: ElectionResponse) {
        Log.d(LOG_TAG_TMP_ELECTIONS_RESPONSE, "The kind of response is ${electionResponse.kind}")
        for (networkElection in electionResponse.elections) {
            Log.d(LOG_TAG_TMP_ELECTIONS_RESPONSE, "The id ${networkElection.id}, with name ${networkElection.name}, at ${networkElection.electionDay}, in division ${networkElection.division}")
        }
    }

    fun showShortToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}