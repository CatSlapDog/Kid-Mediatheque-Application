package com.example.kidmediatheuqe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kidmediatheuqe.databinding.ActivityMenuBinding


class MenuActivity: AppCompatActivity() {
    private val binding: ActivityMenuBinding by lazy {
        ActivityMenuBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}