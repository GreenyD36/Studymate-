package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class StudyMateAudio(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.ENGLISH
        }
    }

    fun speak(text: String, languageCode: String = "en") {
        if (!isInitialized || tts == null) return

        val locale = when (languageCode) {
            "fr" -> Locale.FRENCH
            "zh" -> Locale.CHINESE
            "ko" -> Locale.KOREAN
            "es" -> Locale.forLanguageTag("es-ES")
            "pt" -> Locale.forLanguageTag("pt-BR")
            "rw" -> Locale.forLanguageTag("rw-RW") // fallback if device has Kinyarwanda TTS, else default
            else -> Locale.ENGLISH
        }

        try {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // If specific dialect not available, try standard
                tts?.language = Locale.getDefault()
            }
        } catch (_: Exception) {
            tts?.language = Locale.ENGLISH
        }

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "StudyMateSpeech")
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
