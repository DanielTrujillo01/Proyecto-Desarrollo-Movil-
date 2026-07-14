package com.example.fragmentspractice.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.fragmentspractice.R
import com.example.fragmentspractice.databinding.DialogChallengeResultBinding
import com.example.fragmentspractice.viewmodel.ChallengeResultViewModel

class ChallengeResultDialogFragment: DialogFragment() {

    private var _binding: DialogChallengeResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeResultViewModel by viewModels()

    private val challengeText: String by lazy {
        requireArguments().getString(ARG_CHALLENGE_TEXT, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChallengeResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvChallengeResultText.text = challengeText
        binding.btnCerrar.setOnClickListener { dismiss() }

        observePokemon()
        // Evita pedir un nuevo Pokémon (y perder el que ya se mostraba) si el
        // diálogo se recrea por un cambio de configuración, como rotar la pantalla.
        if (savedInstanceState == null) {
            viewModel.loadRandomPokemon()
        }
    }

    private fun observePokemon() {
        viewModel.pokemon.observe(viewLifecycleOwner) { pokemon ->
            binding.pbPokemonLoading.isVisible = false
            if (pokemon != null) {
                binding.ivPokemon.contentDescription = pokemon.name
                Glide.with(this)
                    .load(pokemon.imageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.empty_bottle)
                    .error(R.drawable.empty_bottle)
                    .into(binding.ivPokemon)
            } else {
                // Si la API falla, se deja un ícono de la app en vez de un círculo vacío.
                binding.ivPokemon.setImageResource(R.drawable.empty_bottle)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Criterio 1: fondo propio (negro degradado), sin el recuadro blanco
        // por defecto que agrega el sistema alrededor del diálogo.
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }
        // Criterio 6: el diálogo solo se cierra con el botón "Cerrar".
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Se notifica siempre que el diálogo se cierre (botón Cerrar u otra
        // vía, como el botón atrás) para que HomeFragment reanude la partida.
        setFragmentResult(REQUEST_KEY, bundleOf())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ChallengeResultDialogFragment"
        const val REQUEST_KEY = "challenge_result_closed"
        private const val ARG_CHALLENGE_TEXT = "arg_challenge_text"

        fun newInstance(challengeText: String): ChallengeResultDialogFragment {
            return ChallengeResultDialogFragment().apply {
                arguments = bundleOf(ARG_CHALLENGE_TEXT to challengeText)
            }
        }
    }
}