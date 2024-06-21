package com.example.trashify.ui.adaptasi

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.trashify.data.reponse.PredictionResponse
import com.example.trashify.data.reponse.TimeStamp
import com.example.trashify.databinding.ItemStoryBinding
import com.example.trashify.ui.detail.DetailStoryActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StoryAdapter : ListAdapter<PredictionResponse, StoryAdapter.MyViewHolder>(CALLBACK) {
    class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(prediction: PredictionResponse) {
            binding.apply {
                photoUrl.load(prediction.imageUrl)
                textDescriptions.text = prediction.description
                textLabel.text = prediction.label
                textAction.text = prediction.action
                textPriceRange.text = prediction.priceRange

                val maxProbability = prediction.probabilities.maxOrNull()?.let {
                    String.format("%.2f%%", it * 100)
                } ?: "N/A"
                textProbability.text = "Probability: $maxProbability"

                val timestamp = convertTimestampToReadable(prediction.timeStamp)
                textTimestamp.text = timestamp

                root.setOnClickListener {
                    Intent(root.context, DetailStoryActivity::class.java).also {
                        it.putExtra(DetailStoryActivity.EXTRA_PHOTO_URL, prediction.imageUrl)
                        it.putExtra(DetailStoryActivity.EXTRA_DESC, prediction.description)
                        it.putExtra(DetailStoryActivity.EXTRA_LABEL, prediction.label)
                        it.putExtra(DetailStoryActivity.EXTRA_ACTION, prediction.action)
                        it.putExtra(DetailStoryActivity.EXTRA_PRICE, prediction.priceRange)
                        it.putExtra(DetailStoryActivity.EXTRA_PROBABILITY, maxProbability)
                        it.putExtra(DetailStoryActivity.EXTRA_TIMESTAMP, timestamp)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(photoUrl, "photo"),
                                Pair(textDescriptions, "description")
                            )

                        root.context.startActivity(it, optionsCompat.toBundle())
                    }
                }
            }
        }

        private fun convertTimestampToReadable(timeStamp: TimeStamp): String {
            val date = Date(timeStamp.seconds * 1000)
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return format.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<PredictionResponse>() {
            override fun areItemsTheSame(oldItem: PredictionResponse, newItem: PredictionResponse): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: PredictionResponse, newItem: PredictionResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}
