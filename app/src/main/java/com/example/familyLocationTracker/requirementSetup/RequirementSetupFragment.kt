package com.example.familyLocationTracker.requirementSetup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentRequirementSetupBinding


class RequirementSetupFragment : BaseFragment<FragmentRequirementSetupBinding>()
{
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentRequirementSetupBinding
    {
        return FragmentRequirementSetupBinding.inflate(inflater,container,false)
    }




}