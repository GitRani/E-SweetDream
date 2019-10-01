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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_lamp);

        // module_name을 가져온 이유: 어떤 아두이노에게 전송할 것인지를 식별하기 위해서
        module_name = (ArrayList<String>) getIntent().getSerializableExtra("moduleNames");
        // 객체유지용
        lamp = (ArrayList<Lamp>)getIntent().getSerializableExtra("lamp");

    }

    // 버튼 이벤트
    public void moodOff(View view){
        // module_name 어레이리스트를 탐색하여 무드등이 포함된 인덱스를 찾는다.
        for(int i = 0; i < module_name.size(); i++){
            if(module_name.get(i).contains("무드등")){
                // 무드등을 찾았으면 그때의 index와 무드등의 전원종료 정보를 send_Power_Data 메소드(바로 아래쪽에 있음)로 넘긴다.
                send_power_Data(i,"0");
            }
        }
    }
    public void moodOn(View view){
        // module_name 어레이리스트를 탐색하여 무드등이 포함된 인덱스를 찾는다.
        for(int i = 0; i < module_name.size(); i++) {
            if (module_name.get(i).contains("무드등")) {
                // 무드등을 찾았으면 그때의 index와 무드등의 전원종료 정보를 send_Power_Data 메소드(바로 아래쪽에 있음)로 넘긴다.
                send_power_Data(i,"1");
            }
        }

    }

    void send_power_Data(int deviceIndex, String power) {
        try{
            lamp.get(0).setPower(power);
            send_Info=lamp.get(0).getPower() + lamp.get(0).getWeather() + lamp.get(0).getColor();
            // BluetoothHelper의 send_Data 메소드로 정보를 보낼 아두이노의 인덱스와 취합된 정보인 send_Info를 전송한다.
            BluetoothHelper.send_Data(deviceIndex, send_Info);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}