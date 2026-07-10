package com.example.fragmentspractice.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.fragmentspractice.databinding.InstructionsFragmentBinding

/**
 * HU 5.0: Instrucciones del juego
 *
 * Como (Actor): Jugador
 * Quiero (Acción): Conocer las instrucciones del juego "pico botella"
 * Para poder (Consecuencia): Entender y jugar adecuadamente
 *
 * Criterio 1: Si el audio de fondo del home está en ON, al entrar se pausa.
 * Criterio 2: Fondo gris oscuro.
 * Criterio 3: Toolbar personalizada "Reglas del Juego" con flecha atrás,
 *             que regresa al home y restablece el audio si estaba en ON.
 * Criterio 4-7: Títulos y descripciones de "¿Cómo se juega?" y "¿Quién gana?"
 * Criterio 8: Animación de triunfo (trofeo con efecto de aparición/rebote).
 */
class InstructionsFragment : Fragment() {

    private lateinit var binding: InstructionsFragmentBinding

    // Guarda si el audio estaba en ON antes de entrar a esta pantalla
    private var audioEstabaOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InstructionsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pausarAudioSiEstabaEncendido()
        backButtonAction()
        animarTrofeo()
    }

    private fun pausarAudioSiEstabaEncendido() {
        audioEstabaOn = MusicPlayerManager.isOn
        if (audioEstabaOn) {
            MusicPlayerManager.pause()
        }
    }

    private fun backButtonAction() {
        binding.btnBack.setOnClickListener {
            if (audioEstabaOn) {
                MusicPlayerManager.resume()
            }
            findNavController().navigateUp()
        }
    }

    private fun animarTrofeo() {
        binding.tvTrofeo.scaleX = 0f
        binding.tvTrofeo.scaleY = 0f

        val scaleX = ObjectAnimator.ofFloat(binding.tvTrofeo, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.tvTrofeo, View.SCALE_Y, 0f, 1f)

        scaleX.duration = 700
        scaleY.duration = 700
        scaleX.interpolator = OvershootInterpolator()
        scaleY.interpolator = OvershootInterpolator()

        scaleX.start()
        scaleY.start()
    }
}