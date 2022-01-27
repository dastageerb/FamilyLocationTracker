package com.example.familyLocationTracker.views.friendRequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyLocationTracker.adapters.RequestAdapter
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentFriendRequestsBinding
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.util.Constants.TAG
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.example.familyLocationTracker.views.viewModels.HandleRequestsViewModel
import com.example.familyLocationTracker.views.viewModels.MainViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber


class FriendRequestsFragment : BaseFragment<FragmentFriendRequestsBinding>()
{

    val mainViewModel: MainViewModel by activityViewModels()
    val handleRequestsViewModel: HandleRequestsViewModel by activityViewModels()
    lateinit var adapter : RequestAdapter


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentFriendRequestsBinding
    {
        return  FragmentFriendRequestsBinding.inflate(inflater,container,false)
    } // createView closed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler(binding.fragmentFriendRequestRecyclerView)

        initViews()

    } // viewCreated closed

    private fun initViews()
    {
        mainViewModel.getAllRequests()

        mainViewModel.getUsersResponse.observe(viewLifecycleOwner)
        {
            when(it)
            {

                is NetworkResponse.Loading ->
                {
                    binding.fragmentFriendRequestProgressBar.show()
                }
                is NetworkResponse.Error ->
                {
                    binding.fragmentFriendRequestProgressBar.hide()
                    requireContext().showToast(it.msg!!)
                }
                is NetworkResponse.Success ->
                {
                   binding.fragmentFriendRequestProgressBar.hide()

                    Timber.tag(TAG).d(""+it.data.toString())

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
        adapter = RequestAdapter(
            {
                /// accept it
                handleRequest(true,it)
            },
            {
                // decline it
                handleRequest(false,it)
            })

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

    } // setupRecyclerView closed


    // accept or delete the request and fetch the updated requests list

    private fun handleRequest(accept: Boolean, user: User) = lifecycleScope.launch()
    {
        binding.fragmentFriendRequestProgressBar.show()
        if(accept)
        {
             async { handleRequestsViewModel.acceptFriendRequest(user) }.await()
        }else
        {
             async { handleRequestsViewModel.cancelOrDeclineFriendRequest(user.userContact!!) }.await()
        }
        mainViewModel.getAllRequests()
    } // handleRequest closed

} // FriendRequestsFragment closed