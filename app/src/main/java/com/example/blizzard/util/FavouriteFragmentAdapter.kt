package com.example.blizzard.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blizzard.R
import com.example.blizzard.data.entities.WeatherDataEntity
import com.example.blizzard.data.entities.extensions.StringCelsius

/**
 * Created by kelvin_clark on 8/19/2020
 */
class FavouriteFragmentAdapter(private val mContext: Context, private val weatherDataEntities: MutableList<WeatherDataEntity>) : RecyclerView.Adapter<FavouriteFragmentAdapter.ViewHolder>() {
    private val timeUtil = TimeUtil()
    fun insertWeatherEntities(entities: List<WeatherDataEntity>) {
        weatherDataEntities.clear()
        weatherDataEntities.addAll(entities)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.weather_list_item, parent, false)
        return ViewHolder(view)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCityName: TextView
        var tvDescription: TextView
        var tvTemperature: TextView
        var tvHumidity: TextView
        var tvWindSpeed: TextView
        var tvTime: TextView

        init {
            tvCityName = itemView.findViewById(R.id.tv_cityName)
            tvDescription = itemView.findViewById(R.id.tv_description)
            tvHumidity = itemView.findViewById(R.id.tv_humidity)
            tvTemperature = itemView.findViewById(R.id.tv_temp)
            tvWindSpeed = itemView.findViewById(R.id.tv_windSpeed)
            tvTime = itemView.findViewById(R.id.tv_day_time)
        }
    }
}