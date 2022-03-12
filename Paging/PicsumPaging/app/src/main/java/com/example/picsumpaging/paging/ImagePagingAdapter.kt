package com.example.picsumpaging.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.picsumpaging.R
import com.example.picsumpaging.databinding.ItemImageBinding
import com.example.picsumpaging.paging.item.ImageData

class ImagePagingAdapter(

) : PagingDataAdapter<ImageData, ImagePagingAdapter.ImageViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position)!!) // placeholder 사용 x
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemImageBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_image, parent, false)
        return ImageViewHolder(binding)
    }

    class ImageViewHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageData: ImageData) {
            Log.e("url", ".${imageData.url}")
            Glide.with(binding.root)
                .load(imageData.url)
                .into(binding.ivImage)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ImageData>() {
            override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
                return oldItem == newItem
            }

        }
    }
}