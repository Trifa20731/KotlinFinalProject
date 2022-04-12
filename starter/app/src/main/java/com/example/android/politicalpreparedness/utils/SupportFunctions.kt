package com.example.android.politicalpreparedness.utils

import android.util.Log
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

}