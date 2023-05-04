package com.ikhsan.storydicoding.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ikhsan.storydicoding.R
import com.ikhsan.storydicoding.data.domain.Result
import com.ikhsan.storydicoding.databinding.ActivityDetailBinding
import com.ikhsan.storydicoding.util.withDateFormat
import com.ikhsan.storydicoding.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this, ViewModelFactory(this))[DetailViewModel::class.java]

        val storyId = intent.getStringExtra(EXTRA_ID)
        storyId?.run(detailViewModel::setStoryId)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel.detailStory.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> { showLoading(true) }
                is Result.Success -> {
                    showLoading(false)
                    val item = result.data
                    binding.apply {
                        Glide.with(this@DetailActivity)
                            .load(item.photoUrl)
                            .into(binding.ivDetailPhoto)
                        toolbar.title = item.name
                        toolbar.subtitle = item.createdAt?.withDateFormat()
                        tvDescription.text = item.description
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) { binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }

    companion object {
        const val EXTRA_ID = "id"
    }
}