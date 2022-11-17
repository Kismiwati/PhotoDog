package com.kismi.photodog.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.kismi.photodog.R
import com.kismi.photodog.data.DogImages
import com.kismi.photodog.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment :
    Fragment(R.layout.fragment_gallery), GalleryAdapter.OnItemClickListener {
    private val viewModel by viewModels<DogGalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)

        val adapter = GalleryAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = GalleryLoadStateAdapter { adapter.retry() },
                footer = GalleryLoadStateAdapter { adapter.retry() },
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        //bagian ini digunakan untuk memperbaharui UI saat tampilan fragmen diubah
        viewModel.photos.observe(viewLifecycleOwner) {
            //bagian ini merupakan siklus tampilan fragmen
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        //bagian ini digunakan untuk membuat tampilan terlihat atau tidak terlihat
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // bagian ini dugunakan untuk tampilan kosong
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }

        }
        setHasOptionsMenu(true)
    }

    override fun onItemClick(photo: DogImages) {

        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment2(photo)
        findNavController().navigate(action)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus() // bagian ini digunakan untuk menyembunyikan keyboard
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}