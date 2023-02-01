package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import com.grp2.a4al2androidgrp2.viewmodel.auth.LoginViewModel
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel
import okhttp3.internal.cache2.Relay.Companion.edit

class MainActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)
    }
}

class LoginFragment : Fragment() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var meViewModel: MeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initLoginViewModel()
        if (activity?.getPreferences(0)?.contains("token") == true) {
            initMeViewModel()
            meViewModel.me(requireActivity().getPreferences(0).getString("token", "")!!)
        }
        val view = inflater.inflate(R.layout.login_main, container, false)
        val btnConnect = view.findViewById<Button>(R.id.btn_connect)
        btnConnect.setOnClickListener(){
            println("Click login")
            login(view)
        }
        val btnTosubscribe = view.findViewById<Button>(R.id.btn_tosubscribe)
        btnTosubscribe.setOnClickListener(){
            println("Click subscribe")
            LoginFragmentDirections.actionLoginFragmentToSubscribeFragment()
        }
        return view
    }

    private fun initMeViewModel() {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        meViewModel.getAccountObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                LoginFragmentDirections.actionLoginFragmentToHomePageFragment()
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
        initLoginViewModel();
        val loginRequest = LoginRequest(
            email.text.toString(),
            password.text.toString()
        )
        loginViewModel.login(loginRequest)
    }

    private fun initLoginViewModel() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.getLoginTokenObserver().observe(viewLifecycleOwner, Observer<LoginToken?> {
            if (it == null) {
                //Toast.makeText(this@MainActivityOld, "Failed to login", Toast.LENGTH_LONG).show()
                println("Failed to login")
            } else {
                val editor = requireActivity().getPreferences(0).edit()
                editor.putString("token", it.token)
                editor.apply()
                LoginFragmentDirections.actionLoginFragmentToHomePageFragment()
            }
        })
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