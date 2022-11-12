package com.example.calculatorfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.calculatorfragment.databinding.FragmentButtonBinding
import com.example.calculatorfragment.viewmodel.CalcViewModel

class ButtonFragment: Fragment(R.layout.fragment_button) {

    private lateinit var binding: FragmentButtonBinding
    private lateinit var viewModel: CalcViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_button,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(CalcViewModel::class.java)

        binding.calcViewModel = viewModel

        binding.setLifecycleOwner(this)

        viewModel.showToast1.observe(viewLifecycleOwner, { doShow ->
            if (doShow) {
                showToastFirstDigit()
                viewModel.onToast1ShownComplete()
            }
        })

        viewModel.showToast2.observe(viewLifecycleOwner, { doShow ->
            if (doShow) {
                showToastNextDigit()
                viewModel.onToast2ShownComplete()
            }
        })

        return binding.root
    }

    private fun showToastFirstDigit() {
        Toast.makeText(activity, "Введите хотя бы одно число", Toast.LENGTH_LONG).show()
    }

    private fun showToastNextDigit() {
        Toast.makeText(activity, "Введите следующее число", Toast.LENGTH_LONG).show()
    }


}