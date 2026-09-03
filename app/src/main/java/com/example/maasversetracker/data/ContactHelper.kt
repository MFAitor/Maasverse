package com.example.maasversetracker.data

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openContactEmail(context: Context) {
    val email = "acinofelet@hotmail.es"
    val subject = Uri.encode("Maas Tracker – sugerencia o error")
    val body = Uri.encode(
        "Describe aquí el error o la información que quieres dejar:\n\n\n" +
                "Quieres dejar alguna sugerencia a cerca de la app?\n\n\n" +
                "En caso de haber surgido algún error descríbelo a continuación:"
    )
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email?subject=$subject&body=$body")
    }
    context.startActivity(Intent.createChooser(intent, "Enviar correo"))
}