package com.mobile.azri.hiltmvvmrxpaging.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.azri.hiltmvvmrxpaging.R
import com.mobile.azri.hiltmvvmrxpaging.databinding.MovieGridItemBinding
import com.mobile.azri.hiltmvvmrxpaging.model.Movies
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MoviesRxAdapter @Inject constructor() : PagingDataAdapter<Movies.Movie,MoviesRxAdapter.MovieRxViewHolder>(COMPARATOR) {

//    private lateinit var bindingRow : MovieGridItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieRxViewHolder {
//        bindingRow = MovieGridItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieRxViewHolder.create(parent)

        //cara 2 untuk create viewHolder
//        return MovieRxViewHolder(bindingRow)
    }

    override fun onBindViewHolder(holder: MovieRxViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class MovieRxViewHolder(private val binding: MovieGridItemBinding ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("CheckResult")
        fun bind(data:Movies.Movie){
            Glide.with(binding.root)
                .load(data.poster?.medium.toString())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.poster)

            binding.tvTitle.text = data.title
            binding.tvReleaseDate.text = data.releaseDate.toString()
            Log.d(TAG, "bind: title -> ${data.title} ")
        }

        companion object{
            fun create(parent: ViewGroup) : MovieRxViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_grid_item,parent,false)

                val binding = MovieGridItemBinding.bind(view)

                return MovieRxViewHolder(binding)
            }
        }
    }

   companion object{
       private val COMPARATOR = object : DiffUtil.ItemCallback<Movies.Movie>() {
           override fun areItemsTheSame(oldItem: Movies.Movie, newItem: Movies.Movie): Boolean {
               return oldItem.movieId == newItem.movieId
           }

           override fun areContentsTheSame(oldItem: Movies.Movie, newItem: Movies.Movie): Boolean {
              return oldItem == newItem
           }

       }
       private const val TAG = "MoviesRxAdapter"
   }
}