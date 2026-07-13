package com.example.fragmentspractice.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentspractice.data.Challenge
import com.example.fragmentspractice.databinding.ItemChallengeBinding
import com.example.fragmentspractice.utils.animateTouch

class ChallengeAdapter(
    private val onEditClick: (Challenge) -> Unit,
    private val onDeleteClick: (Challenge) -> Unit
) : ListAdapter<Challenge, ChallengeAdapter.ChallengeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChallengeViewHolder(
        private val binding: ItemChallengeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(challenge: Challenge) {
            binding.tvChallengeText.text = challenge.text

            binding.btnEditChallenge.setOnClickListener {
                it.animateTouch { onEditClick(challenge) }
            }

            binding.btnDeleteChallenge.setOnClickListener {
                it.animateTouch { onDeleteClick(challenge) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Challenge>() {
            override fun areItemsTheSame(oldItem: Challenge, newItem: Challenge) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Challenge, newItem: Challenge) =
                oldItem == newItem
        }
    }
}
