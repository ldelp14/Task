package com.luigi.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luigi.task.R
import com.luigi.task.databinding.FragmentDoingBinding
import com.luigi.task.databinding.FragmentSplashBinding

class DoingFragment : Fragment() {

    private var _binding: FragmentDoingBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDoingBinding.inflate(inflater, container, false)
        return binding.root
    }

}