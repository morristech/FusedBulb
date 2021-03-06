package com.fusedbulb;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.fusedbulb.dialogs.CheckGPSDialog;
import com.fusedbulb.dialogs.PermissionDeniedDialog;
import com.fusedbulb.fonts.FontTypeFace;
import com.fusedbulblib.GetCurrentLocation;
import com.fusedbulblib.interfaces.DialogClickListener;
import com.fusedbulblib.interfaces.GpsOnListner;

/**
 * Created by AnkurYadav on 23-09-2017.
 */

public class MainActivity extends AppCompatActivity implements GpsOnListner{

    TextView currentLocationTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView getLocationTxt=(TextView)findViewById(R.id.getLocationTxt);
        currentLocationTxt=(TextView)findViewById(R.id.currentLocationTxt);
        getLocationTxt.setTypeface(new FontTypeFace(this).MontserratRegular());
        currentLocationTxt.setTypeface(new FontTypeFace(this).MontserratRegular());

        getLocationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetCurrentLocation(MainActivity.this).getCurrentLocation();
            }
        });
    }

    @Override
    public void gpsStatus(boolean _status) {
          if (_status==false){
              new CheckGPSDialog(this).showDialog();
          }else {
              new GetCurrentLocation(MainActivity.this).getCurrentLocation();
          }
    }

    @Override
    public void gpsPermissionDenied(int deviceGpsStatus) {
            if (deviceGpsStatus==1){
                permissionDeniedByUser();
            }else {
                new GetCurrentLocation(MainActivity.this).getCurrentLocation();
            }
    }

    @Override
    public void gpsLocationFetched(Location location) {
        if (location != null) {
            currentLocationTxt.setText(location.getLatitude()+", "+location.getLongitude());
           // currentLocationTxt.setText(new GetAddress(this).fetchCurrentAddress(location));
        } else {
            Toast.makeText(this, getResources().getString(R.string.unable_find_location), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new GetCurrentLocation(MainActivity.this).getCurrentLocation();
                } else {
                    permissionDeniedByUser();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                return;
            }}
    }

    private void permissionDeniedByUser() {

        new PermissionDeniedDialog(this).showDialog(new DialogClickListener() {
            @Override
            public void positiveListener(Activity context, Dialog dialog) {
                new GetCurrentLocation(MainActivity.this).getCurrentLocation();
            }

            @Override
            public void negativeListener(Activity context, Dialog dialog) {
                dialog.dismiss();
            }
        });

    }

}
