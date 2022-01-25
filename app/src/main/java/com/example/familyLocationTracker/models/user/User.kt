package com.example.familyLocationTracker.models.user

data class User(
    val userName:String?=null
    ,val userContact:String?=null
    ,val userCity:String?=null
    ,val userImageUrl:String?=null
    ,val userLocation: UserLocation?=null
)