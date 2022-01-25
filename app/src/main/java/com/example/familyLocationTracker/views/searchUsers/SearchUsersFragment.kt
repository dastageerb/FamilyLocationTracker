package com.example.familyLocationTracker.views.searchUsers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.adapters.UserAdapter
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentSearchUsersBinding
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.example.familyLocationTracker.views.MainViewModel


class SearchUsersFragment : BaseFragment<FragmentSearchUsersBinding>()
{


    val mainViewModel:MainViewModel by activityViewModels()
    lateinit var adapter :UserAdapter
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentSearchUsersBinding
    {
        return  FragmentSearchUsersBinding.inflate(inflater,container,false)
    } // createView closed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler(binding.fragmentSearchUsersRecyclerView)

        initViews()

    } // viewCreated closed

    private fun initViews()
    {
        mainViewModel.getAllUsers()

        mainViewModel.getUsersResponse.observe(viewLifecycleOwner)
        {
            when(it)
            {

                is NetworkResponse.Loading ->
                {
                    binding.fragmentSearchUsersProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                    binding.fragmentSearchUsersProgressBar.hide()
                    requireContext().showToast(it.msg!!)
                }
                is NetworkResponse.Success ->
                {
                    binding.fragmentSearchUsersProgressBar.hide()
                    it.data.let()
                    {
                        adapter.submitList(it)
                    }
                }
            } // when closed
        } // observer closed


    } // initViews closed

    private fun setupRecycler(recycler: RecyclerView)
    {
        adapter = UserAdapter()
        {
            mainViewModel.sharedUser = it
            findNavController().navigate(R.id.action_searchUsersFragment_to_userProfileFragment)
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter


    } // setupRecyclerView closed


} // SearchUsersFragment closed