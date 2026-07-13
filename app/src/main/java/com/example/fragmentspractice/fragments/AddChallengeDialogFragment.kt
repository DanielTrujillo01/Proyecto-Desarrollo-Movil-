package com.example.fragmentspractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.fragmentspractice.R
import com.example.fragmentspractice.databinding.DialogAddChallengeBinding
import com.example.fragmentspractice.viewmodel.ChallengeViewModel

/**
 * HU 7.0: Add challenge dialog.
 *
 * Lets the player create a new challenge that gets stored in the database
 * and shown in the challenge management list (HU6).
 */
class AddChallengeDialogFragment : DialogFragment() {

    private var _binding: DialogAddChallengeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnAdd.setOnClickListener { addChallenge() }
    }

    @Suppress("DEPRECATION")
    override fun onStart() {
        super.onStart()
        // Keeps the Add/Cancel buttons visible above the keyboard while typing.
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun addChallenge() {
        val text = binding.etChallengeText.text.toString().trim()
        if (text.isEmpty()) {
            Toast.makeText(requireContext(), R.string.challenge_empty_error, Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.addChallenge(text)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AddChallengeDialogFragment"
    }
}
