package com.ikhsan.storydicoding.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikhsan.storydicoding.R
import com.ikhsan.storydicoding.data.domain.Result
import com.ikhsan.storydicoding.data.remote.response.StoryItem
import com.ikhsan.storydicoding.databinding.ActivityMainBinding
import com.ikhsan.storydicoding.ui.adapter.StoryAdapter
import com.ikhsan.storydicoding.ui.add.UploadStoryActivity
import com.ikhsan.storydicoding.ui.auth.AuthenticActivity
import com.ikhsan.storydicoding.ui.detail.DetailActivity
import com.ikhsan.storydicoding.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoryAdapter()

        mainViewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]

        setSupportActionBar(binding.toolbar)

        getRecycle()
        onClickCallback()
        setUpViewModel()
        addStory()
        setRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.btn_logout -> {
                mainViewModel.logout()

                val intent = Intent(this, AuthenticActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            mainViewModel.refreshData()
        }
    }

    private fun getRecycle() {
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }

    private fun onClickCallback() {
        storyAdapter.setOnItemCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                startActivity(intent)
            }

        })
    }

    private fun setUpViewModel() {
        mainViewModel.getAllStory().observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    binding.swipeRefresh.isRefreshing = false
                }
                is Result.Loading -> { showLoading(true) }
                is Result.Success -> {
                    showLoading(false)
                    storyAdapter.submitList(result.data)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun addStory() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadStoryActivity::class.java)
            launcherCamera.launch(intent)
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            mainViewModel.refreshData()
        }
    }

    private fun showLoading(isLoading: Boolean) { binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }
}