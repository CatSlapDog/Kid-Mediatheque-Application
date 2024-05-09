package com.example.kidmediatheuqe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AlphabetActivity : AppCompatActivity() {
    private lateinit var characterImage: ImageView
    private lateinit var animalImage: ImageView
    private lateinit var animalName: TextView
    private lateinit var homeText: TextView
    private lateinit var nextText: TextView
    private lateinit var backText: TextView
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var scaleAnimation: Animation
    private lateinit var currentModel: AlphabetModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alphabet)

        //initialize variables views in activity_alphabet.xml find by id
        characterImage = findViewById(R.id.characterImage)
        animalImage = findViewById(R.id.animalImage)
        animalName = findViewById(R.id.animalName)
        homeText = findViewById(R.id.homeText)
        nextText = findViewById(R.id.nextText)
        backText = findViewById(R.id.backText)

        //load animation
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)

        //load the initial or default model
        currentModel = intent.getSerializableExtra("ALPHABET_MODEL") as? AlphabetModel ?: getDefaultAlphabetModel()
        updateUI(currentModel)

        characterImage.setOnClickListener {
            // Apply animation
            it.startAnimation(scaleAnimation)
            playSoundForLetter(currentModel.letter)
        } //close characterImage.setOnClick

        animalImage.setOnClickListener {
            // Apply animation
            it.startAnimation(scaleAnimation)
            playAnimalSoundForLetter(currentModel.letter)
        } //close animalImage.setOnClick

        homeText.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(this)
            }
        } //close homeText.setOnClick

        nextText.setOnClickListener {
            navigateToLetter(isNext = true)
        } //close nextText.setOnClick

        backText.setOnClickListener {
            navigateToLetter(isNext = false)
        } //close backText.setOnClick

    } //close fun onCreate

    private fun navigateToLetter(isNext: Boolean) {
        val nextLetter = if (isNext) getNextLetter(currentModel.letter) else getPreviousLetter(currentModel.letter)
        getNextAlphabetModel(nextLetter)?.let { model ->
            currentModel = model
            updateUI(currentModel)
        }
    } //close fun navigateToLetter
    private fun getPreviousLetter(currentLetter: String): String {
        return if (currentLetter[0] > 'A') (currentLetter[0] - 1).toString() else "Z"
    } //close getPreviousLetter

    private fun getNextLetter(currentLetter: String): String {
        return if (currentLetter[0] < 'Z') (currentLetter[0] + 1).toString() else "A"
    } //close getNextLetter

    private fun getDefaultAlphabetModel(): AlphabetModel {
        return getNextAlphabetModel("A") ?: throw IllegalStateException("Default model not found")
    } //close getDefaultAlphabetModel

    private fun getNextAlphabetModel(letter: String): AlphabetModel? {
        val characterImageResId = resources.getIdentifier("ic_alphabet_$letter".toLowerCase(), "drawable", packageName)
        val animalImageResId = resources.getIdentifier("ic_animal_$letter".toLowerCase(), "drawable", packageName)
        val translationResName = "translation_$letter".toLowerCase()
        val translationResId = resources.getIdentifier(translationResName, "string", packageName)
        val translation = if (translationResId != 0) getString(translationResId) else "Translation missing for $letter"

        return if (characterImageResId != 0 && animalImageResId != 0) {
            AlphabetModel(letter, characterImageResId, animalImageResId, translation)
        } else null
    } //close getNextAlphabetModel

    private fun updateUI(model: AlphabetModel) {
        characterImage.setImageResource(model.characterImageResource)
        animalImage.setImageResource(model.animalImageResource)
        animalName.text = model.translation
    } //close fun updateUI

    private fun playSoundForLetter(letter: String) {
        mediaPlayer?.release()
        val resourceName = "ic_voice_alphabet_${letter.lowercase()}"
        val resourceId = resources.getIdentifier(resourceName, "raw", packageName)
        if (resourceId != 0) {
            mediaPlayer = MediaPlayer.create(this, resourceId).apply {
                setOnCompletionListener { it.release() }
                setOnErrorListener { mp, _, _ -> mp.release(); true }
                start()
            }
        }
    } //close fun playSoundForLetter

    private fun playAnimalSoundForLetter(letter: String) {
        mediaPlayer?.release()
        val resourceName = "ic_voice_animal_${letter.lowercase()}"
        val resourceId = resources.getIdentifier(resourceName, "raw", packageName)
        if (resourceId != 0) {
            mediaPlayer = MediaPlayer.create(this, resourceId).apply {
                setOnCompletionListener { it.release() }
                setOnErrorListener { mp, _, _ -> mp.release(); true }
                start()
            }
        }
    } //close fun playAnimalSoundForLetter

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    } //close fun onDestroy


}