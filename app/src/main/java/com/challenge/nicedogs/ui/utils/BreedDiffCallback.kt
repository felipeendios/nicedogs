package com.challenge.nicedogs.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.challenge.nicedogs.domain.models.BreedDomainModel

class BreedDiffCallback : DiffUtil.ItemCallback<BreedDomainModel>() {

    override fun areItemsTheSame(oldItem: BreedDomainModel, newItem: BreedDomainModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BreedDomainModel, newItem: BreedDomainModel): Boolean =
        oldItem == newItem
}
