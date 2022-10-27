package com.example.calculatorfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.example.calculatorfragment.R
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.lang.StringBuilder

class JournalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_journal, container, false)
    }

    fun setSelectedItem(selectedItem: StringBuilder?) {
        val journal = requireView().findViewById<TextView>(R.id.journal)
        journal.text = selectedItem
    }
}