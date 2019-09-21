package com.x.unncrimewatch.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.x.unncrimewatch.R;

public class EmergencyFragment extends Fragment {

    private ImageView callAE, callWard, callFire;
    private TextView AEnumber, wardNumber, fireNumber;
    private String AEnumber_s, wardNumber_s, fireNumber_s, tel;

    private static final int REQUEST_CALL = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.emergency, container, false);

        getActivity().setTitle("Emergency");

        AEnumber = layout.findViewById(R.id.numberAE);
        AEnumber_s = AEnumber.getText().toString();

        wardNumber = layout.findViewById(R.id.numberWard);
        wardNumber_s = wardNumber.getText().toString();

        fireNumber = layout.findViewById(R.id.numberFire);
        fireNumber_s = fireNumber.getText().toString();


        callAE = layout.findViewById(R.id.callAE);
        callAE.setOnClickListener(view ->
                makeCall(AEnumber_s)
        );

        callWard = layout.findViewById(R.id.callWard);
        callWard.setOnClickListener(view ->
                makeCall(wardNumber_s)
        );

        callFire = layout.findViewById(R.id.callFire);
        callFire.setOnClickListener(view ->
                makeCall(fireNumber_s)
        );


        // Inflate the layout for this fragment
        return layout;

    }


    private void makeCall(String number) {
        tel = "tel:" + number;
        Log.i("Make call", "");
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse(tel));
        try {
            if (ActivityCompat.checkSelfPermission(getView().getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                return;
            }
            startActivity(phoneIntent);
            Log.i("Finished making a call", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getView().getContext(), "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CALL) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                return;


            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getView().getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

