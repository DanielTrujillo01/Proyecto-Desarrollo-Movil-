package com.example.fragmentspractice.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fragmentspractice.databinding.RatingFragmentBinding
import com.example.fragmentspractice.utils.BrandedToast

/**
 * User Story 4.0: Rate the Application
 *
 * As a: App
 * I want: Players to be able to rate the application
 * So that: User satisfaction can be measured and evaluated.
 *
 * Acceptance Criteria:
 * Since the app is not published on Google Play, the rating flow
 * (stars + comment) is simulated, similar to the Play Store rating screen.
 */
class RatingFragment : Fragment() {

    private lateinit var binding: RatingFragmentBinding

    companion object {
        private const val PREFS_NAME = "app_rating_prefs"
        private const val KEY_RATING = "key_rating"
        private const val KEY_COMMENT = "key_comment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RatingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSavedRating()
        setupRatingBar()
        setupSubmitButton()
        setupBackButton()
    }

    private fun setupRatingBar() {
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            binding.btnSubmitRating.isEnabled = rating > 0
            binding.tvFeedbackMessage.text = getFeedbackMessage(rating)
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmitRating.setOnClickListener {
            val rating = binding.ratingBar.rating
            val comment = binding.etComment.text.toString().trim()

            saveRating(rating, comment)

            BrandedToast.show(
                requireContext(),
                "¡Gracias por calificarnos con $rating estrellas!"
            )

            findNavController().navigateUp()
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getFeedbackMessage(rating: Float): String {
        return when {
            rating <= 1f -> "Lamentamos tu experiencia. Cuéntanos qué podemos mejorar."
            rating <= 2f -> "Gracias por tu opinión, seguiremos mejorando."
            rating <= 3f -> "¡Gracias! Trabajaremos para superar tus expectativas."
            rating <= 4f -> "¡Nos alegra que te guste la app!"
            else -> "¡Excelente! Gracias por tu calificación de 5 estrellas."
        }
    }

    private fun saveRating(rating: Float, comment: String) {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putFloat(KEY_RATING, rating)
            .putString(KEY_COMMENT, comment)
            .apply()
    }

    private fun loadSavedRating() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val savedRating = prefs.getFloat(KEY_RATING, 0f)
        val savedComment = prefs.getString(KEY_COMMENT, "") ?: ""

        if (savedRating > 0f) {
            binding.ratingBar.rating = savedRating
            binding.etComment.setText(savedComment)
            binding.btnSubmitRating.isEnabled = true
            binding.tvFeedbackMessage.text = getFeedbackMessage(savedRating)
        }
    }
}