package com.example.familyLocationTracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.familyLocationTracker.databinding.LayoutRequestItemBinding

import com.example.familyLocationTracker.databinding.LayoutUserItemBinding
import com.example.familyLocationTracker.databinding.LayoutUsersOnMapBinding
import com.example.familyLocationTracker.models.user.User


class UsersOnMap(private val onUserSelected: (User) -> Unit) :
    ListAdapter<User, UsersOnMap.ViewHolder>
        (object : DiffUtil.ItemCallback<User>()
    {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean
        {
            return oldItem == newItem
        }


        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean
        {
            return oldItem.toString() == newItem.toString()
        }
    })
{


    inner class ViewHolder(private val binding: LayoutUsersOnMapBinding) :
        RecyclerView.ViewHolder(binding.root)
    {

        fun bind(item: User?, position: Int)
        {
            binding.layoutUsersOnMapUserProfile.load(item?.userImageUrl)
        } // bind closed

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view =
            LayoutUsersOnMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(getItem(position), position)


        holder.itemView.setOnClickListener()
        {
            onUserSelected(getItem(position))
        }


    } // onBindViewHolder closed

}    