package com.example.familyLocationTracker.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.familyLocationTracker.R
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.hide
import com.example.familyLocationTracker.util.extensionFunctions.ExtensionFunctions.show
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()
{

    lateinit var navController:NavController
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.activityMainBottomNavigation)

        bottomNavigationView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment , R.id.familyMapFragment , R.id.friendRequestsFragment , R.id.ownProfileFragment
            ))

            navController.addOnDestinationChangedListener()
            { _, destination, _ ->

                when(destination.id)
                {
                    R.id.registerForOtpFragment,R.id.otpVerificationFragment,R.id.requirementSetupFragment , R.id.setupProfileFragment ->
                    {
                        bottomNavigationView.hide()
                    }
                    else -> bottomNavigationView.show()
                }
            }


        setupActionBarWithNavController(navController,appBarConfiguration)

    } // onCreate closed

    override fun onSupportNavigateUp(): Boolean
    {
        return  navController.navigateUp() || super.onSupportNavigateUp()
    }

}