package com.example.fragmentspractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fragmentspractice.adapters.ChallengeAdapter
import com.example.fragmentspractice.data.Challenge
import com.example.fragmentspractice.databinding.ChallengeManagementFragmentBinding
import com.example.fragmentspractice.viewmodel.ChallengeViewModel
import kotlinx.coroutines.launch

/**
 * HU 6.0: Challenge management screen.
 *
 * Lists every challenge stored in the database. Each row lets the player edit
 * or delete a challenge, and a floating action button opens the "add challenge"
 * dialog (HU7).
 */
class ChallengeManagementFragment : Fragment() {

    private var _binding: ChallengeManagementFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeViewModel by activityViewModels()
    private lateinit var challengeAdapter: ChallengeAdapter
    private var wasMusicOnWhenEntering = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChallengeManagementFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pauseBackgroundMusicIfNeeded()
        setupRecyclerView()
        setupBackButton()
        setupAddButton()
        observeChallenges()
    }

    private fun pauseBackgroundMusicIfNeeded() {
        wasMusicOnWhenEntering = MusicPlayerManager.isOn
        if (wasMusicOnWhenEntering) {
            MusicPlayerManager.pause()
        }
    }

    private fun setupRecyclerView() {
        challengeAdapter = ChallengeAdapter(
            onEditClick = { challenge -> openEditChallengeDialog(challenge) }
        )
        binding.rvChallenges.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChallenges.adapter = challengeAdapter
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            if (wasMusicOnWhenEntering) {
                MusicPlayerManager.resume()
            }
            findNavController().navigateUp()
        }
    }

    private fun setupAddButton() {
        binding.fabAddChallenge.setOnClickListener {
            AddChallengeDialogFragment().show(childFragmentManager, AddChallengeDialogFragment.TAG)
        }
    }

    private fun openEditChallengeDialog(challenge: Challenge) {
        EditChallengeDialogFragment.newInstance(challenge.id, challenge.text)
            .show(childFragmentManager, EditChallengeDialogFragment.TAG)
    }

    private fun observeChallenges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.challenges.collect { challenges ->
                    challengeAdapter.submitList(challenges)
                    binding.tvEmptyState.visibility =
                        if (challenges.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
