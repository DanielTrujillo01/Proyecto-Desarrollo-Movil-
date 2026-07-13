package com.example.fragmentspractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.fragmentspractice.R
import com.example.fragmentspractice.databinding.DialogEditChallengeBinding
import com.example.fragmentspractice.viewmodel.ChallengeViewModel

/**
 * HU 8.0: Edit challenge dialog.
 *
 * Lets the player update the text of an existing challenge, opened from the
 * edit button on the challenge management screen (HU6).
 */
class EditChallengeDialogFragment : DialogFragment() {

    private var _binding: DialogEditChallengeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeViewModel by activityViewModels()

    private val challengeId: Long by lazy { requireArguments().getLong(ARG_CHALLENGE_ID) }
    private val challengeText: String by lazy { requireArguments().getString(ARG_CHALLENGE_TEXT, "") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etChallengeText.setText(challengeText)
        binding.etChallengeText.setSelection(binding.etChallengeText.text?.length ?: 0)

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener { saveChallenge() }
    }

    private fun saveChallenge() {
        val text = binding.etChallengeText.text.toString().trim()
        if (text.isEmpty()) {
            Toast.makeText(requireContext(), R.string.challenge_empty_error, Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.updateChallenge(challengeId, text)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "EditChallengeDialogFragment"
        private const val ARG_CHALLENGE_ID = "arg_challenge_id"
        private const val ARG_CHALLENGE_TEXT = "arg_challenge_text"

        fun newInstance(id: Long, text: String): EditChallengeDialogFragment {
            return EditChallengeDialogFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_CHALLENGE_ID, id)
                    putString(ARG_CHALLENGE_TEXT, text)
                }
            }
        }
    }
}
