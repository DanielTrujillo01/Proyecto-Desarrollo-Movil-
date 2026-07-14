package com.example.fragmentspractice.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fragmentspractice.R
import com.example.fragmentspractice.databinding.HomeFragmentBinding
import com.example.fragmentspractice.utils.BrandedToast
import com.example.fragmentspractice.viewmodel.ChallengeViewModel
import com.example.fragmentspractice.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()
    private val challengeViewModel: ChallengeViewModel by viewModels()

    private var spinMediaPlayer: MediaPlayer? = null
    private var wasMusicOn = false

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
        setupSpinButton()
        observeViewModel()
        
        // Sincronizar rotación inicial desde el ViewModel (Criterio 4)
        binding.homeBottle.rotation = viewModel.currentAngle
    }

    override fun onResume() {
        super.onResume()
        actualizarIconoVolumen()
        // Si el botón es visible, reiniciar la animación de parpadeo
        if (binding.homeButton.isVisible) {
            setupBlinkAnimation()
        }
    }

    private fun setupMusic() {
        MusicPlayerManager.start(requireContext())
        actualizarIconoVolumen()
    }

    private fun setupSpinButton() {
        binding.homeButton.setOnClickListener {
            startSpin()
        }
        setupBlinkAnimation()
    }

    private fun setupBlinkAnimation() {
        val anim = AlphaAnimation(0.4f, 1.0f).apply {
            duration = 800
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        binding.homeButton.startAnimation(anim)
    }

    private fun startSpin() {
        // Bloquear multi-clicks y ocultar controles (Criterio 7)
        binding.homeButton.clearAnimation()
        binding.homeButton.isVisible = false
        binding.tvPressMe.isVisible = false

        // Gestión de audio de fondo (Criterio 8)
        wasMusicOn = MusicPlayerManager.isOn
        if (wasMusicOn) {
            MusicPlayerManager.pause()
        }
        playSpinSound() // Criterio 2

        // Parámetros de giro aleatorio (Criterio 1 y 3)
        val fromDegrees = viewModel.currentAngle
        val randomVueltas = (3..5).random()
        val extraDegrees = (0..359).random().toFloat()
        val toDegrees = fromDegrees + (randomVueltas * 360) + extraDegrees
        val spinDuration = (3000..5000).random().toLong()

        val rotate = RotateAnimation(
            fromDegrees, toDegrees,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = spinDuration
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    stopSpinSound()
                    val finalAngle = toDegrees % 360
                    binding.homeBottle.rotation = finalAngle
                    viewModel.updateAngle(finalAngle)
                    startCountdown() // Criterio 5
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }

        binding.homeBottle.startAnimation(rotate)
    }

    private fun playSpinSound() {
        try {
            // Se usa getIdentifier para evitar error si el archivo no existe físicamente aún
            val resId = resources.getIdentifier("bottle_spin", "raw", requireContext().packageName)
            if (resId != 0) {
                spinMediaPlayer = MediaPlayer.create(requireContext(), resId).apply {
                    isLooping = true
                    start()
                }
            } else {
                Log.w("HomeFragment", "No se encontró res/raw/bottle_spin para el sonido de giro")
            }
        } catch (e: Exception) {
            Log.e("HomeFragment", "Error al reproducir sonido de giro", e)
        }
    }

    private fun stopSpinSound() {
        spinMediaPlayer?.stop()
        spinMediaPlayer?.release()
        spinMediaPlayer = null
    }

    private fun startCountdown() {
        binding.countdownText.isVisible = true
        viewLifecycleOwner.lifecycleScope.launch {
            for (i in 3 downTo 0) {
                binding.countdownText.text = i.toString()
                delay(1000)
            }
            binding.countdownText.isVisible = false
            viewModel.triggerChallenge() // Criterio 6 (HU 12)
        }
    }

    private fun observeViewModel() {
        viewModel.showChallengeEvent.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow) {
                showRandomChallengeDialog()
            }
        }
    }

    private fun showRandomChallengeDialog() {
        // HU 12: Mostrar reto aleatorio
        val challengesList = challengeViewModel.challenges.value
        val randomChallengeText = if (challengesList.isNotEmpty()) {
            challengesList.random().text
        } else {
            "No hay retos disponibles. ¡Crea algunos en el gestor!"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("¡El destino ha hablado!")
            .setMessage(randomChallengeText)
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                onChallengeDialogClosed()
                viewModel.onChallengeShown()
            }
            .show()
    }

    private fun onChallengeDialogClosed() {
        // Criterio 8: Reanudar solo tras cerrar el diálogo
        if (wasMusicOn) {
            MusicPlayerManager.resume()
            actualizarIconoVolumen()
        }
        // Restaurar controles (Criterio 7)
        binding.homeButton.isVisible = true
        binding.tvPressMe.isVisible = true
        setupBlinkAnimation()
    }

    private fun starButtonAction(){
        val startButton = binding.contentToolbar.toolBarStartButton
        startButton.setOnClickListener {
            BrandedToast.show(requireContext(), "Calificación Presionado")
            findNavController().navigate(R.id.action_homeFragment_to_ratingFragment)
        }
    }

    private fun controllerButtonAction(){
        val controllerButton = binding.contentToolbar.toolBarControllerButton
        controllerButton.setOnClickListener {
            BrandedToast.show(requireContext(), "Instrucciones Presionado")
            findNavController().navigate(R.id.action_homeFragment_to_instructionsFragment)
        }
    }

    private fun addButtonAction(){
        val addButton = binding.contentToolbar.toolBarAddButtom
        addButton.setOnClickListener {
            BrandedToast.show(requireContext(), "Gestor de Retos Presionado")
            findNavController().navigate(R.id.action_homeFragment_to_challengeManagementFragment)
        }
    }

    private fun shareButtonAction(){
        val shareButton = binding.contentToolbar.toolBarShareButton
        shareButton.setOnClickListener {
            BrandedToast.show(requireContext(), "Compartir Presionado")
            val shareText = "App pico botella.\nSolo los valientes lo juegan !!\nhttps://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es"
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(shareIntent, "Compartir vía"))
        }
    }

    private fun volumeButtonAction(){
        val volumeButton = binding.contentToolbar.toolBarVolumeButton
        volumeButton.setOnClickListener {
            if (MusicPlayerManager.isOn) {
                MusicPlayerManager.pause()
                BrandedToast.show(requireContext(), "Sonido pausado")
            } else {
                MusicPlayerManager.resume()
                BrandedToast.show(requireContext(), "Sonido activado")
            }
            actualizarIconoVolumen()
        }
    }

    private fun actualizarIconoVolumen() {
        val volumeButton = binding.contentToolbar.toolBarVolumeButton
        if (MusicPlayerManager.isOn) {
            volumeButton.setImageResource(R.drawable.volume)
        } else {
            volumeButton.setImageResource(R.drawable.mute)
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
        stopSpinSound()
    }
}