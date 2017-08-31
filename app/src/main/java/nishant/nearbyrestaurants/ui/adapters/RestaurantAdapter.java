package nishant.nearbyrestaurants.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import nishant.nearbyrestaurants.R;
import nishant.nearbyrestaurants.models.Place;
import nishant.nearbyrestaurants.utils.Functions;

/**
 * Created by nishant pardamwar on 31/8/17.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Place> list;
    private Context context;

    public RestaurantAdapter(Context context, List<Place> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vh_place, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.placeName.setText(list.get(position).getName());
        holder.placeVicinity.setText(list.get(position).getVicinity());
        holder.placeRating.setText(list.get(position).getRating());
        double dist = list.get(position).getDistance();
        if (dist < 1000) {
            holder.placeDistance.setText(String.format("%.2f", list.get(position).getDistance()) + " m");
        } else {
            holder.placeDistance.setText(String.format("%.2f", list.get(position).getDistance() / 1000) + " km");
        }

        String iconUrl = list.get(position).getIconUrl();
        if (Functions.isStringValid(iconUrl)) {
            Glide.with(context).load(iconUrl).asBitmap().placeholder(R.drawable.placeholder).into(holder.placeIcon);
        } else {
            holder.placeIcon.setImageResource(R.drawable.placeholder);
        }
    }

    public void setNewList(List<Place> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView placeName, placeVicinity, placeRating, placeDistance;
        private ImageView placeIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            placeName = (TextView) itemView.findViewById(R.id.tv_place_name);
            placeDistance = (TextView) itemView.findViewById(R.id.tv_place_distance);
            placeVicinity = (TextView) itemView.findViewById(R.id.tv_place_vicinity);
            placeRating = (TextView) itemView.findViewById(R.id.tv_place_rating);
            placeIcon = (ImageView) itemView.findViewById(R.id.tv_place_icon);
        }
    }
}
