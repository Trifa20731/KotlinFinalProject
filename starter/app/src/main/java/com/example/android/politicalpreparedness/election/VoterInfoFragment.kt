package com.example.android.politicalpreparedness.election

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
        initViewModelAndLifeCycleOwner(args.argElectionId, args.argDivision)
        // Initialize the click listener
        initClickListener(args.argElectionId)

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state

        //TODO: cont'd Handle save button clicks

        return binding.root
    }


//------------------------------------- Intialization ----------------------------------------------


    private fun initViewModelAndLifeCycleOwner(electionId: Int, division: Division) {
        viewModelFactory = VoterInfoViewModelFactory(application, ElectionDatabase.getInstance(application))
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getElectionInfo(electionId)
    }

    private fun initClickListener(electionId: Int) {
        binding.followToggleBtn.setOnClickListener { viewModel.insertFollowElection(electionId) }
    }

    //TODO: Create method to load URL intents

}