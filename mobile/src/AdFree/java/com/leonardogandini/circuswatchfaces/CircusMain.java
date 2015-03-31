package com.leonardogandini.circuswatchfaces;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CircusMain extends ActionBarActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relativo_libero_noad);


        /*Genera l'ad*/
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void buttonOnClick(View v){
        Uri uri = Uri.parse("https://leonardogandini.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    public void buttonOnClickroberta(View v){
        Uri uri = Uri.parse("http://robertapagnoni.it");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }



}
