package com.example.picsumpaging.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.picsumpaging.R
import com.example.picsumpaging.data.Repository
import com.example.picsumpaging.databinding.ItemImageBinding
import com.example.picsumpaging.paging.item.ImageData
import javax.inject.Inject

class ImagePagingAdapter @Inject constructor(
    private val removeImage: (String) -> Unit
) : PagingDataAdapter<ImageData, ImagePagingAdapter.ImageViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position)!!) // placeholder 사용 x
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemImageBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_image, parent, false)
        return ImageViewHolder(binding, removeImage)
    }

    class ImageViewHolder(
        private val binding: ItemImageBinding,
        private val removeImage: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var imageData: ImageData

        init {
            initClickListener()
        }

        fun bind(imageData: ImageData) {
            this.imageData = imageData
            // 사진 불러오는데 시간이 너무 오래 걸려서 뺴버림
//            Glide.with(binding.root)
//                .load(imageData.url)
//                .into(binding.ivImage)
            binding.tvUrl.text = imageData.url
        }

        private fun initClickListener() {
            binding.ivAdd.setOnClickListener {
                Toast.makeText(binding.root.context, "추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
            binding.ivTrash.setOnClickListener {
                removeImage(imageData.id)
                Toast.makeText(binding.root.context, "삭제되었습니다..", Toast.LENGTH_SHORT).show()
            }
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