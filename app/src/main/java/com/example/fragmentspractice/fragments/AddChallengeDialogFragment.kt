package com.example.fragmentspractice.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
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
        watchChallengeText()
    }

    @Suppress("DEPRECATION")
    override fun onStart() {
        super.onStart()
        // Keeps the Add/Cancel buttons visible above the keyboard while typing.
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        // The dialog must only close via Cancel/Add, not by tapping outside it.
        dialog?.setCanceledOnTouchOutside(false)
    }

    private fun watchChallengeText() {
        binding.etChallengeText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.btnAdd.isEnabled = !s.isNullOrBlank()
            }
        })
    }

    private fun addChallenge() {
        val text = binding.etChallengeText.text.toString().trim()
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
