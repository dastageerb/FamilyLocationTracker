package com.example.familyLocationTracker.views.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.adapters.UserAdapter
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentHomeBinding
import com.example.familyLocationTracker.models.user.User
import com.example.familyLocationTracker.util.Constants
import com.example.familyLocationTracker.util.Constants.TAG
import com.example.familyLocationTracker.util.NetworkResponse
import com.example.familyLocationTracker.util.extensionFunctions.ContextExtension.showToast
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.example.familyLocationTracker.views.viewModels.MainViewModel
import com.example.familyLocationTracker.views.viewModels.HandleRequestsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HomeFragment : BaseFragment<FragmentHomeBinding>()
{



    val mainViewModel: MainViewModel by activityViewModels()
    val handleRequestsViewModel: HandleRequestsViewModel by activityViewModels()
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

      //  initLocationUploadingService()



        startWorker(requireContext())


    } // viewCreated closed

    private fun startWorker(context: Context)
    {

        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val refreshCpnWork = PeriodicWorkRequest.Builder(HomeWorker::class.java, 20, TimeUnit.MINUTES)
            .setConstraints(myConstraints)
            .addTag("myWorkManager")
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork("myWorkManager",
                ExistingPeriodicWorkPolicy.REPLACE, refreshCpnWork)

    } // startWorker


    override fun onPause()
    {
        super.onPause()
        Timber.tag(TAG).d("onPause")

    } // onPause closed


    override fun onStop()
    {
        super.onStop()
        Timber.tag(TAG).d("onStop")
    }


    override fun onResume()
    {
        super.onResume()
        Timber.tag(TAG).d("onResume")
    }

    private fun initLocationUploadingService()
    {
        mainViewModel.uploadUserLocation(requireContext())

    }


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
                    Timber.tag(Constants.TAG).d(""+it.data.toString())
                    it.data.let()
                    {
                        adapter.submitList(it)
                    }
                } //
            } // when closed
        } // observer closed
    } // initViews closed

    private fun setupRecycler(recycler: RecyclerView)
    {
        adapter = UserAdapter()
        {
            handleRequestsViewModel.sharedUser = it
           findNavController().navigate(R.id.action_homeFragment_to_addedFriendsProfileFragment)
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