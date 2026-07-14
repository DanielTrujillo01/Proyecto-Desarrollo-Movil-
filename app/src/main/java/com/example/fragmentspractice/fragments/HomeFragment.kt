package com.example.fragmentspractice.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fragmentspractice.R
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.fragmentspractice.databinding.HomeFragmentBinding
import com.example.fragmentspractice.utils.BrandedToast

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

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

    override fun onResume() {
        super.onResume()
        // Sincroniza el ícono de volumen por si el estado del audio
        // cambió mientras estábamos en otro fragment (ej: Instructions)
        actualizarIconoVolumen()
    }

    private fun setupMusic() {
        MusicPlayerManager.start(requireContext())
        actualizarIconoVolumen()
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
        // No se libera MusicPlayerManager aquí: la música debe seguir
        // sonando (o mantener su estado) al navegar a otros fragments.
        // Solo debería liberarse al cerrar la app (ej: en la Activity).
    }
}