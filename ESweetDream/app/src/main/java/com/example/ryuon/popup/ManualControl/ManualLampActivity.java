package com.example.ryuon.popup.ManualControl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Lamp;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

public class ManualLampActivity extends AppCompatActivity implements ManualControl {

    String send_Info;
    ArrayList<String> module_name;
    ArrayList<Lamp> lamp;
    String remember_power = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_lamp);

        module_name = (ArrayList<String>) getIntent().getSerializableExtra("moduleNames");
        lamp = (ArrayList<Lamp>)getIntent().getSerializableExtra("lamp");


    }

    // 버튼 이벤트
    public void moodOff(View view){
        for(int i = 0; i < module_name.size(); i++){
            if(module_name.get(i).contains("무드등")){
                remember_power = "0";
                send_power_Data(i,"0");
            }
        }
    }
    public void moodOn(View view){
        for(int i = 0; i < module_name.size(); i++) {
            if (module_name.get(i).contains("무드등")) {
                remember_power = "1";
                send_power_Data(i,"1");
            }
        }

    }

    void send_power_Data(int deviceIndex, String power) {
        try{
            lamp.get(0).setPower(power);
            send_Info=lamp.get(0).getPower() + lamp.get(0).getWeather() + lamp.get(0).getColor();
            // 데이터 송신
            BluetoothHelper.send_Data(0, send_Info);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        lamp.get(0).setPower(remember_power);
        finish();
    }

}