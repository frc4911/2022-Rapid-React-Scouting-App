package com.example.a2022frcscoutingapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FileNameDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.file_name_fragment, null);
        //get the spinner from the xml
        Spinner eventNameDropdown = view.findViewById(R.id.event_name_spinner);
        List<String> eventNames = new ArrayList<>();
        eventNames.add("glacier_peak");
        eventNames.add("sammamish");
        eventNames.add("pnw_champs");
        eventNames.add("world_champs");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, eventNames);
        eventNameDropdown.setAdapter(adapter);


        Spinner tabletIdDropdown = view.findViewById(R.id.tablet_id_spinner);
        List<String> tabletIds = new ArrayList<>();
        tabletIds.add("1");
        tabletIds.add("2");
        tabletIds.add("3");
        tabletIds.add("4");
        tabletIds.add("5");
        tabletIds.add("6");
        tabletIds.add("7");
        tabletIds.add("8");
        tabletIds.add("9");
        tabletIds.add("10");

        ArrayAdapter<String> tabletIdsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tabletIds);
        tabletIdDropdown.setAdapter(tabletIdsAdapter);


        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String fileName = (String) eventNameDropdown.getSelectedItem() + "_" + (String) tabletIdDropdown.getSelectedItem() + ".csv";
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(MainActivity.FIlE_NAME_KEY, fileName);
                        editor.apply();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FileNameDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
