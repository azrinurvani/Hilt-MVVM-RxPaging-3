package com.mobile.azri.hiltmvvmrxpaging.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.azri.hiltmvvmrxpaging.R
import com.mobile.azri.hiltmvvmrxpaging.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
//    private val viewModel: GetMoviesRxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}