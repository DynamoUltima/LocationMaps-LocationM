package com.example.joel.locationmaps;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tapadoo.alerter.Alerter;

public class MainActivity extends AppCompatActivity {

    private TextView textCoordinates;
    private TextView PlaceName;

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (isServicesOK()) {
            init();
        }

        textCoordinates = findViewById(R.id.textCoordinates);
        PlaceName = findViewById(R.id.localName);


    }

    private void init() {

        findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make request
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "an error ocured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                String placeResult = data.getStringExtra("placeName");

                Alerter.create(this)
                        .setTitle(placeResult)
                        .setText(result)
                        .setIcon(R.drawable.ic_place)
                        .setBackgroundColorRes(R.color.colorPrimaryDark)
                        .setDuration(10000)
                        .enableSwipeToDismiss()
                        .show();
//                textCoordinates.setText(result);
//                PlaceName.setText(placeResult);
            }
            if (resultCode == RESULT_CANCELED) {
                textCoordinates.setText("No Coordinates");
                PlaceName.setText("No Locction Name");
            }
        }
    }


//    public void showAlerter(View v) {
//        Alerter.create(this)
//                .setTitle("Title")
//                .setText("Specific Location")
//                .show();
//    }
}
