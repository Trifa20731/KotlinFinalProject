package com.example.android.politicalpreparedness.election

import android.app.Application
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

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        //TODO: Handle loading of URLs


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
    }

    //TODO: Create method to load URL intents


//------------------------------------- Observer Functions -----------------------------------------


    private fun updateElectionSaveButton(state: Int) {
        when (state) {
            0 -> { binding.followToggleBtn.text = getString(R.string.follow_election_btn_label) }
            1 -> { binding.followToggleBtn.text = getString(R.string.unfollow_election_btn_label) }
        }

    }


}