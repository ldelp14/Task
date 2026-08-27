package com.luigi.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luigi.task.R
import com.luigi.task.data.model.Status
import com.luigi.task.data.model.Task
import com.luigi.task.databinding.FragmentDoingBinding
import com.luigi.task.databinding.FragmentSplashBinding
import com.luigi.task.ui.adapter.TaskAdapter

class DoingFragment : Fragment() {

    private var _binding: FragmentDoingBinding?=null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        initRecyclerViewTask(getTask())
    }

    private fun initListeners(){
        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate((R.id.action_homeFragment_to_formTaskFragment))
        }
    }

    private fun initRecyclerViewTask(taskList: List<Task>){
        taskAdapter = TaskAdapter(requireContext(),taskList)
        binding.recyclerViewTask.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTask.setHasFixedSize(true)

        binding.recyclerViewTask.adapter = taskAdapter
    }


    private fun getTask() = listOf(
        Task("0", "Criar nova tela do app",Status.DOING),
        Task("1","Validar informações na tela de login",Status.DOING),
        Task("2","Adicionar nova funcionalidade no app",Status.DOING),
        Task("3","Salvar token localmente",Status.DOING),
        Task("2","Criar funcionalidade de logout no app",Status.DOING),
    )
}