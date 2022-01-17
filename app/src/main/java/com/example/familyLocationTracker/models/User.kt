package com.example.familyLocationTracker.models

data class User(
    val userName:String
    ,val userContact:String
    ,val userCity:String
    ,val userImageUrl:String
    ,val userLocation: UserLocation
)