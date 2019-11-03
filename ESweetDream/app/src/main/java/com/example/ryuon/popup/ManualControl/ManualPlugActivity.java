package com.example.ryuon.popup.ManualControl;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Plug;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

public class ManualPlugActivity extends AppCompatActivity implements ManualControl {

    ArrayList<Plug> plug;
    int deviceIndex;
    Byte plugNum, onoff;

    Button plug1on;
    Button plug1off;
    Button plug2on;
    Button plug2off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_plug);

        plug = (ArrayList<Plug>)getIntent().getSerializableExtra("plug");

        deviceIndex = BluetoothHelper.findingIndex("멀티탭");

        plug1on = (Button) findViewById(R.id.plug1on);
        plug1off = (Button) findViewById(R.id.plug1off);
        plug2on = (Button) findViewById(R.id.plug2on);
        plug2off = (Button) findViewById(R.id.plug2off);



    }

    public void plug1On(View view) {
        plug1on.setTextColor(Color.RED);
        plug1off.setTextColor(Color.BLACK);
        plugNum = 1;
        onoff   = 1;
        BluetoothHelper.send_Data(deviceIndex, plugNum, onoff);
    }

    public void plug1Off(View view) {
        plug1on.setTextColor(Color.BLACK);
        plug1off.setTextColor(Color.RED);
        plugNum = 1;
        onoff   = 0;
        BluetoothHelper.send_Data(deviceIndex, plugNum, onoff);
    }

    public void plug2On(View view) {
        plug2on.setTextColor(Color.RED);
        plug2off.setTextColor(Color.BLACK);
        plugNum = 2;
        onoff   = 1;
        BluetoothHelper.send_Data(deviceIndex, plugNum, onoff);
    }

    public void plug2Off(View view) {
        plug2on.setTextColor(Color.BLACK);
        plug2off.setTextColor(Color.RED);
        plugNum = 2;
        onoff   = 0;
        BluetoothHelper.send_Data(deviceIndex, plugNum, onoff);
    }

}
