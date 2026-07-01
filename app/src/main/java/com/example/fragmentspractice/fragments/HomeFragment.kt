package com.example.fragmentspractice.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fragmentspractice.R
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.fragmentspractice.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private var mediaPlayer: MediaPlayer? = null
    var isMuted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMusic()
        setupToolbar()
    }

    private fun setupMusic() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.blue_jazz_music).apply {
            isLooping = true
            start()
        }
        isMuted = true
    }

    private fun starButtonAction(){
        val startButton = binding.contentToolbar.toolBarStartButton
        startButton.setOnClickListener {
            Toast.makeText(requireContext(), "Star Pressed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_ratingFragment)
        }
    }

    private fun controllerButtonAction(){
        val controllerButton = binding.contentToolbar.toolBarControllerButton
        controllerButton.setOnClickListener {
            Toast.makeText(requireContext(), "Instructions Pressed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_instructionsFragment)
        }
    }

    private fun addButtonAction(){
        val addButton = binding.contentToolbar.toolBarAddButtom
        addButton.setOnClickListener {
            Toast.makeText(requireContext(), "Challenge Management Pressed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_challengeManagementFragment)
        }
    }

    private fun shareButtonAction(){
        val shareButton = binding.contentToolbar.toolBarShareButton
        shareButton.setOnClickListener {
            Toast.makeText(requireContext(), "Share Pressed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_shareFragment)
        }
    }

    private fun volumeButtonAction(){
        val volumeButton = binding.contentToolbar.toolBarVolumeButton
        volumeButton.setOnClickListener {
            if (isMuted) {
                volumeButton.setImageResource(R.drawable.mute)
                Toast.makeText(requireContext(), "Sonido pausado", Toast.LENGTH_SHORT).show()
                mediaPlayer?.pause()
            }else {
                volumeButton.setImageResource(R.drawable.volume)
                Toast.makeText(requireContext(), "Sonido pausado", Toast.LENGTH_SHORT).show()
                mediaPlayer?.start()
            }
            isMuted = !isMuted
        }
    }
    private fun setupToolbar() {
        starButtonAction()

        volumeButtonAction()

        controllerButtonAction()

        addButtonAction()

        shareButtonAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
