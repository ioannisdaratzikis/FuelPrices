package com.tappgames.fuelprices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.ArrayList;

/**
 * Created by johndaratzikis on 25/03/2017.
 */



public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {

    private ArrayList<GasStation> mstationData;
    private final StationAdapterOnClickHandler mClickHandler;

    public interface StationAdapterOnClickHandler {
        void onClick(String stationAddress);
    }

    public StationAdapter(StationAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class StationViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final TextView stationTextView;

        public StationViewHolder(View itemView) {
            super(itemView);
            stationTextView = (TextView) itemView.findViewById(R.id.tv_item_station);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String stationAdress = mstationData.get(adapterPosition).getAddress().toString();
            mClickHandler.onClick(stationAdress);
        }
    }


    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.station_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder, int position) {
        GasStation station = mstationData.get(position);
        holder.stationTextView.setText("Name: " + station.getName() + "\n" + "Address: " + station.getAddress() + "\n" + "Price: " + station.getPrice());
    }

    @Override
    public int getItemCount() {
        if (null == mstationData) return 0;
        return mstationData.size();
    }

    public void setStationData(ArrayList<GasStation> stationData) {
        mstationData = stationData;
        notifyDataSetChanged();
    }
}


