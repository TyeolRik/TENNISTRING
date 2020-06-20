package io.tyeolrik.tennistring.ui.equipmentzone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import io.tyeolrik.tennistring.R;

public class EquipmentZoneFragment extends Fragment {

    private EquipmentZoneViewModel equipmentZoneViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        equipmentZoneViewModel =
                ViewModelProviders.of(this).get(EquipmentZoneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_equipmentzone, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        equipmentZoneViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}