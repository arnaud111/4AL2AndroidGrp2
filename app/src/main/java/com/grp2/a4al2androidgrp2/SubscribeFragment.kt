package com.grp2.a4al2androidgrp2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.R


class SubscribeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("Guys, we are in subscribe")
        return inflater.inflate(R.layout.subscribe_activity, container, false)
    }
}