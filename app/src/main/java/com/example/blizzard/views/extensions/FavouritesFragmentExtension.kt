package com.example.blizzard.views.extensions

import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.lifecycle.ViewModelProvider
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.viewmodel.BlizzardViewModel
import com.example.blizzard.views.FavouritesFragment
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicReference

/**
Create by kelvin clark on 9/27/2020
 */

suspend fun FavouritesFragment.makeViewsVisible(entities: AtomicReference<List<WeatherDataEntity>>) {
    withContext(Dispatchers.Main) {
        if (entities.get().isNotEmpty()) {
            adapter?.insertWeatherEntities(entities.get())
            iv_no_data.visibility = View.INVISIBLE
            rv_fav.visibility = View.VISIBLE
            tv_no_data.visibility = View.INVISIBLE

        } else {
            delay(1000L)
            iv_no_data.alpha = 0f
            tv_no_data.alpha = 0f
            iv_no_data.animate()
                    .alpha(1f)
                    .setDuration(100)
                    .setInterpolator(AnticipateInterpolator())
                    .start()
            tv_no_data.animate()
                    .alpha(1f)
                    .setDuration(100)
                    .setInterpolator(AnticipateInterpolator())
                    .start()
            iv_no_data.visibility = View.VISIBLE
            tv_no_data.visibility = View.VISIBLE

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