package com.challenge.nicedogs.ui.fragments.breeddetail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.challenge.nicedogs.databinding.FragmentBreedDetailBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.domain.repository.BreedsDomainRepository
import com.challenge.nicedogs.domain.usecases.UseCaseGetBreedByID
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreedDetailFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBreedDetailBinding? = null
    private val binding get() = _binding!!
    private var dialog: BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreedDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog as BottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            arguments?.getInt(BREED_ID)?.let {
                renderScreen(
                    UseCaseGetBreedByID(BreedsDomainRepository.getInstance(requireContext()))(it)
                )
            }
        }
    }

    private fun setTextViewProperties(
        label: TextView,
        value: TextView,
        data: String?
    ) {
        label.isVisible = !data.isNullOrBlank()
        value.run {
            isVisible = !data.isNullOrBlank()
            text = data
        }
    }

    private fun renderScreen(breed: BreedDomainModel) = with(binding) {
        setTextViewProperties(textviewBreedNameLabel, textviewBreedNameValue, breed.name)
        setTextViewProperties(
            textviewBreedCategoryLabel,
            textviewBreedCategoryValue,
            breed.category
        )
        setTextViewProperties(textviewBreedOriginLabel, textviewBreedOriginValue, breed.origin)
        setTextViewProperties(
            textviewBreedTemperamentLabel,
            textviewBreedTemperamentValue,
            breed.temperament
        )
    }

    override fun onDestroy() {
        dialog?.dismiss()
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val BREED_ID = "breedId"
    }
}
