package com.mobile.azri.hiltmvvmrxpaging.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.azri.hiltmvvmrxpaging.databinding.ActivityMainBinding
import com.mobile.azri.hiltmvvmrxpaging.ui.adapter.MoviesRxAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel: GetMoviesRxViewModel by viewModels()

    @Inject
    lateinit var movieAdapter : MoviesRxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeObservers()
        initAdapter()
    }

    private fun subscribeObservers(){
        viewModel.moviesLiveData.observe(this, Observer {
            Log.d(TAG, "subscribeObservers: $it")
            movieAdapter.submitData(lifecycle,it)
        })
    }

    private fun initAdapter(){
        binding.rvMain.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            adapter = movieAdapter
        }
    }
    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disposeComposite()
    }
}