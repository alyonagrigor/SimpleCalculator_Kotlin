package com.example.calculatorfragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class JournalFragment extends Fragment {

    public JournalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journal, container, false);
    }

     public void setSelectedItem(StringBuilder selectedItem) {
            TextView journal = getView().findViewById(R.id.journal);
            journal.setText(selectedItem);
     }

}