package com.example.familyLocationTracker.features.requirementSetup

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.familyLocationTracker.base.BaseFragment
import com.example.familyLocationTracker.databinding.FragmentRequirementSetupBinding


class RequirementSetupFragment : BaseFragment<FragmentRequirementSetupBinding>()
{
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, root: Boolean): FragmentRequirementSetupBinding
    {
        return FragmentRequirementSetupBinding.inflate(inflater,container,false)
    }




}