package com.example.ryuon.popup.AutoControl;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Lamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AutoLampActivity extends AppCompatActivity implements AutoControl,Runnable  {

    ArrayList<Lamp> lamp;
    SimpleDateFormat format = new SimpleDateFormat( "HHmm");

    public AutoLampActivity(ArrayList<Lamp> lamp){
        this.lamp = lamp;
    }


    @Override
    public void run(){
        while(true){
            Calendar time = Calendar.getInstance(); // 실시간으로 시간을 받아온다.
            int current_date = Integer.parseInt(format.format(time.getTime())); // format에 맞게 변환된 시간을 int로 변환(format형식은 전역변수참고)

            try { // 여기 try는 신경안써도 됨. 그냥 format문제 생겼을 때 예외처리한 것.
                int check = Integer.parseInt(lamp.get(0).get_sleep_time()); // timer라는 스트링 타입의 리스트에서 4번째 즉, 시분초가 합쳐진 값을 int로 변환한다.

                System.out.println(current_date +"*********************");
                System.out.println(check +"******************");

                if (current_date == check) { // 사용자가 입력한시간 1시간 전에 무드등이 켜지도록 할 예정.
                    // 조건을 만족하면 무드등이 켜진다. 참고로 아래의 send_Info에는 setter로 저장한 값을 넣을 예정.
                    BluetoothHelper.send_Data(0,"121");

                    try {
                        // 1시간동안 무드등이 켜져있음.
                        Thread.sleep(360000);
                        // 무드등을 (취침시간-1h)만큼 켠다음 취침시간이 되면 종료하고 다음 기상시간만큼 sleep을 하도록 계산생각.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 1시간이 되면 무드등이 꺼지고 기상시간 때까지 다시 sleep에 빠진다.
                    BluetoothHelper.send_Data(0,"021");
                } else {
                    BluetoothHelper.send_Data(0,"021");;
                    try {
                        // 기상시간과 취침시간 사이의 시간값을 계산하여 Thread에 넣을 생각.
                        Thread.sleep(2000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
