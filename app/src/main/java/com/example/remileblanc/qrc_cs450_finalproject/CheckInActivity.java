package com.example.remileblanc.qrc_cs450_finalproject;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CheckInActivity extends AppCompatActivity implements CheckInFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

