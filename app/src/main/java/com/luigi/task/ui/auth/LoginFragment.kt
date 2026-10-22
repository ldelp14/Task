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
import com.luigi.task.databinding.FragmentLoginBinding
import com.luigi.task.ui.BaseFragment
import com.luigi.task.util.FirebaseHelper
import com.luigi.task.util.showBottomSheet

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun checkAuth(){
        try{
            val currentUser = FirebaseHelper.getAuth().currentUser

            if (currentUser != null){
                findNavController().navigate(R.id.action_global_homeFragment)
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_authentication)
            }
        }catch (e: Exception){
            Toast.makeText(requireContext(),e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun initListener(){
        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }
        binding.btnRegistrar.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    private fun validateData(){
        val email = binding.digiteEmail.text.toString().trim()
        val senha = binding.digiteSenha.text.toString().trim()
        if(email.isNotBlank()){
            if(senha.isNotBlank()){

                hideKeyboard()
                binding.progressBar.isVisible = true
                loginUser(email,senha)
            }else{
                Toast.makeText(requireContext(),"Preencha a senha!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(),"Preencha a senha!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }
    private fun loginUser(email: String, password: String){
        try{
            FirebaseHelper.getAuth().signInWithEmailAndPassword(email,password).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    findNavController().navigate(R.id.action_global_homeFragment)
                }else{
                    binding.progressBar.isVisible=false
                    showBottomSheet(message = getString(FirebaseHelper.validError(task.exception?.message.toString())))
                }
            }
        }catch (e: Exception){

        }
    }

}