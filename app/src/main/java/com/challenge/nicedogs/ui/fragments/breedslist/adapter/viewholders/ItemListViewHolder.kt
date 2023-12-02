package com.challenge.nicedogs.ui.fragments.breedslist.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.challenge.nicedogs.databinding.ItemListBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.ui.fragments.BreedsEventHandler
import com.challenge.nicedogs.ui.utils.loadImage

class ItemListViewHolder(
    private val binding: ItemListBinding,
    private val eventHandler: BreedsEventHandler
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(breed: BreedDomainModel) {
        binding.apply {
            textViewBreedName.text = breed.name
            imageViewBreed.loadImage(breed.image)
            root.setOnClickListener { eventHandler.onBreedClick(breed.id) }
        }
    }
}
