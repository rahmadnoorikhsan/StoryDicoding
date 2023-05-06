package com.ikhsan.storydicoding.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikhsan.storydicoding.R
import com.ikhsan.storydicoding.data.local.entity.StoryEntity
import com.ikhsan.storydicoding.databinding.ActivityMainBinding
import com.ikhsan.storydicoding.ui.adapter.StoryAdapter
import com.ikhsan.storydicoding.ui.add.UploadStoryActivity
import com.ikhsan.storydicoding.ui.auth.AuthenticActivity
import com.ikhsan.storydicoding.ui.detail.DetailActivity
import com.ikhsan.storydicoding.ui.maps.MapsActivity
import com.ikhsan.storydicoding.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]

        setSupportActionBar(binding.toolbar)

        getRecycle()
        onClickCallback()
        addStory()
        setRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.btn_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.btn_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
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
        storyAdapter = StoryAdapter()
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
        mainViewModel.story.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    private fun onClickCallback() {
        storyAdapter.setOnItemCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryEntity) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                startActivity(intent)
            }

        })
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

//    private fun showLoading(isLoading: Boolean) { binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }
}