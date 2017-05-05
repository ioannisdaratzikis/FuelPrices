package com.tappgames.fuelprices;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StationAdapter.StationAdapterOnClickHandler, NumberPicker.OnValueChangeListener {

    private StationAdapter mAdapter;
    private RecyclerView mStationsList;
    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStationsList = (RecyclerView) findViewById(R.id.rv_gasStations);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mStationsList.setLayoutManager(layoutManager);

        mStationsList.setHasFixedSize(true);

        mAdapter = new StationAdapter(this);

        mStationsList.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        show();

        //new getDocument().execute();

    }

    public void show()
    {

        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Location");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.btn_select);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(4); // max value 100
        np.setMinValue(0);   // min value 0
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setDisplayedValues( new String[] { "Αθήνα", "Θεσσαλονίκη", "Πάτρα", "Ηράκλειο", "Λάρισα" } );
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                new getDocument().execute(np.getValue());
                d.dismiss();
            }
        });
        d.show();


    }

    @Override
    public void onClick(String stationAddress) {
        Uri geoLocation = Uri.parse("geo:0,0?q=" + stationAddress);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        //System.out.print("button cliked");
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }


    private class getDocument extends AsyncTask<Integer,Void,Void>{

        private ArrayList<GasStation> gasStations = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            String town = "";
            if (params[0] == 0){
                town = "athina";
            }else if (params[0] == 1){
                town = "thessaloniki";
            }else if (params[0] == 2){
                town = "patra";
            }else if (params[0] == 3){
                town = "irakleio";
            }else if (params[0] == 4){
                town = "larisa";
            }

            try {
                Document doc = Jsoup.connect("http://www.vrisko.gr/times-kafsimon-venzinadika/" + town).get();
                Elements prices = doc.select("div.selected > b");
                Elements companies = doc.select("div.GasCompanyName");
                Elements addrees = doc.select("div.GasAddress");
                for(int i=0; i<prices.size(); i++){
                    GasStation a = new GasStation();
                    a.setPrice(prices.get(i).text());
                    a.setName(companies.get(i).text());
                    a.setAddress(addrees.get(i).text());
                    gasStations.add(a);
                }

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (gasStations != null) {
                // COMPLETED (45) Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather data
                mAdapter.setStationData(gasStations);
            }

        }
    }

}


