package com.grp2.a4al2androidgrp2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.viewmodel.auth.LoginViewModel
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel


class LoginFragment : Fragment() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var meViewModel: MeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initLoginViewModel()
        initMeViewModel()
        if (activity?.getPreferences(0)?.contains("token") == true) {
            meViewModel.me(requireActivity().getPreferences(0).getString("token", "")!!)
        }
        val view = inflater.inflate(R.layout.login_main, container, false)
        initOnClick(view)
        return view
    }

    private fun initOnClick(view: View) {
        view.findViewById<Button>(R.id.btn_connect).setOnClickListener(){
            login(view)
        }
        view.findViewById<Button>(R.id.btn_tosubscribe).setOnClickListener(){
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToSubscribeFragment()
            )
        }
    }

    private fun initMeViewModel() {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        meViewModel.getAccountObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToHomePageFragment()
                )
            }
        }
    }


    fun login(view: View) {
        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)

        if (!loginChecker(email, password)) {
            loginRequest(email, password)
        }
    }

    private fun loginRequest(email: EditText, password: EditText) {
        val loginRequest = LoginRequest(
            email.text.toString(),
            password.text.toString()
        )
        loginViewModel.login(loginRequest)
    }

    private fun initLoginViewModel() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.getLoginTokenObserver().observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(requireContext(), "Failed to login", Toast.LENGTH_LONG).show()
            } else {
                val editor = requireActivity().getPreferences(0).edit()
                editor.putString("token", it.token)
                editor.apply()
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToHomePageFragment()
                )
            }
        }
    }
    private fun loginChecker(email: EditText, password: EditText): Boolean {
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