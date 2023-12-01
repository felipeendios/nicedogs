package com.challenge.nicedogs.ui.fragments.breedslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.challenge.nicedogs.databinding.FragmentBreedsBinding
import com.challenge.nicedogs.domain.models.BreedDomainModel
import com.challenge.nicedogs.ui.fragments.BreedsEventHandler
import com.challenge.nicedogs.ui.fragments.breedslist.adapter.BreedsAdapter
import com.challenge.nicedogs.ui.fragments.breedslist.viewmodel.BreedsViewModel
import com.challenge.nicedogs.ui.fragments.breedslist.viewmodel.BreedsViewModelContract
import com.challenge.nicedogs.ui.utils.AppViewModelFactory

class BreedsListFragment : Fragment(), BreedsEventHandler {

    private var _binding: FragmentBreedsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BreedsViewModel by viewModels { AppViewModelFactory.invoke(requireContext().applicationContext) }

    private var breedsRecyclerViewScrollListener: OnScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.breedsState.observe(viewLifecycleOwner, Observer(::renderViewState))
        viewModel.breedsEvent.observe(viewLifecycleOwner, Observer(::performViewEvent))
        viewModel.invokeAction(BreedsViewModelContract.Action.GetBreeds())
    }

    private fun renderViewState(state: BreedsViewModelContract.State) {
        renderScreenVisibility(state)

        when (state) {
            is BreedsViewModelContract.State.ErrorState -> {
                binding.errorState.text = state.message
                binding.errorStateTryAgain.setOnClickListener {
                    viewModel.invokeAction(
                        BreedsViewModelContract.Action.GetBreeds(0)
                    )
                }
            }

            is BreedsViewModelContract.State.SuccessState -> configureRecyclerView(state.breeds)

            else -> Unit
        }
    }

    private fun performViewEvent(event: BreedsViewModelContract.Event) {
        when (event) {
            is BreedsViewModelContract.Event.PaginationError -> Toast.makeText(
                requireContext(),
                event.message,
                Toast.LENGTH_LONG
            ).show()

            is BreedsViewModelContract.Event.NavigationToBreedDetail ->
                BreedsListFragmentDirections.actionNavigationBreedsToNavigationBreedDetail(event.id)
                    .also { action ->
                        view?.let { Navigation.findNavController(it).navigate(action) }
                    }

            else -> {}
        }
    }

    private fun renderScreenVisibility(state: BreedsViewModelContract.State) {
        binding.loadingState.isVisible = state is BreedsViewModelContract.State.LoadingState
        binding.emptyState.isVisible = state is BreedsViewModelContract.State.EmptyState
        binding.errorState.isVisible = state is BreedsViewModelContract.State.ErrorState
        binding.errorStateTryAgain.isVisible = state is BreedsViewModelContract.State.ErrorState
        binding.successState.isVisible = state is BreedsViewModelContract.State.SuccessState
    }

    private fun configureRecyclerView(breeds: List<BreedDomainModel>?) {
        if (binding.successState.layoutManager == null)
            binding.successState.layoutManager = GridLayoutManager(requireContext(), 1)

        if (binding.successState.adapter == null)
            binding.successState.adapter = BreedsAdapter(this)

        binding.successState.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        binding.successState.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        configureScrollListener()

        val updatedList =
            (binding.successState.adapter as BreedsAdapter).currentList + (breeds ?: emptyList())
        (binding.successState.adapter as BreedsAdapter).submitList(updatedList)
    }

    private fun configureScrollListener() {
        if (breedsRecyclerViewScrollListener == null) {
            breedsRecyclerViewScrollListener = object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1))
                        viewModel.invokeAction(
                            BreedsViewModelContract.Action.GetBreeds(
                                (binding.successState.layoutManager as GridLayoutManager)
                                    .findLastCompletelyVisibleItemPosition()
                            )
                        )
                }
            }
            binding.successState.addOnScrollListener(breedsRecyclerViewScrollListener as OnScrollListener)
        }
    }

    override fun onBreedClick(id: Int) {
        viewModel.invokeAction(BreedsViewModelContract.Action.NavigateToBreedDetails(id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
