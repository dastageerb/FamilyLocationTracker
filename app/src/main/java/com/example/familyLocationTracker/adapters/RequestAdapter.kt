package com.example.familyLocationTracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.familyLocationTracker.databinding.LayoutRequestItemBinding

import com.example.familyLocationTracker.databinding.LayoutUserItemBinding
import com.example.familyLocationTracker.models.user.User


class RequestAdapter(private val onAccept: (User) -> Unit,private val onDecline: (User) -> Unit) :
    ListAdapter<User, RequestAdapter.ViewHolder>
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


    inner class ViewHolder(private val binding: LayoutRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {

        fun bind(item: User?, position: Int)
        {
            binding.layoutRequestItemUserProfileImageView.load(item?.userImageUrl)
            binding.layoutRequestItemUserNameTextView.text  = item?.userName



            binding.layoutRequestItemAcceptButton.setOnClickListener()
            {
                item?.let { it1 -> onAccept(it1) }
            }

            binding.layoutRequestItemDeclineButton.setOnClickListener()
            {
                item?.let { it1 -> onDecline(it1) }
            }


        } // bind closed

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view =
            LayoutRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(getItem(position), position)


    } // onBindViewHolder closed

}    