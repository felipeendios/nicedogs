package com.challenge.nicedogs.ui.fragments.breedslist.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.challenge.nicedogs.databinding.ItemListBreedBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.ui.fragments.BreedsEventHandler
import com.challenge.nicedogs.ui.utils.loadImage

class BreedListItemViewHolder(
    private val binding: ItemListBreedBinding,
    private val eventHandler: BreedsEventHandler
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(breed: BreedDomainModel) {
        binding.textViewBreedName.text = breed.name
        binding.imageViewBreed.loadImage(breed.image)
        binding.root.setOnClickListener { eventHandler.onBreedClick(breed.id) }
    }
}
