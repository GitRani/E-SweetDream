package com.example.ryuon.popup.AutoControl;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Blind;
import com.example.ryuon.popup.Module_Object.Plug;

import java.util.ArrayList;

public class AutoPlugActivity extends AppCompatActivity implements  AutoControl, Runnable  {
    ArrayList<Plug> plug;
    ArrayList<Integer> user_temperature = new ArrayList<>();
    ArrayList<Integer> user_humidity = new ArrayList<>();
    ArrayList<Integer> temperature_ud = new ArrayList<>();
    ArrayList<Integer> humidity_ud = new ArrayList<>();
    int temperature;
    int humidity;

    Byte plugNum, onoff;

    public AutoPlugActivity(ArrayList<Plug> plug){
        for (int i = 0; i < plug.size(); i++) {
            user_temperature.add(plug.get(i).getTemperature());
            temperature_ud.add(plug.get(i).getTemperature_ud());
            user_humidity.add(plug.get(i).getHumidity());
            humidity_ud.add(plug.get(i).getHumidity_ud());
        }
        this.plug = plug;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                temperature = BluetoothHelper.temperature;
                humidity = BluetoothHelper.humidity;
                for (int i = 0; i < plug.size(); i++) {
                    if (temperature_ud.get(i) == 1) { // 설정한 온도 이상 유지
                        if (user_temperature.get(i) < temperature) {
                            plugNum = 1;
                            onoff = 0;
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("멀티탭"), plugNum, onoff);
                        } else if (user_temperature.get(i) > temperature) {
                            plugNum = 1;
                            onoff = 1;
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("멀티탭"), plugNum, onoff);
                        }
                    } else { // 설정한 온도 이하 유지
                        if (user_temperature.get(i) < temperature) {
                            plugNum = 1;
                            onoff = 1;
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("멀티탭"), plugNum, onoff);
                        } else if (user_temperature.get(i) > temperature) {
                            plugNum = 1;
                            onoff = 0;
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("멀티탭"), plugNum, onoff);
                        }
                    }
                    if (humidity_ud.get(i) == 1) { // 설정한 습도 이상 유지
                        if (user_humidity.get(i) < humidity) {
                            plugNum = 2;
                            onoff = 0;
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("멀티탭"), plugNum, onoff);
                        } else if (user_humidity.get(i) > humidity) {
                            plugNum = 2;
                            onoff = 1;
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("멀티탭"), plugNum, onoff);
                        }
                    } else { // 설정한 습도 이하 유지
                        if (user_humidity.get(i) < humidity) {
                            plugNum = 2;
                            onoff = 1;
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("멀티탭"), plugNum, onoff);
                        } else if (user_humidity.get(i) > humidity) {
                            plugNum = 2;
                            onoff = 0;
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("멀티탭"), plugNum, onoff);
                        }
                    }
                }
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

}
