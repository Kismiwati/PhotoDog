package com.kismi.photodog.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kismi.photodog.databinding.ImageLoadStateFooterBinding


class GalleryLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<GalleryLoadStateAdapter.LoadStateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ImageLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: ImageLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //digunakan untuk mengatur onclick agar tidak perlu mengulanginya lagi
        init {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }

        }

    }


}