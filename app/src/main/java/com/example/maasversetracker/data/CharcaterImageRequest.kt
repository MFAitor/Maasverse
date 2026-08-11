package com.example.maasversetracker.data

import android.content.Context
import coil.request.ImageRequest

fun characterImageRequest(context: Context, imagePath: String): ImageRequest {
    return ImageRequest.Builder(context)
        .data("file:///android_asset/$imagePath")
        .crossfade(true)
        .build()
}