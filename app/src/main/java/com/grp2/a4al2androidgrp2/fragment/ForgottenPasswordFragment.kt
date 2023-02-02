package com.grp2.a4al2androidgrp2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.viewmodel.auth.*

class ForgottenPasswordFragment: Fragment() {
    lateinit var forgottenPasswordViewModel: ForgottenPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.forgotten_password, container, false)
        initOnClick(view)
        initForgottenPasswordViewModel()
        return view
    }

    private fun initOnClick(view: View) {
        view.findViewById<Button>(R.id.btn_reset).setOnClickListener(){
            resetPassword(view)
        }
    }

    private fun initForgottenPasswordViewModel() {
        forgottenPasswordViewModel = ViewModelProvider(this)[ForgottenPasswordViewModel::class.java]
        forgottenPasswordViewModel.getAccountDataObserver().observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(requireContext(), "Failed to reset", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Success to reset", Toast.LENGTH_LONG).show()
                findNavController().navigate(
                    ForgottenPasswordFragmentDirections.actionForgottenPasswordFragmentToLoginFragment()
                )
            }
        }
    }

    fun resetPassword(view: View) {
        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)

        if (!resetPasswordChecker(email, password)) {
            resetPasswordRequest(email, password)
        }
    }

    private fun resetPasswordRequest(email: EditText, password: EditText) {
        val resetPasswordRequest = LoginRequest(
            email.text.toString(),
            password.text.toString()
        )
        forgottenPasswordViewModel.sendNewPassword(resetPasswordRequest)
    }

    private fun resetPasswordChecker(email: EditText, password: EditText): Boolean {
        var error = false

        email.setBackgroundResource(R.drawable.edit_text_background)
        password.setBackgroundResource(R.drawable.edit_text_background)

        if (email.text.toString() == "") {
            email.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        if (password.text.toString() == "") {
            password.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        return error;
    }

}