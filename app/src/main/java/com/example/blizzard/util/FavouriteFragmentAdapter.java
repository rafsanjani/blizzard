package com.example.blizzard.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blizzard.R;
import com.example.blizzard.data.entities.WeatherDataEntity;

import java.util.List;

/**
 * Created by kelvin_clark on 8/19/2020
 */
public class FavouriteFragmentAdapter extends RecyclerView.Adapter<FavouriteFragmentAdapter.ViewHolder> {
    private List<WeatherDataEntity> weatherDataEntities;
    private Context mContext;

    public FavouriteFragmentAdapter(Context mContext, List<WeatherDataEntity> weatherDataEntities) {
        this.mContext = mContext;
        this.weatherDataEntities = weatherDataEntities;
    }

    public void insertWeatherEntities(List<WeatherDataEntity> entities) {

        weatherDataEntities.clear();
        weatherDataEntities.addAll(entities);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherDataEntity entity = weatherDataEntities.get(position);
        String cityName = entity.getCityName() + ", " + entity.getCountry();
        holder.tvCityName.setText(cityName);
        holder.tvDescription.setText(entity.getDescription());
        String humidity = entity.getHumidity() + "%";
        holder.tvHumidity.setText(humidity);
        holder.tvTemperature.setText(TempConverter.kelToCelsius(entity.getTemperature()));
    }

    @Override
    public int getItemCount() {
        return weatherDataEntities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName;
        TextView tvDescription;
        TextView tvTemperature;
        TextView tvHumidity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tv_cityName);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvHumidity = itemView.findViewById(R.id.tv_humidity);
            tvTemperature = itemView.findViewById(R.id.tv_temp);
        }
    }
}
