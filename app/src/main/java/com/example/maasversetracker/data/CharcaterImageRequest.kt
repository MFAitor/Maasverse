package com.example.maasversetracker.data

import android.content.Context
import coil.request.ImageRequest

//Peticion para cargar la imagen desde los assets
fun characterImageRequest(context: Context, imagePath: String): ImageRequest {
    return ImageRequest.Builder(context)
        .data("file:///android_asset/$imagePath")
        //Pequeña animacion al cargarla
        .crossfade(true)
        .build()
}