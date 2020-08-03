package com.example.blizzard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.example.blizzard.HomeFragmentDirections.ActionFirstFragmentToSecondFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class HomeFragment extends Fragment {

    TextInputEditText searchBox;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBox = view.findViewById(R.id.enter_city);

        view.findViewById(R.id.button_search).setOnClickListener(view1 -> {
            if (Objects.requireNonNull(searchBox.getText()).toString().isEmpty()){
                searchBox.setError("Enter city name");
            }else{
                String cityName = searchBox.getText().toString();
                ActionFirstFragmentToSecondFragment action = HomeFragmentDirections.actionFirstFragmentToSecondFragment(cityName);
                Navigation.findNavController(view1).navigate(action);
            }
        });
    }
}