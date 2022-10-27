package com.example.calculatorfragment

import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorfragment.ButtonFragment.OnFragmentSendDataListener
import android.os.Bundle
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), OnFragmentSendDataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSendData(selectedItem: StringBuilder?) {
        val fragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as JournalFragment?
        fragment?.setSelectedItem(selectedItem)
    }
}