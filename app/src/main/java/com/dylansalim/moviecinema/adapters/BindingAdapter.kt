package com.dylansalim.moviecinema.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dylansalim.moviecinema.R
import com.dylansalim.moviecinema.models.Genres
import com.dylansalim.moviecinema.models.Movie
import com.dylansalim.moviecinema.utils.DateTimeUtil
import com.dylansalim.moviecinema.utils.IMAGE_BASE_PATH
import java.util.*

// Binding adapter used to display images from URL using Glide
@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imgUrl: String?) {
    val url = "$IMAGE_BASE_PATH$imgUrl"

    Glide.with(imageView.context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
        )
        .into(imageView)
}

//@BindingAdapter("enable")
//fun enableTag(view:View){
//    view.setEnabled
//
//}

@BindingAdapter("genre")
fun TextView.setGenres(data: List<Genres>?) {
    data?.let {
        var genres = " "
        var show: String
        for (genre in data) {
            if (data.size == 1) {
                genre.name
            } else {
                if (genres == " ") {
                    show = genre.name
                    genres = show
                } else {
                    show = genres + ", " + genre.name
                    genres = show
                }
                text = show
            }
        }
    }
}


@BindingAdapter("runtime")
fun TextView.setRuntime(data: Int?) {
    data?.let {
        text = resources.getString(R.string.runtime, data)
    }
}

@BindingAdapter("vote_average")
fun TextView.setVoteAverage(data: Double?) {
    data?.let {
        text = data.toString()
    }
}


@BindingAdapter("language_code")
fun TextView.setLanguageCode(data: String?) {
    data?.let {
        text = Locale(data).getDisplayLanguage(Locale.ENGLISH)
    }
}

@BindingAdapter("date")
fun TextView.setDate(data: String?) {
    data?.let {
        text = DateTimeUtil().getAbbreviatedFromDateTime(
            data,
            "yyyy-mm-dd",
            "dd-MMM-yyyy"
        )
    }
}

@BindingAdapter("list_title")
fun TextView.setListTitle(data: Movie?) {
    data?.let {

        text = if (data.release_date != null && data.release_date.length > 4) {
            resources.getString(
                R.string.list_title,
                data.title,
                data.release_date.substring(0, 4)
            )
        } else {
            data.title
        }

    }
}

@BindingAdapter("popularity")
fun TextView.setPopularity(data: Double?) {
    data?.let {
        text = Math.round(data).toString()
    }
}
