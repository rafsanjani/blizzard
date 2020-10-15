package com.example.blizzard.views.extensions

import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.lifecycle.ViewModelProvider
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.viewmodel.BlizzardViewModel
import com.example.blizzard.views.FavouritesFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.atomic.AtomicReference

/**
Create by kelvin clark on 9/27/2020
 */

suspend fun FavouritesFragment.makeViewsVisible(entities: AtomicReference<List<WeatherDataEntity>>) {
    withContext(Dispatchers.Main) {
        if (entities.get().isNotEmpty()) {
            adapter?.insertWeatherEntities(entities.get())
            binding.ivNoData.visibility = View.INVISIBLE
            binding.rvFav.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.INVISIBLE

        } else {
            delay(1000L)
            binding.ivNoData.alpha = 0f
            binding.tvNoData.alpha = 0f
            binding.ivNoData.animate()
                    .alpha(1f)
                    .setDuration(100)
                    .setInterpolator(AnticipateInterpolator())
                    .start()
            binding.tvNoData.animate()
                    .alpha(1f)
                    .setDuration(100)
                    .setInterpolator(AnticipateInterpolator())
                    .start()
            binding.ivNoData.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.VISIBLE

        }
    }
}

suspend fun FavouritesFragment.initialiseAdapter() {
    val viewModel = ViewModelProvider(requireActivity()).get(BlizzardViewModel::class.java)
    val entities = AtomicReference<List<WeatherDataEntity>>()

    val dataEntities = viewModel.getAll()
    val favWeather: MutableList<WeatherDataEntity> = ArrayList()
    if (dataEntities != null) {
        for (dataEntity in dataEntities) {
            if (dataEntity != null) {
                if (dataEntity.favourite!!) {
                    favWeather.add(dataEntity)
                }
            }
        }
    }
    entities.set(favWeather)
    makeViewsVisible(entities)
}