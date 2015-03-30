package com.leonardogandini.circuswatchfaces;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.leonardogandini.circuswatchfaces.util.IabHelper;
import com.leonardogandini.circuswatchfaces.util.IabResult;
import com.leonardogandini.circuswatchfaces.util.Inventory;
import com.leonardogandini.circuswatchfaces.util.Purchase;


public class Nuovo extends Activity {

    boolean mIsPremium = false;

    static final String SKU_NOAD = "com.dgwp.circuswatchfaces.removeads";
  //static final String SKU_NOAD = "android.test.purchased";

    IabHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdViewisIn();

        //String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqbichWrlW0mUt8FdQ/aVZbBYrZ2sDwea3LT2cuwsEQZaNiYt2c1OJplM0tzceWF5F2sP4p/qir+EKfhSgo67eohuPHohdlbnzVnGr0Yjox0csxTz3b/LjuUhfqyQrYlNzjGCsXlqDFEaM1IcwXalY39/rzWTvyMU2VZpPys5wIwnyKk8cJkLK3d278kjNYA64Big8xpjfwfbIVBMhqMgZEDF6wShfiRFBHdCg8RDAq+Ec/WE+kWBDI4lyRj8Z7ecsNP5j5I0T8jlUJoX+oXAKJv0kHrv4W8+Th1roonQROnL5PNv8Zr+aAhdeZ/Y4cgsB2gmbSuLN9TNeN00z9Se9wIDAQAB";

        String akiave = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqbichWrlW0mUt8FdQ";
        String akiaveprimo = "/aVZbBYrZ2sDwea3LT2cuwsEQZaNiYt2c1OJplM0tzceWF5F2sP4p/qir+EKfhSgo67eohuPHohdlbnzVnGr0Yjox0csxTz3b";
        String akiavesecondo = "/LjuUhfqyQrYlNzjGCsXlqDFEaM1IcwXalY39";
        String akiaveterzo = "/rzWTvyMU2VZpPys5wIwnyKk8cJkLK3d278kjNYA64Big8xpjfwfbIVBMhqMgZEDF6wShfiRFBHdCg8RDAq+Ec/WE";
        String akiavequarto = "+kWBDI4lyRj8Z7ecsNP5j5I0T8jlUJoX+oXAKJv0kHrv4W8+Th1roonQROnL5PNv8Zr+aAhdeZ/Y4cgsB2gmbSuLN9TNeN00z9Se9wIDAQAB";

        String base64EncodedPublicKey = akiave + akiaveprimo + akiavesecondo + akiaveterzo + akiavequarto;


        // compute your public key and store it in base64EncodedPublicKey
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









    // Listener that's called when we finish querying the items and subscriptions we own
//    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//
//            if (mHelper == null) return;
//
//            // Is it a failure?
//            if (result.isFailure()) {
//               return;
//            }
//
//
//
//            /*
//             * Check for items we own. Notice that for each purchase, we check
//             * the developer payload to see if it's correct! See
//             * verifyDeveloperPayload().
//             */
//
//            // Do we have the premium upgrade?
//          /*  Purchase premiumPurchase = inventory.getPurchase(SKU_NOAD);
//            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
//
//*/
//
//
//
//            // Do we have the premium upgrade?
//            Purchase premiumPurchase = inventory.getPurchase(SKU_NOAD);
//            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
//            //setContentView(R.layout.relativo_libero);
//        }
//
//
//
//    };


//    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
//            = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result,
//                                             Inventory inventory) {
//
//            if (result.isFailure()) {
//                // handle error here
//                setContentView(R.layout.relativo_cattivita);
//            }
//            else {
//                // does the user have the premium upgrade?
//                mIsPremium = inventory.hasPurchase(SKU_NOAD);
//                // update UI accordingly
//                setContentView(R.layout.relativo_libero);
//            }
//        }
//    };


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

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {


            // if we were disposed of in the meantime, quit.
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
                // bought the premium upgrade!

                alert(getString(R.string.cheers));

                mIsPremium = true;
                updateUi();

            }

        }
    };

    public void buttonOnClickGrana(View v) {


        //setWaitScreen(true);
      //  String payload = "fdsg4598khdsgfgsffsdgfdg";
        String payload = "";
        mHelper.launchPurchaseFlow(this, SKU_NOAD, 15254605, mPurchaseFinishedListener, payload);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mHelper == null) return;
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
          //  setContentView(R.layout.relativo_cattivita);
        }
        else {
           // setContentView(R.layout.relativo_libero);
        }
    }




//    IabHelper.QueryInventoryFinishedListener mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
//        {
//            if (result.isFailure()) {
//                // handle error
//                return;
//            }
//            String noads =
//                    inventory.getSkuDetails(SKU_NOAD).getPrice();
//            // update the UI
//        }
//    };

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mHelper != null) mHelper.dispose();
//        mHelper = null;
//    }
// We're being destroyed. It's important to dispose of the helper here!


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


// updates UI to reflect model
public void updateUi() {
    // update the car color to reflect premium status or lack thereof
    setContentView(mIsPremium ? R.layout.relativo_libero : R.layout.relativo_cattivita);

    /*if (mIsPremium){
        setContentView(R.layout.relativo_libero);
    }
    else{
        AdViewisIn();
    }*/


}
    public void AdViewisIn() {
        setContentView(R.layout.relativo_cattivita);
    /*Genera l'ad*/
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
