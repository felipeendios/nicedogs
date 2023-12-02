package com.challenge.nicedogs.ui.fragments.breedslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.challenge.nicedogs.R
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
                binding.textviewErrorState.text = state.message
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
            is BreedsViewModelContract.Event.PaginationLoading -> binding.pbPaginationLoadingState.isVisible =
                event.isVisible

            is BreedsViewModelContract.Event.PaginationError -> Toast.makeText(
                requireContext(),
                event.message,
                Toast.LENGTH_LONG
            ).show()

            is BreedsViewModelContract.Event.ChangeViewStyle -> configureViewStyle(event.isListView)
            is BreedsViewModelContract.Event.ChangeAlphabeticalOrder -> configureAlphabeticalOrder(
                event.isAZSort
            )

            is BreedsViewModelContract.Event.NavigationToBreedDetail ->
                BreedsListFragmentDirections.actionNavigationBreedsToNavigationBreedDetail(event.id)
                    .also { action ->
                        view?.let { Navigation.findNavController(it).navigate(action) }
                    }
        }
    }

    private fun renderScreenVisibility(state: BreedsViewModelContract.State) {
        binding.pbLoadingState.isVisible = state is BreedsViewModelContract.State.LoadingState
        binding.textviewEmptyState.isVisible = state is BreedsViewModelContract.State.EmptyState
        binding.textviewErrorState.isVisible = state is BreedsViewModelContract.State.ErrorState
        binding.errorStateTryAgain.isVisible = state is BreedsViewModelContract.State.ErrorState
        binding.rvBreeds.isVisible = state is BreedsViewModelContract.State.SuccessState
    }

    private fun configureViewStyle(isListView: Boolean) {
        val itemCount = binding.rvBreeds.adapter?.itemCount
        if (binding.rvBreeds.layoutManager != null && (binding.rvBreeds.adapter?.itemCount
                ?: 0) > 0
        ) {
            (binding.rvBreeds.adapter as BreedsAdapter).changeViewStyle()
            binding.toolbar.imageList.background = ContextCompat.getDrawable(
                requireContext(),
                if (isListView) R.drawable.baseline_grid_on_24 else R.drawable.baseline_format_list_bulleted_24
            )
            (binding.rvBreeds.layoutManager as GridLayoutManager).spanCount =
                if (isListView) 1 else 2
            (binding.rvBreeds.adapter as BreedsAdapter).notifyItemRangeChanged(
                0,
                itemCount ?: 0
            )
        }
    }

    private fun configureAlphabeticalOrder(isAZSort: Boolean) {
        if ((binding.rvBreeds.adapter?.itemCount ?: 0) > 0) {
            val sortedList =
                (binding.rvBreeds.adapter as BreedsAdapter).currentList.sortedBy { it.name }
            (binding.rvBreeds.adapter as BreedsAdapter).submitList(if (isAZSort) sortedList else sortedList.reversed()) {
                binding.rvBreeds.scrollToPosition(0)
            }
        }
    }

    private fun configureRecyclerView(breeds: List<BreedDomainModel>?) {
        if (binding.rvBreeds.layoutManager == null)
            binding.rvBreeds.layoutManager = GridLayoutManager(requireContext(), 2)

        if (binding.rvBreeds.adapter == null)
            binding.rvBreeds.adapter = BreedsAdapter(this)

        binding.rvBreeds.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        binding.rvBreeds.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        configureScrollListener()
        configureToolbar()

        val updatedList =
            (binding.rvBreeds.adapter as BreedsAdapter).currentList + (breeds ?: emptyList())
        (binding.rvBreeds.adapter as BreedsAdapter).submitList(updatedList)
    }

    private fun configureScrollListener() {
        if (breedsRecyclerViewScrollListener == null) {
            breedsRecyclerViewScrollListener = object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1))
                        viewModel.invokeAction(
                            BreedsViewModelContract.Action.GetBreeds(
                                (binding.rvBreeds.layoutManager as GridLayoutManager)
                                    .findLastCompletelyVisibleItemPosition()
                            )
                        )
                }
            }
            binding.rvBreeds.addOnScrollListener(breedsRecyclerViewScrollListener as OnScrollListener)
        }
    }

    private fun configureToolbar() {
        binding.toolbar.imageList.setOnClickListener {
            viewModel.invokeAction(
                BreedsViewModelContract.Action.ChangeViewStyle
            )
        }
        binding.toolbar.imageviewOrder.setOnClickListener {
            viewModel.invokeAction(
                BreedsViewModelContract.Action.ChangeAlphabeticalOrder
            )
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
