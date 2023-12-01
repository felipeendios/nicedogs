package com.challenge.nicedogs.ui.utils

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.challenge.nicedogs.R

private const val STROKE_WIDTH = 10f
private const val CENTER_RADIUS = 50f

fun ImageView.loadImage(url: String?) {
    val options = RequestOptions()
        .placeholder(createProgressDrawable(context))
        .error(R.drawable.baseline_warning_24)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

private fun createProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = STROKE_WIDTH
        centerRadius = CENTER_RADIUS
        start()
    }
}
