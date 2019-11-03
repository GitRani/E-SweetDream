package com.example.ryuon.popup.ManualControl;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

public class ManualBlindActivity extends AppCompatActivity implements ManualControl {
    ArrayList<String> module_name;
//    Byte data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_blind);

        module_name = (ArrayList<String>) getIntent().getSerializableExtra("moduleNames");

    }

    public void BlindUp(View view) {
//        data = 1;
        int deviceIndex = BluetoothHelper.findingIndex("블라인드");
        BluetoothHelper.send_Data(deviceIndex, '1');
    }

    public void BlindDown(View view) {
//        data = 2;
        int deviceIndex = BluetoothHelper.findingIndex("블라인드");
        BluetoothHelper.send_Data(deviceIndex, '2');
    }
}