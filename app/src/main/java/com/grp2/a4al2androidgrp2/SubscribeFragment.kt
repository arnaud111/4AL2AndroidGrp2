package com.grp2.a4al2androidgrp2

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.api.auth.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import com.grp2.a4al2androidgrp2.viewmodel.auth.LoginViewModel
import com.grp2.a4al2androidgrp2.viewmodel.auth.SubscribeViewModel
import okhttp3.internal.cache2.Relay.Companion.edit


class SubscribeFragment : Fragment() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var subscribeViewModel: SubscribeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("Guys, we are in subscribe")
        return inflater.inflate(R.layout.subscribe_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.popup()
    }
    @SuppressLint("ClickableViewAccessibility")
    fun popup() {
        val popupView = layoutInflater.inflate(R.layout.popup_password, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val passwordField = requireView().findViewById<EditText>(R.id.password)
        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= passwordField.right - passwordField.compoundDrawables[2].bounds.width()) {
                    popupWindow.showAsDropDown(passwordField, 0, 0, Gravity.END)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    fun subscribe(view: View) {
        val username = view.findViewById<EditText>(R.id.username)
        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)
        val confirmPassword = view.findViewById<EditText>(R.id.confirm_password)

        if (!this.checkSubscribeInfo(username, email, password, confirmPassword)) {
            initSubscribeViewModel()
            val subscribeRequest = SubscribeRequest (
                email.text.toString(),
                password.text.toString()
            )
            subscribeViewModel.subscribe(subscribeRequest)
        }
    }

    private fun initSubscribeViewModel() {
        subscribeViewModel = ViewModelProvider(this)[SubscribeViewModel::class.java]
        subscribeViewModel.getAccountObserver().observe(this) {
            if (it == null) {
                Toast.makeText(requireContext(), "Failed to subscribe", Toast.LENGTH_LONG).show()
            } else {
                loginRequest()
            }
        }
    }

    private fun loginRequest() {

        val email = requireView().findViewById<EditText>(R.id.email)
        val password = requireView().findViewById<EditText>(R.id.password)

        initLoginViewModel();
        val loginRequest = LoginRequest(
            email.text.toString(),
            password.text.toString()
        )
        loginViewModel.login(loginRequest)
    }

    private fun initLoginViewModel() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.getLoginTokenObserver().observe(this) {
            if (it == null) {
                Toast.makeText(requireContext(), "Failed to login", Toast.LENGTH_LONG).show()
                findNavController().navigate(
                    SubscribeFragmentDirections.actionSubscribeFragmentToLoginFragment()
                )
            } else {
                val sharedPref = requireActivity().getSharedPreferences(
                    "token_pref",
                    AppCompatActivity.MODE_PRIVATE
                )
                val editor = sharedPref.edit()
                editor.putString("token", it.token)
                editor.apply()

                findNavController().navigate(
                    SubscribeFragmentDirections.actionSubscribeFragmentToHomePageFragment()
                )
            }
        }
    }

    private fun checkSubscribeInfo(username: EditText, email: EditText, password: EditText, confirmPassword: EditText): Boolean {
        var error = false

        username.setBackgroundResource(R.drawable.edit_text_background)
        email.setBackgroundResource(R.drawable.edit_text_background)
        password.setBackgroundResource(R.drawable.edit_text_background)
        confirmPassword.setBackgroundResource(R.drawable.edit_text_background)

        if (username.text.toString() == "") {
            username.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        if (email.text.toString() == "" ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        if (password.text.toString().length < 8 ||
            !password.text.toString().matches(Regex(".*[A-Z].*")) ||
            !password.text.toString().matches(Regex(".*[a-z].*")) ||
            !password.text.toString().matches(Regex(".*\\d.*"))) {
            password.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        if (confirmPassword.text.toString() == "" ||
            confirmPassword.text.toString() != password.text.toString()) {
            confirmPassword.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        return error;
    }
}