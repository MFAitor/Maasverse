package com.example.maasversetracker.data

import com.example.maasversetracker.R

fun getCoverResource(coverPath: String): Int {
    return when (coverPath) {
        "covers/a_court_of_thorns_and_roses.jpg" -> R.drawable.rosas_espinas
        "covers/a_court_of_mist_and_fury.jpg" -> R.drawable.niebla_furia
        "covers/a_court_of_wings_and_ruin.jpg" -> R.drawable.alas_ruina
        "covers/a_court_of_frost_and_starlight.jpg" -> R.drawable.hielo_estrellas
        "covers/a_court_of_silver_flames.jpg" -> R.drawable.llamas_plateadas
        "covers/the_assassins_blade.jpg" -> R.drawable.espada_asesina
        "covers/throne_of_glass.jpg" -> R.drawable.trono_cristal
        "covers/crown_of_midnight.jpg" -> R.drawable.corona_medianoche
        "covers/heir_of_fire.jpg" -> R.drawable.heredera_fuego
        "covers/queen_of_shadows.jpg" -> R.drawable.reina_sombras
        "covers/empire_of_storms.jpg" -> R.drawable.imperio_tormenta
        "covers/tower_of_dawn.jpg" -> R.drawable.torre_alba
        "covers/kingdom_of_ash.jpg" -> R.drawable.reino_cenizas
        "covers/house_of_earth_and_blood.jpg" -> R.drawable.casa_tierra_sangre
        "covers/house_of_sky_and_breath.jpg" -> R.drawable.casa_cielo_aliento
        "covers/house_of_flame_and_shadow.jpg" -> R.drawable.casa_llama_sombra
        else -> R.drawable.cover_placeholder
    }
}