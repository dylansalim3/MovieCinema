package com.dylansalim.moviecinema.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dylansalim.moviecinema.databinding.ItemListBinding
import com.dylansalim.moviecinema.models.Movie

class MovieListAdapter(
    private val onOnClickListener: OnClickListener,
    private var movies: MutableList<Movie>
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    fun appendMovies(movies: List<Movie>) {
        val handler = Handler(Looper.getMainLooper())

        val r = Runnable {
            val curSize = this.movies.size
            this.movies.addAll(movies)
            notifyItemRangeInserted(curSize, this.movies.size - 1)
        }

        handler.post(r)
    }

    fun clearMovies() {
        this.movies.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]
        holder.itemView.setOnClickListener {
            onOnClickListener.onClick(item)
        }
        holder.bind(item)
    }

    class MovieViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movie = movie
        }
    }

    class OnClickListener(val clickListener: (movie: Movie) -> Unit) {
        fun onClick(movie: Movie) = clickListener(movie)
    }


}