package com.luigi.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.luigi.task.util.hideKeyboard
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.luigi.task.R
import com.luigi.task.data.model.Status
import com.luigi.task.data.model.Task
import com.luigi.task.databinding.FragmentFormTaskBinding
import com.luigi.task.util.FirebaseHelper
import com.luigi.task.util.initToolbar
import com.luigi.task.util.showBottomSheet

class FormTaskFragment : Fragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: Task
    private var newTask: Boolean = true
    private var status: Status = Status.TODO

    private val viewModel: TaskViewModel by activityViewModels()
    private val args: FormTaskFragmentArgs by navArgs()
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

        getArgs()
        initListener()
    }

    private fun getArgs(){
        args.task.let{
            if (it != null){
                this.task = it
                configTask()
            }
        }
    }

    private fun configTask(){
        newTask=false
        status=task.status
        binding.textToolbar.setText(R.string.text_toolbar_update_form_task_fragment)

        binding.textToolbar.text="Editando..."
        binding.editTextDescricao.setText(task.description)
        setStatus()
    }

    private fun setStatus(){
        val id=when(task.status){
            Status.TODO -> R.id.rbTodo
            Status.DOING -> R.id.rbDoing
            else -> R.id.rbDone
        }
        binding.radioGroup.check(id)
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
        val description = binding.editTextDescricao.text.toString().trim()
        if(description.isNotBlank()){
            hideKeyboard()
            binding.progressBar.isVisible = true

            if(newTask) task = Task()

            task.description=description
            task.status=status
            saveTask()
        }else{
            Toast.makeText(requireContext(),"Preencha uma descrição!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveTask(){
        val userId = FirebaseHelper.getIdUser()
        if(userId==null){
            showBottomSheet(message="Ocorreu um erro! Por favor tente novamente mais tarde")
            return
        }
        binding.progressBar.isVisible=true
        FirebaseHelper.getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .setValue(task).addOnCompleteListener {
                result->
                if (result.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        "Registro gravado com sucesso.",
                        Toast.LENGTH_SHORT).show()
                    if(newTask) {
                        findNavController().popBackStack()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Tarefa atualizada com sucesso",
                            Toast.LENGTH_SHORT
                        )
                    }

                }else{
                    binding.progressBar.isVisible=false
                    val errorMessage = result.exception?.message
                        ?: getString(R.string.error_generic)
                    showBottomSheet(message="Erro ao registrar.")
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}