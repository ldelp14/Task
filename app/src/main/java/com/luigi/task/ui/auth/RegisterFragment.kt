package com.luigi.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.luigi.task.R
import com.luigi.task.databinding.FragmentRegisterBinding
import com.luigi.task.databinding.FragmentSplashBinding
import com.luigi.task.util.FirebaseHelper
import com.luigi.task.util.initToolbar
import com.luigi.task.util.showBottomSheet

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun initListener(){
        binding.btnRegistrar.setOnClickListener{
            validateData()
        }
    }

    private fun registerUser(email: String, password: String){
        try{
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    task ->
                    if (task.isSuccessful){
                        //direcionar para a tela home
                        findNavController().navigate(R.id.action_global_homeFragment)
                    }else{
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = getString(FirebaseHelper.validError(task.exception?.message.toString())))
                    }
                }
        }catch (e: Exception){
            Toast.makeText(requireContext(),e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListener()
    }

    private fun validateData(){
        val email = binding.digiteEmail.text.toString().trim()
        val senha = binding.digiteSenha.text.toString().trim()
        if(email.isNotBlank()){
            if(senha.isNotBlank()){
                binding.progressBar.isVisible = false
                registerUser(email,senha)
            }else{
                Toast.makeText(requireContext(),"Preencha a senha!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(),"Preencha a senha!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}