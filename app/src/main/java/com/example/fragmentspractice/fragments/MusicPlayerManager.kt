package com.example.fragmentspractice.fragments

import android.content.Context
import android.media.MediaPlayer
import com.example.fragmentspractice.R

/**
 * Mantiene un único MediaPlayer para la música de fondo, para que
 * pueda pausarse/reanudarse desde cualquier fragment (ej: al entrar
 * y salir de InstructionsFragment) sin perder el estado.
 */
object MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null
    var isOn: Boolean = false
        private set

    fun start(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.blue_jazz_music).apply {
                isLooping = true
            }
        }
        mediaPlayer?.start()
        isOn = true
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
        isOn = false
    }

    fun resume() {
        mediaPlayer?.start()
        isOn = true
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isOn = false
    }
}