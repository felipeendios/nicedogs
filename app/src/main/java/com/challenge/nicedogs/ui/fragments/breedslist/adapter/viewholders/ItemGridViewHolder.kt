package com.challenge.nicedogs.ui.fragments.breedslist.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.challenge.nicedogs.databinding.ItemGridBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.ui.fragments.BreedsEventHandler
import com.challenge.nicedogs.ui.utils.loadImage

class ItemGridViewHolder(
    private val binding: ItemGridBinding,
    private val eventHandler: BreedsEventHandler
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(breed: BreedDomainModel) {
        binding.apply {
            textviewBreedName.text = breed.name
            imageviewBreedPicture.loadImage(breed.image)
            root.setOnClickListener { eventHandler.onBreedClick(breed.id) }
        }
    }
}