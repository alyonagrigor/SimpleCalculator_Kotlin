package com.example.calculatorfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.example.calculatorfragment.R
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.calculatorfragment.databinding.FragmentButtonBinding
import com.example.calculatorfragment.databinding.FragmentJournalBinding
import com.example.calculatorfragment.viewmodel.CalcViewModel
import java.lang.StringBuilder

class JournalFragment : Fragment(R.layout.fragment_journal) {

    private lateinit var binding: FragmentJournalBinding
    private lateinit var viewModel: CalcViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_journal,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(CalcViewModel::class.java)

        binding.calcViewModel = viewModel

        binding.setLifecycleOwner(this)

        return binding.root
    }
}