package com.islamiat.blackwallpapers.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.adapter.OSLicenseAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OSLicenseActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oslicense);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> list = getOSLicenses();

        /*for (String s: list){
            Log.e("TAG", "OS License: "+ s);
        }*/

        OSLicenseAdapter adapter = new OSLicenseAdapter(this, list);
        recyclerView.setAdapter(adapter);


    }

    private List<String> getOSLicenses(){

        try {
            String[] list = getAssets().list("oslicense");

            return Arrays.asList(list);

        } catch (IOException e) {
            //e.printStackTrace();
        }

        return new ArrayList<>();

    }
}
