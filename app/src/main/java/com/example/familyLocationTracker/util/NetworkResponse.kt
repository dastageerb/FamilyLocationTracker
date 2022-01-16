package com.example.familyLocationTracker.util

sealed class NetworkResponse<T>(private val data:T?=null, val message:String?=null)
{



    class Loading<T>:NetworkResponse<T>(null,null)
    class Error<T>(val msg: String?):NetworkResponse<T>(null,msg)
    class Success<T>(val data: T?):NetworkResponse<T>(data,null)


}