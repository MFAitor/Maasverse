package com.example.maasversetracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.maasversetracker.R
import com.example.maasversetracker.R.font


val Sinera = FontFamily(
    Font(R.font.sinera, FontWeight.Normal)
)

val Typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = Sinera,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Sinera,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Sinera,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
)