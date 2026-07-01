package com.example.fragmentspractice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fragmentspractice.R
import com.example.fragmentspractice.databinding.SplashFragmentBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.delay
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import kotlinx.coroutines.*


class SplashFragment : Fragment() {

    private lateinit var binding: SplashFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
        goToHome()
    }

    private fun startAnimation(){
        val image = binding.IVbottle

        Glide.with(this)
            .load(R.drawable.bottle)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)
    }

    private fun goToHome() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000)
            findNavController().navigate(
                R.id.action_splashFragment_to_homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build()
            )

        }
    }
}