package com.example.blizzard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blizzard.data.entities.WeatherDataEntity;
import com.example.blizzard.data.repository.BlizzardRepository;
import com.example.blizzard.util.FavouriteFragmentAdapter;
import com.example.blizzard.viewmodel.BlizzardViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;


public class FavouritesFragment extends Fragment {
    private RecyclerView weatherRecyclerView;
    private FavouriteFragmentAdapter adapter;
    private Handler favHandler;
    private ImageView ivNoData;
    private TextView tvNoData;


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
        favHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        weatherRecyclerView = view.findViewById(R.id.rv_fav);
        ivNoData = view.findViewById(R.id.iv_no_data);
        tvNoData = view.findViewById(R.id.tv_no_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        weatherRecyclerView.setLayoutManager(layoutManager);
        adapter = new FavouriteFragmentAdapter(getContext(), new ArrayList<>());
        weatherRecyclerView.setAdapter(adapter);
        initialiseAdapter();
    }

    private void initialiseAdapter() {
        BlizzardViewModel viewModel = new ViewModelProvider(requireActivity()).get(BlizzardViewModel.class);
        AtomicReference<List<WeatherDataEntity>> entities = new AtomicReference<>();
        Executors.newSingleThreadExecutor().execute(() -> {

                    List<WeatherDataEntity> dataEntities = viewModel.getAllDataFromDb();
                    List<WeatherDataEntity> favWeather = new ArrayList<>();

                    for (WeatherDataEntity dataEntity : dataEntities) {
                        if (dataEntity.getFavourite()) {
                            favWeather.add(dataEntity);
                        }
                    }

                    entities.set(favWeather);

                    makeViewsVisible(entities);
                }
        );

    }

    private void makeViewsVisible(AtomicReference<List<WeatherDataEntity>> entities) {
        if (entities.get().size() > 0) {
            favHandler.post(() -> {
                adapter.insertWeatherEntities(entities.get());
                ivNoData.setVisibility(View.INVISIBLE);
                weatherRecyclerView.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.INVISIBLE);
            });

        } else {
            favHandler.postDelayed(() -> {
                ivNoData.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.VISIBLE);
            }, 5000);
        }
    }


}