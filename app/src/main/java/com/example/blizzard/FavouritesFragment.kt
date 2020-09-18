package com.example.blizzard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.util.FavouriteFragmentAdapter
import com.example.blizzard.viewmodel.BlizzardViewModel
import kotlinx.android.synthetic.main.fragment_favourites.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

class FavouritesFragment : Fragment() {
    private var weatherRecyclerView: RecyclerView = rv_fav
    private var adapter: FavouriteFragmentAdapter? = null
    private var favHandler: Handler? = null
    private var ivNoData: ImageView? = null
    private var tvNoData: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favHandler = Handler(Looper.getMainLooper())
        ivNoData = view.findViewById(R.id.iv_no_data)
        tvNoData = view.findViewById(R.id.tv_no_data)
        val layoutManager = LinearLayoutManager(context)
        weatherRecyclerView.layoutManager = layoutManager
        adapter = FavouriteFragmentAdapter(context, ArrayList())
        weatherRecyclerView.adapter = adapter
        initialiseAdapter()
    }

    private fun initialiseAdapter() {
        val viewModel = ViewModelProvider(requireActivity()).get(BlizzardViewModel::class.java)
        val entities = AtomicReference<List<WeatherDataEntity>>()
        Executors.newSingleThreadExecutor().execute {
            val dataEntities = viewModel.allDataFromDb
            val favWeather: MutableList<WeatherDataEntity> = ArrayList()
            for (dataEntity in dataEntities) {
                if (dataEntity.favourite) {
                    favWeather.add(dataEntity)
                }
            }
            entities.set(favWeather)
            makeViewsVisible(entities)
        }
    }

    private fun makeViewsVisible(entities: AtomicReference<List<WeatherDataEntity>>) {
        if (entities.get().isNotEmpty()) {
            favHandler?.post {
                adapter?.insertWeatherEntities(entities.get())
                ivNoData?.visibility = View.INVISIBLE
                weatherRecyclerView.visibility = View.VISIBLE
                tvNoData?.visibility = View.INVISIBLE
            }
        } else {
            favHandler?.postDelayed({
                ivNoData?.alpha = 0f
                tvNoData?.alpha = 0f
                ivNoData?.animate()
                        .alpha(1f)
                        .setDuration(100)
                        .setInterpolator(AnticipateInterpolator())
                        .start()
                tvNoData?.animate()
                        .alpha(1f)
                        .setDuration(100)
                        .setInterpolator(AnticipateInterpolator())
                        .start()
                ivNoData?.visibility = View.VISIBLE
                tvNoData?.visibility = View.VISIBLE
            }, 1000)
        }
    }
}