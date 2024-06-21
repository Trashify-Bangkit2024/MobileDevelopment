package com.example.trashify.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.trashify.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var photoUrl: String
    private lateinit var description: String
    private lateinit var label: String
    private lateinit var action: String
    private lateinit var priceRange: String
    private lateinit var probability: String
    private lateinit var timestamp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        deklarasiExtra()
        setupAction()
        setupButtons()
    }

    private fun deklarasiExtra() {
        val def_PhotoUrl = "N/A"
        val def_Description = "N/A"
        val def_Label = "N/A"
        val def_Action = "N/A"
        val def_PriceRange = "N/A"
        val def_Probability = "N/A"
        val def_Timestamp = "N/A"

        photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL) ?: def_PhotoUrl
        description = intent.getStringExtra(EXTRA_DESC) ?: def_Description
        label = intent.getStringExtra(EXTRA_LABEL) ?: def_Label
        action = intent.getStringExtra(EXTRA_ACTION) ?: def_Action
        priceRange = intent.getStringExtra(EXTRA_PRICE) ?: def_PriceRange
        probability = intent.getStringExtra(EXTRA_PROBABILITY) ?: def_Probability
        timestamp = intent.getStringExtra(EXTRA_TIMESTAMP) ?: def_Timestamp
    }

    private fun setupAction() {
        binding.apply {
            photoUrlImageView.load(photoUrl)
            textDescriptions.text = description
            textLabel.text = label
            textAction.text = action
            textPriceRange.text = priceRange
            textProbability.text = probability
            textTimestamp.text = timestamp
        }
    }

    private fun setupButtons() {
        binding.buttonMaps1Link.setOnClickListener {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.app.goo.gl/sNmkEvC9riW6yH2j9?g_st=iw"))
            startActivity(webIntent)
        }

        binding.buttonMaps2Link.setOnClickListener {
            val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.app.goo.gl/nqZwctBm7pZwtqpA6?g_st=iw"))
            startActivity(mapsIntent)
        }

        binding.buttonMaps3Link.setOnClickListener {
            val anotherIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.app.goo.gl/bwrZ1YyN4qRB52Cb9?g_st=iw"))
            startActivity(anotherIntent)
        }
    }

    companion object {
        const val EXTRA_PHOTO_URL = "extra_photourl"
        const val EXTRA_DESC = "extra_description"
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_ACTION = "extra_action"
        const val EXTRA_PRICE = "extra_price"
        const val EXTRA_PROBABILITY = "extra_probability"
        const val EXTRA_TIMESTAMP = "extra_timestamp"
    }
}
