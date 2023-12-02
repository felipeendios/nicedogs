package com.challenge.nicedogs.ui.fragments.breedslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.challenge.nicedogs.databinding.ItemGridBinding
import com.challenge.nicedogs.databinding.ItemListBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.ui.fragments.BreedsEventHandler
import com.challenge.nicedogs.ui.fragments.breedslist.adapter.viewholders.ItemGridViewHolder
import com.challenge.nicedogs.ui.fragments.breedslist.adapter.viewholders.ItemListViewHolder
import com.challenge.nicedogs.ui.utils.BreedDiffCallback

class BreedsAdapter(
    private val breedsEventHandler: BreedsEventHandler
) : ListAdapter<BreedDomainModel, RecyclerView.ViewHolder>(
    BreedDiffCallback()
) {

    private var isListView = false

    fun changeViewStyle() {
        isListView = !isListView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val binding =
                    ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemListViewHolder(binding, breedsEventHandler)
            }

            2 -> {
                val binding =
                    ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemGridViewHolder(binding, breedsEventHandler)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemListViewHolder -> getItem(position)?.let { holder.onBind(it) }
            is ItemGridViewHolder -> getItem(position)?.let { holder.onBind(it) }
        }
    }

    override fun getItemViewType(position: Int): Int = if (isListView) 1 else 2
}
