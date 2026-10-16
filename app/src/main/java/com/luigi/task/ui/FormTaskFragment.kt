package com.luigi.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.luigi.task.R
import com.luigi.task.data.model.Status
import com.luigi.task.data.model.Task
import com.luigi.task.databinding.FragmentFormTaskBinding
import com.luigi.task.util.initToolbar
import com.luigi.task.util.showBottomSheet

class FormTaskFragment : Fragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: Task
    private var newTask: Boolean = true
    private var status: Status = Status.TODO
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFormTaskBinding.inflate(inflater,container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        reference = Firebase.database.reference
        auth = Firebase.auth

        initListener()
    }

    private fun initListener(){
        binding.buttonSave.setOnClickListener{
            validateData()
        }
        binding.radioGroup.setOnCheckedChangeListener { _, id -> status = when(id){
            R.id.rbTodo -> Status.TODO
            R.id.rbDoing -> Status.DOING
            else -> Status.DONE

        } }
    }

    private fun validateData(){
        val desc = binding.editTextDescricao.text.toString().trim()
        if(desc.isNotBlank()){
            task=Task(reference.database.reference.push().key.toString(),binding.editTextDescricao.text.toString().trim(),status)
            saveTask()
        }else{
            Toast.makeText(requireContext(),"Preencha uma descrição!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveTask(){
        reference
            .child("task")
            .child(auth.currentUser?.uid ?: "")
            .child(task.id)
            .setValue(task).addOnCompleteListener {
                result->
                if (result.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        "Registro gravado com sucesso.",
                        Toast.LENGTH_SHORT).show()

                    findNavController().popBackStack()

                }else{
                    binding.progressBar.isVisible=false
                    showBottomSheet(message="Erro ao registrar.")
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}