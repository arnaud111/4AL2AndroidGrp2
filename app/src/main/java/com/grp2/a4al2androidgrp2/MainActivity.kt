package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import com.grp2.a4al2androidgrp2.viewmodel.auth.LoginViewModel
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel
import com.grp2.a4al2androidgrp2.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}

class LoginFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(requireContext()).apply {
            text = "Subscribe"
            setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSubscribeFragment()
                )
            }
        }
    }


}