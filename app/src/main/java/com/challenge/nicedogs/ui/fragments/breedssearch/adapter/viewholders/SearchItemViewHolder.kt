package com.challenge.nicedogs.ui.fragments.breedssearch.adapter.viewholders

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.challenge.nicedogs.databinding.ItemSearchBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.ui.fragments.BreedsEventHandler

class SearchItemViewHolder(
    private val view: ItemSearchBinding,
    private val eventHandler: BreedsEventHandler
) : RecyclerView.ViewHolder(view.root) {

    fun onBind(breed: BreedDomainModel) {
        with(view) {
            bindTextView(textViewBreedNameLabel, textViewBreedNameValue, breed.name)
            bindTextView(textViewBreedGroupLabel, textViewBreedGroupValue, breed.group)
            bindTextView(textViewBreedOriginLabel, textViewBreedOriginValue, breed.origin)
            root.setOnClickListener { eventHandler.onBreedClick(breed.id) }
        }
    }

    private fun bindTextView(label: TextView, value: TextView, text: String?) {
        val isVisible = !text.isNullOrEmpty()
        label.isVisible = isVisible
        value.isVisible = isVisible
        text?.let { value.text = it }
    }
}
