package com.challenge.nicedogs.ui.fragments.breedssearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.challenge.nicedogs.databinding.ItemSearchBreedBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.ui.fragments.BreedsEventHandler
import com.challenge.nicedogs.ui.fragments.breedssearch.adapter.viewholders.SearchItemViewHolder
import com.challenge.nicedogs.ui.utils.BreedDiffCallback

class SearchAdapter(
    private val breedsEventHandler: BreedsEventHandler
) : ListAdapter<BreedDomainModel, RecyclerView.ViewHolder>(
    BreedDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSearchBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchItemViewHolder(binding, breedsEventHandler)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { (holder as SearchItemViewHolder).onBind(it) }
    }
}