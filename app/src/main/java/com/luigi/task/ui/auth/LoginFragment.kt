package com.luigi.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.luigi.task.R
import com.luigi.task.databinding.FragmentLoginBinding
import com.luigi.task.util.showBottomSheet

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        initListener()
    }

    private fun checkAuth(){
        try{
            val currentUser = auth.currentUser

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
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    findNavController().navigate(R.id.action_global_homeFragment)
                }else{
                    Toast.makeText(requireContext(),task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e: Exception){

        }
    }

}