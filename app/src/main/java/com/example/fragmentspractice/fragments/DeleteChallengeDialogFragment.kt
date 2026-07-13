package com.example.fragmentspractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.fragmentspractice.data.Challenge
import com.example.fragmentspractice.databinding.DialogDeleteChallengeBinding
import com.example.fragmentspractice.viewmodel.ChallengeViewModel

/**
 * HU 9.0: Delete challenge dialog.
 *
 * Confirms whether the player wants to remove a challenge, opened from the
 * delete button on the challenge management screen (HU6).
 */
class DeleteChallengeDialogFragment : DialogFragment() {

    private var _binding: DialogDeleteChallengeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeViewModel by activityViewModels()

    private val challengeId: Long by lazy { requireArguments().getLong(ARG_CHALLENGE_ID) }
    private val challengeText: String by lazy { requireArguments().getString(ARG_CHALLENGE_TEXT, "") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDeleteChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvChallengeDescription.text = challengeText
        binding.btnNo.setOnClickListener { dismiss() }
        binding.btnYes.setOnClickListener {
            viewModel.deleteChallenge(Challenge(id = challengeId, text = challengeText))
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // The dialog must only close via NO/SI, not by tapping outside it.
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "DeleteChallengeDialogFragment"
        private const val ARG_CHALLENGE_ID = "arg_challenge_id"
        private const val ARG_CHALLENGE_TEXT = "arg_challenge_text"

        fun newInstance(id: Long, text: String): DeleteChallengeDialogFragment {
            return DeleteChallengeDialogFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_CHALLENGE_ID, id)
                    putString(ARG_CHALLENGE_TEXT, text)
                }
            }
        }
    }
}
