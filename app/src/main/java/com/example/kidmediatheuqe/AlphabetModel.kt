package com.example.kidmediatheuqe

import java.io.Serializable

data class AlphabetModel (
    val letter: String,
    val characterImageResource: Int,
    val animalImageResource: Int,
    val translation: String
) : Serializable