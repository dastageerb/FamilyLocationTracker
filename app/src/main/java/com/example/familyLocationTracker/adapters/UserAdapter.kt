package com.example.familyLocationTracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.example.familyLocationTracker.databinding.LayoutUserItemBinding
import com.example.familyLocationTracker.models.User


class UserAdapter( private val onClicked: (User) -> Unit) :
    ListAdapter<User, UserAdapter.ViewHolder>
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


    inner class ViewHolder(private val binding: LayoutUserItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {

        fun bind(item: User?, position: Int)
        {
            binding.layoutUserItemUserProfileImageView.load(item?.userImageUrl)
            binding.layoutUserItemUserNameTextView.text  = item?.userName
            binding.layoutUserItemUserCityTextView.text = item?.userCity

        } // bind closed

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view =
            LayoutUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(getItem(position), position)

        holder.itemView.setOnClickListener()
        {
            onClicked(getItem(position))
        }
    } // onBindViewHolder closed

}    