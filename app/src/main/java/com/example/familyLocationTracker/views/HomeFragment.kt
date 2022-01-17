package com.example.familyLocationTracker.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class HomeFragment : BaseFragment<FragmentHomeBinding>()
{
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentHomeBinding
    {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)



    } // onViewCreated closed


} // HomeFragment closed