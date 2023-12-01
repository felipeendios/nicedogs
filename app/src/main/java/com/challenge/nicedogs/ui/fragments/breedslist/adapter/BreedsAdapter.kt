package com.challenge.nicedogs.ui.fragments.breedslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.challenge.nicedogs.databinding.ItemListBreedBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.ui.fragments.BreedsEventHandler
import com.challenge.nicedogs.ui.fragments.breedslist.adapter.viewholders.BreedListItemViewHolder
import com.challenge.nicedogs.ui.utils.BreedDiffCallback

class BreedsAdapter(
    private val breedsEventHandler: BreedsEventHandler
) : ListAdapter<BreedDomainModel, RecyclerView.ViewHolder>(
    BreedDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemListBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreedListItemViewHolder(binding, breedsEventHandler)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { (holder as BreedListItemViewHolder).onBind(it) }
    }
}
