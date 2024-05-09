package com.example.kidmediatheuqe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val buttonGetStarted = findViewById<Button>(R.id.buttonGetStarted)
        buttonGetStarted.setOnClickListener {
            // Load animation
            val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.button_click_animation)
            // Start animation on button
            buttonGetStarted.startAnimation(animation)

            // Play sound
            playSound()

            // Intent to go to LoginActivity after a delay
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Initialize your views here if necessary
        // Example: val buttonGetStarted = findViewById<Button>(R.id.buttonGetStarted)

        // Set up any onClickListeners or other initialization logic
    }

    private fun playSound() {
        mediaPlayer?.release() // Release any existing MediaPlayer resource
        mediaPlayer = MediaPlayer.create(this, R.raw.ic_voice_tap_casual)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources
        mediaPlayer?.release()
        mediaPlayer = null
    }
}