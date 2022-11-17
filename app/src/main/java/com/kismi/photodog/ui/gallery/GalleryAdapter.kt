package com.kismi.photodog.ui.gallery


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kismi.photodog.R
import com.kismi.photodog.data.DogImages
import com.kismi.photodog.databinding.ItemPhotoListBinding


class GalleryAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<DogImages, GalleryAdapter.DogViewHolder>(IMAGE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {

        val binding =
            ItemPhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    //bagian ini digunakan untuk mengakses properti dari class yang akan dijadikan sebagai inner class
    inner class DogViewHolder(private val binding: ItemPhotoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }


        fun bind(photo: DogImages) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
                textViewUserName.text = photo.user.username
                textViewLikes.text = photo.likes.toString()
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: DogImages)
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<DogImages>() {
            override fun areItemsTheSame(oldItem: DogImages, newItem: DogImages) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DogImages, newItem: DogImages) =
                oldItem == newItem
        }
    }


}
