package com.ikhsan.storydicoding.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ikhsan.storydicoding.R
import com.ikhsan.storydicoding.ui.auth.AuthenticActivity
import com.ikhsan.storydicoding.ui.main.MainActivity
import com.ikhsan.storydicoding.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RoutingActivity : AppCompatActivity() {

    private lateinit var routingViewModel: RoutingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routing)

        routingViewModel = ViewModelProvider(this, ViewModelFactory(this))[RoutingViewModel::class.java]

        lifecycleScope.launch {
            delay(3000)
            routingViewModel.isLogin().observe(this@RoutingActivity) { isLogin ->
                if (isLogin) {
                    val intent = Intent(this@RoutingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@RoutingActivity, AuthenticActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}