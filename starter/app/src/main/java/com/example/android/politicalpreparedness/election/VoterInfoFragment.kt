package com.example.android.politicalpreparedness.election

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.SupportFunctions

class VoterInfoFragment : Fragment() {

    companion object {
        const val LOG_TAG: String = "VoterInfoFragment"
    }

    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var application: Application
    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var viewModelFactory: VoterInfoViewModelFactory

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)
        application = requireNotNull(this.activity).application

        // Get the safe arg from the ElectionsFragment.
        val args: VoterInfoFragmentArgs = VoterInfoFragmentArgs.fromBundle(requireArguments())
        // Add ViewModel values and create ViewModel.
        initViewModelAndLifeCycleOwner(args.argElectionId, args.argDivision, args.argElectionState)
        // Initialize the click listener
        initClickListener(args.argElectionId)
        // Initialize the observer function
        initObserver()

        return binding.root
    }


//------------------------------------- Initialization ----------------------------------------------


    private fun initViewModelAndLifeCycleOwner(electionId: Int, division: Division, state: Int) {

        viewModelFactory = VoterInfoViewModelFactory(application, ElectionDatabase.getInstance(application))
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getElectionInfo(electionId, division, state)

    }

    private fun initClickListener(electionId: Int) {
        binding.followToggleBtn.setOnClickListener { viewModel.insertOrDeleteSaveElection(electionId) }
    }

    private fun initObserver() {
        viewModel.electionState.observe(viewLifecycleOwner, Observer { updateElectionSaveButton(it) })
        viewModel.voterInfoResponse.observe(viewLifecycleOwner, Observer { updateState(it) })
        viewModel.voterInfoState.observe(viewLifecycleOwner, Observer { updateUrl(it) })
    }


//------------------------------------- Observer Functions -----------------------------------------


    private fun updateElectionSaveButton(state: Int) {

        when (state) {
            0 -> { binding.followToggleBtn.text = getString(R.string.follow_election_btn_label) }
            1 -> { binding.followToggleBtn.text = getString(R.string.unfollow_election_btn_label) }
        }

    }

    private fun updateState(voterInfoResponse: VoterInfoResponse?) {
        SupportFunctions.showShortToast(application, "Refresh the Voter Information Response.")
        viewModel.updateState(voterInfoResponse!!)
    }

    private fun updateUrl(state: State) {

        val votingLocationUrl: String = state.electionAdministrationBody.votingLocationFinderUrl!!
        val ballotInfoUrl: String = state.electionAdministrationBody.ballotInfoUrl!!
        val openVotingLocationIntent = Intent(Intent.ACTION_VIEW)
        val openBallotInfoIntent = Intent(Intent.ACTION_VIEW)
        openVotingLocationIntent.data = Uri.parse(votingLocationUrl)
        openBallotInfoIntent.data = Uri.parse(ballotInfoUrl)

        binding.votingLocationTv.setOnClickListener { startActivity(openVotingLocationIntent) }
        binding.ballotInformationTv.setOnClickListener { startActivity(openBallotInfoIntent) }

    }


}