package com.example.android.politicalpreparedness.election

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionClickListener
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.utils.SupportFunctions

class ElectionsFragment: Fragment() {

    companion object {
        const val LOG_TAG: String = "ElectionsFragment"
        const val ELECTION_STATE_UPCOMING: Int = 0
        const val ELECTION_STATE_SAVED: Int = 1
    }

    private lateinit var binding: FragmentElectionBinding
    private lateinit var application: Application
    private lateinit var viewModel: ElectionsViewModel
    private lateinit var viewModelFactory: ElectionsViewModelFactory
    private lateinit var electionListAdapter: ElectionListAdapter
    private lateinit var savedElectionListAdapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        application = requireNotNull(this.activity).application

        // Add ViewModel values and create ViewModel
        initViewModelAndLifeCycleOwner()
        // Initiate recycler adapters
        initAdapter()
        // Initiate observer function
        initObserver()
        return binding.root
    }


//------------------------------------- Initialization ---------------------------------------------


    private fun initViewModelAndLifeCycleOwner() {
        viewModelFactory = ElectionsViewModelFactory(application, ElectionDatabase.getInstance(application))
        viewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initAdapter() {
        electionListAdapter = ElectionListAdapter(
            getString(R.string.upcoming_elections_tv_label),
            ElectionClickListener { election -> requireView().findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division, ELECTION_STATE_UPCOMING)) })
        savedElectionListAdapter = ElectionListAdapter(
            getString(R.string.saved_elections_tv_label),
            ElectionClickListener { election -> requireView().findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division, ELECTION_STATE_SAVED)) })
        binding.upcomingElectionsRv.adapter = electionListAdapter
        binding.savedElectionsRv.adapter = savedElectionListAdapter
    }

    private fun initObserver() {
        viewModel.stateInfoShowing.observe(viewLifecycleOwner, Observer { SupportFunctions.showShortToast(application, it) })
        //viewModel.rawElectionsResponseList.observe(viewLifecycleOwner, Observer { viewModel.refreshElectionListData() })
        viewModel.unsavedElectionResponseList.observe(viewLifecycleOwner, Observer { viewModel.refreshUpcomingElectionListData() })
        viewModel.savedElectionResponseList.observe(viewLifecycleOwner, Observer { viewModel.refreshSavedElectionListData() })
        viewModel.upcomingElectionsShowingList.observe(viewLifecycleOwner, Observer { electionListAdapter.addHeaderAndSubmitList(it) })
        viewModel.savedElectionShowingList.observe(viewLifecycleOwner, Observer { savedElectionListAdapter.addHeaderAndSubmitList(it) })
    }

}