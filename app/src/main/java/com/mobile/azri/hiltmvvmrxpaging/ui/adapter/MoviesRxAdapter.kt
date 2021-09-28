package com.mobile.azri.hiltmvvmrxpaging.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.azri.hiltmvvmrxpaging.R
import com.mobile.azri.hiltmvvmrxpaging.databinding.MovieGridItemBinding
import com.mobile.azri.hiltmvvmrxpaging.model.Movies

class MoviesRxAdapter : PagingDataAdapter<Movies.Movie,MoviesRxAdapter.MovieRxViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieRxViewHolder {
        return MovieRxViewHolder.create(parent)
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
   }
}