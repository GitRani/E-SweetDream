package com.example.ryuon.popup.AutoControl;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Blind;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AutoBlindActivity extends AppCompatActivity implements  AutoControl, Runnable{

    ArrayList<Blind> blind;
    ArrayList<Integer> user_lux = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat( "HHmm");
    int lux;

    public AutoBlindActivity(ArrayList<Blind> blind){
        for (int i = 0; i < blind.size(); i++) {
            user_lux.add(blind.get(i).getlux());
        }
        this.blind = blind;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Calendar time = Calendar.getInstance(); // 실시간으로 시간을 받아온다.

            int current_time = Integer.parseInt(format.format(time.getTime())); // format에 맞게 변환된 시간을 int로 변환(format형식은 전역변수참고)

            try {
                lux = (int) BluetoothHelper.lux;
                Date sleepFormat = new SimpleDateFormat( "HHmm").parse(blind.get(0).get_sleep_time());
                Date change_sleep_time = new Date(sleepFormat.getTime());

                int check_sleep_time = Integer.parseInt(format.format(change_sleep_time.getTime()));

                if (check_sleep_time == current_time) {
                    BluetoothHelper.send_Data(BluetoothHelper.findingIndex("블라인드"), "0");
                    Thread.sleep(timeDiff(blind.get(0).get_sleep_time(),blind.get(0).get_wake_time()));
                } else {
                    for (int i = 0; i < blind.size(); i++) {
                        if (user_lux.get(i) > lux) {
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("블라인드"), "1");
                        } else if (user_lux.get(i) < lux) {
                            BluetoothHelper.send_Data(BluetoothHelper.findingIndex("블라인드"), "2");
                        }
                    }
                }
                Thread.sleep(3000); //센서모듈이 값을 다시 받아올때 까지 스레드 중지
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public static long timeDiff(String sleep_time, String wake_time) {
        long timediff;
        // 받은 시간에 초를 붙였다.
        String check_sleep_time = sleep_time + "00";
        String check_wake_time = wake_time + "00";
        // 취침시간 제작 (앞에 연도월일을 붙여야 하기때문에 임시로 사용하는 포맷이다.)
        SimpleDateFormat temporary_format = new SimpleDateFormat("yyMMdd");
        // 현재 시간을 입력받는다.
        Calendar date = Calendar.getInstance();
        // 현재의 년월일을 취침시간과 기상시간에 붙인다. 타입변환은 Date -> Long -> String
        // 굳이 골치아프게 한 이유는 만약 00:00:00 을 기준으로 취침시간이 그 전이면 문제가 안되지만
        // 기상시간이 00:00:00 이후이면 하루가 지난것이므로 년월일에서 "일"에 1을 더해주어야 하기 때문이다.
        // 만약 취침시간 값이 더 크다면(= 00:00을 기준으로 취침시간이 00:00 이전, 기상시간이 00:00 이후)
        if(Long.parseLong(check_sleep_time) > Long.parseLong(check_wake_time)){
            // wake_time 앞에 년월(일+1)을 붙임
            check_sleep_time = Long.toString(Long.parseLong(temporary_format.format(date.getTime()))) + check_sleep_time;
            check_wake_time = Long.toString(Long.parseLong(temporary_format.format(date.getTime())) + 1) + check_wake_time;
        }else{
            // wake_time 앞에 년월일을 붙임
            check_sleep_time = Long.toString(Long.parseLong(temporary_format.format(date.getTime()))) + check_sleep_time;
            check_wake_time = Long.toString(Long.parseLong(temporary_format.format(date.getTime()))) + check_wake_time;
        }
        // 본격적으로 시간의 차이를 구하기 위한 포맷
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");

        Date wake_time_only = null;
        Date sleep_time_only = null;
        try {
            // date로 변환
            wake_time_only = format.parse(check_wake_time);
            sleep_time_only = format.parse(check_sleep_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 시간차 계산 (ms로 계산)
        timediff = (wake_time_only.getTime() - sleep_time_only.getTime());

        return timediff;
    }



}