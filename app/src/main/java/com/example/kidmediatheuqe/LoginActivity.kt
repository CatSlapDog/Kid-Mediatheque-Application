package com.example.kidmediatheuqe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var dbHelper: DBHelper
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Initialize the DBHelper
        dbHelper = DBHelper(this)
        val backButton = findViewById<ImageView>(R.id.backButton)

        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.ic_voice_tap_casual)

        buttonSubmit.setOnClickListener {
            // Play animation
            buttonSubmit.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            // Play sound
            mediaPlayer.start()

            // Process login
            processLogin()
        }

        backButton.setOnClickListener {
            // Intent to go back to WelcomeActivity
            val intent = Intent(this, WelcomeActivity::class.java)
            // Start the activity
            startActivity(intent)
            // Optionally finish the current activity so it's removed from the back stack
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources
        mediaPlayer.release()
    }

    private fun processLogin() {
        val name = editTextName.text.toString()
        val age = editTextAge.text.toString().toIntOrNull() ?: 0

        if (name.trim().isNotEmpty() && age in 1..99) {
            // Save to database
            val success = dbHelper.insertUser(name, age)
            if (success) {
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()

                // Save user details to SharedPreferences
                val sharedPref = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("USER_NAME", name)
                editor.putInt("USER_AGE", age)
                editor.apply()

                // After a successful login, redirect to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER_NAME", name) // Pass the user's name to MainActivity
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "error can not log in", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (name.trim().isEmpty()) {
                editTextName.error = "what is your name"
                editTextName.requestFocus()
            }
            if (age !in 1..99) {
                editTextAge.error = "age must be 1 to 99 years old"
                editTextAge.requestFocus()
            }
        }
    }
}