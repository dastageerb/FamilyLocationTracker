package com.example.familyLocationTracker.views.ownProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentOwnProfileBinding


class OwnProfileFragment : BaseFragment<FragmentOwnProfileBinding>()
{

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentOwnProfileBinding
    {
        return FragmentOwnProfileBinding.inflate(inflater,container,false)
    }

}