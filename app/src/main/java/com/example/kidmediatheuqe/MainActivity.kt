package com.example.kidmediatheuqe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var userName: String? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbarTitle = findViewById<TextView>(R.id.welcomeTitle)

        // Check if userName is saved in the savedInstanceState
        userName = savedInstanceState?.getString("USER_NAME")
            ?: intent.getStringExtra("USER_NAME")
                    ?: getUserNameFromDatabase() // Use DBHelper to fetch username from the database

        if (userName.isNullOrEmpty()) {
            // If userName is null or blank, redirect to WelcomeActivity
            val welcomeIntent = Intent(this, WelcomeActivity::class.java)
            startActivity(welcomeIntent)
            finish() // Finish MainActivity so the user can't return to it with the back button
            return // Stop further execution of this function
        }

        // Set the user's name to the TextView
        toolbarTitle.text = getString(R.string.welcome_user, userName)

        // Start background music
        mediaPlayer = MediaPlayer.create(this, R.raw.ic_voice_background_music)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        val imageOne = findViewById<ImageView>(R.id.imageOne)
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)

        // Load the sound effect
        val soundEffect = MediaPlayer.create(this, R.raw.ic_voice_tap_casual)

        imageOne.setOnClickListener {
            soundEffect.start() // Play the sound effect

            it.startAnimation(scaleAnimation)
            scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Animation started
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Animation ended, start the activity
                    val intent = Intent(this@MainActivity, AlphabetActivity::class.java).apply {
                        val alphabetA = AlphabetModel(
                            letter = "A",
                            characterImageResource = R.drawable.ic_alphabet_a,
                            animalImageResource = R.drawable.ic_animal_a,
                            translation = "Alpaca"
                        )
                        putExtra("ALPHABET_MODEL", alphabetA)
                    }
                    startActivity(intent)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Animation repeated
                }
            })
        }
    }

    // This method will be called when the activity is paused
    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause() // Pause the background music
    }

    // This method will be called when the activity is resumed
    override fun onResume() {
        super.onResume()
        mediaPlayer?.start() // Resume playing the background music
    }

    // This method will be called when the activity may be temporarily destroyed
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the user name
        outState.putString("USER_NAME", userName)
    }

    // Release MediaPlayer resources when the activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getUserNameFromDatabase(): String {
        val dbHelper = DBHelper(this)
        return dbHelper.getUserName()
    }
}

