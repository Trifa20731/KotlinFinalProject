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
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionClickListener
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment: Fragment() {

    companion object {
        const val LOG_TAG: String = "ElectionsFragment"
    }

    private lateinit var binding: FragmentElectionBinding
    private lateinit var application: Application
    private lateinit var viewModel: ElectionsViewModel
    private lateinit var viewModelFactory: ElectionsViewModelFactory
    private lateinit var electionListAdapter: ElectionListAdapter

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
        electionListAdapter = ElectionListAdapter(ElectionClickListener { election ->
            viewModel.onElectionClicked(election)
        })
    }

    private fun initObserver() {
        viewModel.responseElectionsList.observe(viewLifecycleOwner, Observer { })
    }

    //TODO: Refresh adapters when fragment loads


}