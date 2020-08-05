package com.example.blizzard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
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
    private boolean mIsNetworkAvailable;

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
            checkNetworkConnection();
            if (Objects.requireNonNull(searchBox.getText()).toString().isEmpty()) {
                searchBox.setError("Enter city name");
            } else if (!mIsNetworkAvailable) {
                searchBox.setError("Not Network Connectivity");
            } else {
                String cityName = searchBox.getText().toString();
                ActionFirstFragmentToSecondFragment action = HomeFragmentDirections.actionFirstFragmentToSecondFragment(cityName);
                Navigation.findNavController(view1).navigate(action);
            }
        });
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mIsNetworkAvailable = false;
        if (connMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(connMgr.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        mIsNetworkAvailable = true;
                    }
                }
            } else {
                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    mIsNetworkAvailable = true;
                }
            }
        }
    }

}