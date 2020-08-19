package com.example.blizzard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blizzard.data.entities.WeatherDataEntity;
import com.example.blizzard.data.repository.BlizzardRepository;
import com.example.blizzard.util.FavouriteFragmentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class FavouritesFragment extends Fragment {
    private RecyclerView weatherRecyclerView;
    private FavouriteFragmentAdapter adapter;


    public FavouritesFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherRecyclerView = view.findViewById(R.id.rv_fav);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        weatherRecyclerView.setLayoutManager(layoutManager);
        adapter = new FavouriteFragmentAdapter(getContext(), new ArrayList<>());
        weatherRecyclerView.setAdapter(adapter);
        initialiseAdapter();
    }

    private void initialiseAdapter() {
        BlizzardRepository repository = new BlizzardRepository(getContext());

        Executors.newSingleThreadExecutor().execute(() -> {
                    List<WeatherDataEntity> entities = repository.getAllDataFromDb();
                    adapter.insertWeatherEntities(entities);
                }
        );
    }


}