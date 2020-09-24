package com.example.blizzard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.util.FavouriteFragmentAdapter
import com.example.blizzard.viewmodel.BlizzardViewModel
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class FavouritesFragment : Fragment() {
    private var adapter: FavouriteFragmentAdapter? = null
    private var favHandler: Handler? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favHandler = Handler(Looper.getMainLooper())
        val layoutManager = LinearLayoutManager(context)
        rv_fav.layoutManager = layoutManager
        adapter = FavouriteFragmentAdapter(requireContext(), ArrayList())
        rv_fav.adapter = adapter


        lifecycleScope.launch {
            initialiseAdapter()
        }


    }

    private suspend fun initialiseAdapter() {
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

    private suspend fun makeViewsVisible(entities: AtomicReference<List<WeatherDataEntity>>) {
        withContext(Main){
            if (entities.get().isNotEmpty()) {
                favHandler?.post {
                    adapter?.insertWeatherEntities(entities.get())
                    iv_no_data.visibility = View.INVISIBLE
                    rv_fav.visibility = View.VISIBLE
                    tv_no_data.visibility = View.INVISIBLE
                }
            } else {
                favHandler?.postDelayed({
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
                }, 1000)
            }
        }
    }
}