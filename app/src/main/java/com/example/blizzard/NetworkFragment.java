package com.example.blizzard;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.example.blizzard.util.CheckNetworkUtil;

import java.util.Objects;

public class NetworkFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = NavHostFragment.findNavController(this);
        showDialog(navController);
    }

    private void showDialog(NavController navController) {
        //Initialize dialog
        final Dialog dialog = new Dialog(requireContext());
        //Set content view
        dialog.setContentView(R.layout.alert_dialog);
        //Set outside touch
        dialog.setCanceledOnTouchOutside(false);
        //Set what happens at Back Press
        dialog.setCancelable(false);
        //Set dialog width and height
        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //Set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //Set animation
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

        //Initialize dialog variable
        Button okButton = dialog.findViewById(R.id.ok_button);
        Button exitButton = dialog.findViewById(R.id.exit_button);
        //Perform onClick listener on OK Button
        okButton.setOnClickListener(view -> {
            boolean isDeviceConnected = CheckNetworkUtil.isNetworkAvailable(requireContext());
            if (isDeviceConnected) {
                int startDestination = navController.getGraph().getStartDestination();
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build();
                navController.navigate(startDestination, null, navOptions);
                dialog.dismiss();
            }
        });
        //Perform onClick listener on Exit Button
        exitButton.setOnClickListener(view -> requireActivity().finish());
        //Show dialog
        dialog.show();
    }
}