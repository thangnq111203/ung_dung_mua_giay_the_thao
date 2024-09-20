package com.fpoly.shoes_app.framework.presentation.ui.shoes.review

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.ItemReviewViewBinding
import com.fpoly.shoes_app.framework.domain.model.ReviewDetail
import javax.inject.Inject


private val diff = object : DiffUtil.ItemCallback<ReviewDetail>() {
    override fun areItemsTheSame(oldItem: ReviewDetail, newItem: ReviewDetail) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ReviewDetail, newItem: ReviewDetail) =
        oldItem == newItem
}

class ReviewAdapter @Inject constructor() : ListAdapter<ReviewDetail, ReviewViewHolder>(diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        ItemReviewViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ReviewViewHolder(
    private val binding: ItemReviewViewBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(review: ReviewDetail) {
        binding.run {
            tvName.text = review.userName?.fullName
            tvDescription.text = review.comment
            tvDescription.isVisible = review.comment.isNullOrBlank().not()
            tvRate.text = review.rateNumber.toString()
            tvDate.text = review.date
            val decodeDataImg =
                Base64.decode(
                    review.userName?.imageAccount?.`$binary`?.base64.toString(),
                    Base64.DEFAULT,
                )
            val image = BitmapFactory.decodeByteArray(decodeDataImg, 0, decodeDataImg.size)
            Glide.with(root.context)
                .load(image)
                .error(R.drawable.ic_user_image)
                .into(imgAvatar)
        }
    }
}
