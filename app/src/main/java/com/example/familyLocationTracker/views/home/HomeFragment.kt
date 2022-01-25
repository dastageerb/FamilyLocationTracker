package com.example.familyLocationTracker.views.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.adapters.UserAdapter
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentHomeBinding
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.example.familyLocationTracker.views.MainViewModel
import com.example.familyLocationTracker.views.otherUserProfile.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class HomeFragment : BaseFragment<FragmentHomeBinding>()
{



    val mainViewModel: MainViewModel by activityViewModels()
    val userProfileViewModel: UserProfileViewModel by activityViewModels()
    lateinit var adapter : UserAdapter


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentHomeBinding
    {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupRecycler(binding.fragmentHomeRecyclerView)

        initViews()

    } // viewCreated closed

    private fun initViews()
    {
        mainViewModel.getFriends()

        mainViewModel.getUsersResponse.observe(viewLifecycleOwner)
        {
            when(it)
            {

                is NetworkResponse.Loading ->
                {
                    binding.fragmentHomeProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                    binding.fragmentHomeProgressBar.hide()
                    requireContext().showToast(it.msg!!)
                }
                is NetworkResponse.Success ->
                {
                    binding.fragmentHomeProgressBar.hide()
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
            userProfileViewModel.sharedUser = it
            findNavController().navigate(R.id.action_searchUsersFragment_to_userProfileFragment)
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter


    } // setupRecyclerView closed



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_home_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.menuSearch ->
            {
                findNavController().navigate(R.id.action_homeFragment_to_searchUsersFragment)
            }
        } // when closed
        return super.onOptionsItemSelected(item)

    } // onOptionsItemSelected closed

} // HomeFragment closed