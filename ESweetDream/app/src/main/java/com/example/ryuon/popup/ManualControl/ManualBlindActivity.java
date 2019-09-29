package com.example.ryuon.popup.ManualControl;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.R;

public class ManualBlindActivity extends AppCompatActivity implements ManualControl {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_blind);
    }
}
