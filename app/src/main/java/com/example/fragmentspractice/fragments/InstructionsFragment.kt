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
 * User Story 5.0: Game Instructions
 *
 * As a: Player
 * I want to: Learn the rules of the "Spin the Bottle" game
 * So that I can: Understand how to play correctly
 *
 * Acceptance Criteria:
 * 1. If the home background music is ON, it pauses when entering this screen.
 * 2. Dark gray background.
 * 3. Custom toolbar titled "Game Rules" with a back arrow that returns
 *    to the home screen and restores the music if it was ON.
 * 4-7. Titles and descriptions for "How to Play?" and "Who Wins?"
 * 8. Trophy animation with an appearance/bounce effect.
 */
class InstructionsFragment : Fragment() {

    private lateinit var binding: InstructionsFragmentBinding

    // Stores whether the music was ON before entering this screen
    private var wasMusicOn = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InstructionsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pauseMusicIfPlaying()
        setupBackButton()
        animateTrophy()
    }

    private fun pauseMusicIfPlaying() {
        wasMusicOn = MusicPlayerManager.isOn
        if (wasMusicOn) {
            MusicPlayerManager.pause()
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            if (wasMusicOn) {
                MusicPlayerManager.resume()
            }
            findNavController().navigateUp()
        }
    }

    private fun animateTrophy() {
        binding.tvTrophy.scaleX = 0f
        binding.tvTrophy.scaleY = 0f

        val scaleX = ObjectAnimator.ofFloat(binding.tvTrophy, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.tvTrophy, View.SCALE_Y, 0f, 1f)

        scaleX.duration = 700
        scaleY.duration = 700
        scaleX.interpolator = OvershootInterpolator()
        scaleY.interpolator = OvershootInterpolator()

        scaleX.start()
        scaleY.start()
    }
}