package com.leonardogandini.circuswatchfaces;

import android.app.AlertDialog;
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

    //static final String SKU_NOAD = "com.dgwp.circuswatchfaces.removeads";
    static final String SKU_NOAD = "android.test.purchased";
    boolean mIsPremium = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relativo_cattivita);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqbichWrlW0mUt8FdQ/aVZbBYrZ2sDwea3LT2cuwsEQZaNiYt2c1OJplM0tzceWF5F2sP4p/qir+EKfhSgo67eohuPHohdlbnzVnGr0Yjox0csxTz3b/LjuUhfqyQrYlNzjGCsXlqDFEaM1IcwXalY39/rzWTvyMU2VZpPys5wIwnyKk8cJkLK3d278kjNYA64Big8xpjfwfbIVBMhqMgZEDF6wShfiRFBHdCg8RDAq+Ec/WE+kWBDI4lyRj8Z7ecsNP5j5I0T8jlUJoX+oXAKJv0kHrv4W8+Th1roonQROnL5PNv8Zr+aAhdeZ/Y4cgsB2gmbSuLN9TNeN00z9Se9wIDAQAB";

        Log.d(TAG, "Creating IAB helper.");

        mHelper = new IabHelper(this, base64EncodedPublicKey);


        Log.d(TAG, "Starting setup.");


     /*
        IabHelper.QueryInventoryFinishedListener
                mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inventory)
            {
                if (result.isFailure()) {
                    // handle error
                    return;
                }
                String noads =
                        inventory.getSkuDetails(SKU_NOAD).getPrice();
                // update the UI
            }
        };

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    complain("Problem setting up in-app billing: " + result);
                    //setContentView(R.layout.relativo_cattivita);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });*/


        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    setContentView(R.layout.relativo_cattivita);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
                setContentView(R.layout.relativo_libero);
            }
        });

        /*Genera l'ad*/
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }








    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_NOAD);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));


            setContentView(R.layout.relativo_libero);

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
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

    static final String payload = "fdsg4+5983567/khdsgfgsffsdgfdg";

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }


    public void buttonOnClickGrana(View v) {

        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
       //setWaitScreen(true);
       // String payload = "fdsg4598khdsgfgsffsdgfdg";
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

    IabHelper.QueryInventoryFinishedListener mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {
            if (result.isFailure()) {
                // handle error
                return;
            }
            String noads =
                    inventory.getSkuDetails(SKU_NOAD).getPrice();
            // update the UI
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }


    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }


}
