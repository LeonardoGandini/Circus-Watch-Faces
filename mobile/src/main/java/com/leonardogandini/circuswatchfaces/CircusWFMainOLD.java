package com.leonardogandini.circuswatchfaces;

import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.leonardogandini.circuswatchfaces.util.IabHelper;
import com.leonardogandini.circuswatchfaces.util.IabResult;
import com.leonardogandini.circuswatchfaces.util.Inventory;
import com.leonardogandini.circuswatchfaces.util.Purchase;


public class CircusWFMainOLD extends ActionBarActivity {

    boolean mIsPremium = false;

    static final String SKU_NOAD = "com.dgwp.circuswatchfaces.removeads";
    //static final String SKU_NOAD = "android.test.purchased";

    IabHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.relativo_cattivita);
//           /*Genera l'ad*/
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        String akiave = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqbichWrlW0mUt8FdQ";
        String akiaveprimo = "/aVZbBYrZ2sDwea3LT2cuwsEQZaNiYt2c1OJplM0tzceWF5F2sP4p/qir+EKfhSgo67eohuPHohdlbnzVnGr0Yjox0csxTz3b";
        String akiavesecondo = "/LjuUhfqyQrYlNzjGCsXlqDFEaM1IcwXalY39";
        String akiaveterzo = "/rzWTvyMU2VZpPys5wIwnyKk8cJkLK3d278kjNYA64Big8xpjfwfbIVBMhqMgZEDF6wShfiRFBHdCg8RDAq+Ec/WE";
        String akiavequarto = "+kWBDI4lyRj8Z7ecsNP5j5I0T8jlUJoX+oXAKJv0kHrv4W8+Th1roonQROnL5PNv8Zr+aAhdeZ/Y4cgsB2gmbSuLN9TNeN00z9Se9wIDAQAB";

        String base64EncodedPublicKey = akiave + akiaveprimo + akiavesecondo + akiaveterzo + akiavequarto;

        mHelper = new IabHelper(this, base64EncodedPublicKey);


        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    return;
                }
                if (mHelper == null) return;
                mHelper.queryInventoryAsync(mGotInventoryListener);
                // Hooray, IAB is fully set up!
            }
        });
    }


    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (mHelper == null) return;

            if (result.isFailure()) {
              //  complain("Failed to query inventory: " + result);
                return;
            }

            Purchase premiumPurchase = inventory.getPurchase(SKU_NOAD);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));

            if (result.isFailure()) {
                // handle error here
            }
            else {
                // does the user have the premium upgrade?
                mIsPremium = inventory.hasPurchase(SKU_NOAD);
                // update UI accordingly
                updateUi();
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

  //  static final String payload = "fdsg4598khdsgfgsffsdgfdg";

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }


            if (purchase.getSku().equals(SKU_NOAD)) {
               /*grazie per aver cacciato la grana!*/
                alert(getString(R.string.cheers));
                mIsPremium = true;
                updateUi();
            }
        }
    };

    public void buttonOnClickGrana(View v) {
        String payload = "";
        mHelper.launchPurchaseFlow(this, SKU_NOAD, 15254605, mPurchaseFinishedListener, payload);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mHelper == null) return;
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    void complain(String message) {
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

public void updateUi() {
    // setContentView(mIsPremium ? R.layout.relativo_libero : R.layout.relativo_cattivita);

    if (mIsPremium){
        setContentView(R.layout.relativo_libero);
        //alert(getString(R.string.cheers));
    }
    else{
        AdViewisIn();
    }


}
    public void AdViewisIn() {
        setContentView(R.layout.relativo_cattivita);
    /*Genera l'ad*/
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
