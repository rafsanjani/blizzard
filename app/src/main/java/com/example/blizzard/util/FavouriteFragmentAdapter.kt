package com.example.blizzard.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.data.entities.extensions.StringCelsius
import com.example.blizzard.databinding.ItemWeatherBinding

/**
 * Created by kelvin_clark on 8/19/2020
 */
class FavouriteFragmentAdapter(private val weatherDataEntities: MutableList<WeatherDataEntity>) : RecyclerView.Adapter<FavouriteFragmentAdapter.ViewHolder>() {
    private val timeUtil = TimeUtil()
    private var binding: ItemWeatherBinding? = null

    fun insertWeatherEntities(entities: List<WeatherDataEntity>) {
        weatherDataEntities.clear()
        weatherDataEntities.addAll(entities)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = weatherDataEntities[position]
        timeUtil.setTime(entity.dt!!, entity.timeZone!!)
        val cityName = entity.cityName + ", " + entity.country
        holder.tvCityName.text = cityName
        holder.tvDescription.text = entity.description
        val humidity = entity.humidity.toString() + "%"
        holder.tvHumidity.text = humidity
        holder.tvTemperature.text = entity.StringCelsius
        holder.tvTime.text = timeUtil.getTime()
        val windSpeed = entity.windSpeed.toString() + " m/s"
        holder.tvWindSpeed.text = windSpeed
    }

    override fun getItemCount(): Int {
        return weatherDataEntities.size
    }

    fun destroyBinding() {
        binding = null
    }

    class ViewHolder(binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvCityName: TextView
        var tvDescription: TextView
        var tvTemperature: TextView
        var tvHumidity: TextView
        var tvWindSpeed: TextView
        var tvTime: TextView

        init {
            tvCityName = binding.cityName
            tvDescription = binding.tvDescription
            tvHumidity = binding.tvHumidity
            tvTemperature = binding.tvTemp
            tvWindSpeed = binding.windSpeed
            tvTime = binding.tvDayTime
        }
    }
}