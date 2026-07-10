package com.example.fragmentspractice.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.fragmentspractice.databinding.RatingFragmentBinding

/**
 * HU 4.0: Calificar la aplicación
 *
 * Como (Actor): App
 * Quiero (Acción): Que el jugador pueda calificar la aplicación
 * Para poder (Consecuencia): Medir y observar el nivel de satisfacción
 *                              que tiene la app en los usuarios
 *
 * Criterio 1: Como la app no está publicada en Google Play, se simula
 * el flujo de calificación (estrellas + comentario), tal como en la
 * pantalla de referencia de Nequi en la Play Store.
 */
class RatingFragment : Fragment() {

    private lateinit var binding: RatingFragmentBinding

    companion object {
        private const val PREFS_NAME = "calificacion_app_prefs"
        private const val KEY_RATING = "key_rating"
        private const val KEY_COMENTARIO = "key_comentario"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RatingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargarCalificacionGuardada()
        ratingBarAction()
        enviarButtonAction()
        backButtonAction()
    }

    private fun ratingBarAction() {
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            binding.btnEnviarCalificacion.isEnabled = rating > 0
            binding.tvFeedback.text = mensajeSegunCalificacion(rating)
        }
    }

    private fun enviarButtonAction() {
        binding.btnEnviarCalificacion.setOnClickListener {
            val rating = binding.ratingBar.rating
            val comentario = binding.etComentario.text.toString().trim()

            guardarCalificacion(rating, comentario)

            Toast.makeText(
                requireContext(),
                "¡Gracias por calificarnos con $rating estrellas!",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigateUp()
        }
    }

    private fun backButtonAction() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun mensajeSegunCalificacion(rating: Float): String {
        return when {
            rating <= 1f -> "Lamentamos tu experiencia. Cuéntanos qué podemos mejorar."
            rating <= 2f -> "Gracias por tu opinión, seguiremos mejorando."
            rating <= 3f -> "¡Gracias! Trabajaremos para superar tus expectativas."
            rating <= 4f -> "¡Nos alegra que te guste la app!"
            else -> "¡Excelente! Gracias por tu calificación de 5 estrellas."
        }
    }

    private fun guardarCalificacion(rating: Float, comentario: String) {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putFloat(KEY_RATING, rating)
            .putString(KEY_COMENTARIO, comentario)
            .apply()
    }

    private fun cargarCalificacionGuardada() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val ratingGuardado = prefs.getFloat(KEY_RATING, 0f)
        val comentarioGuardado = prefs.getString(KEY_COMENTARIO, "") ?: ""

        if (ratingGuardado > 0f) {
            binding.ratingBar.rating = ratingGuardado
            binding.etComentario.setText(comentarioGuardado)
            binding.btnEnviarCalificacion.isEnabled = true
            binding.tvFeedback.text = mensajeSegunCalificacion(ratingGuardado)
        }
    }
}