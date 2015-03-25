package com.leonardogandini.circuswatchfaces;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.leonardogandini.circuswatchfaces.util.IabHelper;
import com.leonardogandini.circuswatchfaces.util.IabResult;
import com.leonardogandini.circuswatchfaces.util.Inventory;
import com.leonardogandini.circuswatchfaces.util.Purchase;


public class CircusMain extends ActionBarActivity {

    static final String TAG = "CircusWatch";
    IabHelper mHelper;

    static final String SKU_NOAD = "com.dgwp.circuswatchfaces.removeads";
    //static final String SKU_NOAD = "android.test.purchased";
    boolean mIsPremium = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relativo_cattivita);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqbichWrlW0mUt8FdQ/aVZbBYrZ2sDwea3LT2cuwsEQZaNiYt2c1OJplM0tzceWF5F2sP4p/qir+EKfhSgo67eohuPHohdlbnzVnGr0Yjox0csxTz3b/LjuUhfqyQrYlNzjGCsXlqDFEaM1IcwXalY39/rzWTvyMU2VZpPys5wIwnyKk8cJkLK3d278kjNYA64Big8xpjfwfbIVBMhqMgZEDF6wShfiRFBHdCg8RDAq+Ec/WE+kWBDI4lyRj8Z7ecsNP5j5I0T8jlUJoX+oXAKJv0kHrv4W8+Th1roonQROnL5PNv8Zr+aAhdeZ/Y4cgsB2gmbSuLN9TNeN00z9Se9wIDAQAB";

        Log.d(TAG, "Creating IAB helper.");

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                   // complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        /*Genera l'ad*/
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }


    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                setContentView(R.layout.relativo_cattivita);

            }
            else {
                // suupa powa?
                mIsPremium = inventory.hasPurchase(SKU_NOAD);
                setContentView(R.layout.relativo_libero);
            }
        }
    };


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


    public void buttonOnClickGrana(View v) {

        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
       //setWaitScreen(true);
        String payload = "fdsg4598khdsgfgsffsdgfdg";
        mHelper.launchPurchaseFlow(this, SKU_NOAD, 15254605, mPurchaseFinishedListener, payload);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            setContentView(R.layout.relativo_cattivita);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
            setContentView(R.layout.relativo_libero);
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        //String payload = p.getDeveloperPayload();
        String payload = "fdsg4598khdsgfgsffsdgfdg";
        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                setContentView(R.layout.relativo_cattivita);
            }
            else if (purchase.getSku().equals(SKU_NOAD)) {
                setContentView(R.layout.relativo_libero);
            }
        }
    };








}
