package com.example.android.politicalpreparedness.election

import android.app.Application
import android.os.Bundle
import android.util.Log
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
    }

    private lateinit var binding: FragmentElectionBinding
    private lateinit var application: Application
    private lateinit var viewModel: ElectionsViewModel
    private lateinit var viewModelFactory: ElectionsViewModelFactory
    private lateinit var electionListAdapter: ElectionListAdapter
    private lateinit var followedElectionListAdapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        application = requireNotNull(this.activity).application

        // Add ViewModel values and create ViewModel
        initViewModelAndLifeCycleOwner()

        //TODO: Add binding values

        //TODO: Link elections to voter info

        // Initiate recycler adapters
        initAdapter()
        // Initiate observer function
        initObserver()

        //TODO: Populate recycler adapters

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
            ElectionClickListener { election -> view!!.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division)) })
        followedElectionListAdapter = ElectionListAdapter(
            getString(R.string.saved_elections_tv_label),
            ElectionClickListener { election -> view!!.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id,election.division)) })
        binding.upcomingElectionsRv.adapter = electionListAdapter
        binding.savedElectionsRv.adapter = followedElectionListAdapter
    }

    private fun initObserver() {
        viewModel.stateInfoShowing.observe(viewLifecycleOwner, Observer { SupportFunctions.showShortToast(application, it) })
        viewModel.upcomingElectionsResponseList.observe(viewLifecycleOwner, Observer { viewModel.refreshElectionListData() })
        viewModel.followedElectionResponseList.observe(viewLifecycleOwner, Observer { viewModel.refreshFollowedElectionsData() })
        viewModel.upcomingElectionsShowingList.observe(viewLifecycleOwner, Observer { electionListAdapter.submitList(it) })
        viewModel.followedElectionShowingList.observe(viewLifecycleOwner, Observer { followedElectionListAdapter.submitList(it) })
    }

}