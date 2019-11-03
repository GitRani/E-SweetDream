package com.example.ryuon.popup.AutoControl;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Lamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AutoLampActivity extends AppCompatActivity implements AutoControl,Runnable  {

    ArrayList<Lamp> lamp;
    SimpleDateFormat format = new SimpleDateFormat( "HHmm");
    SimpleDateFormat format_sec = new SimpleDateFormat("HHmmss");

    public AutoLampActivity(ArrayList<Lamp> lamp){
        this.lamp = lamp;
    }

    @Override
    public void run(){
        while(true){
            try {
                Calendar time = Calendar.getInstance(); // 실시간으로 시간을 받아온다.

                int current_time = Integer.parseInt(format.format(time.getTime())); // format에 맞게 변환된 시간을 int로 변환(format형식은 전역변수참고)

                try { // 여기 try는 신경안써도 됨. 그냥 format문제 생겼을 때 예외처리한 것.
                    try {
                        // 우선 받아온 취침시간을 HHmm의 Date타입 포맷으로 바꾼다.(시분)
                        Date sleepFormat = new SimpleDateFormat("HHmm").parse(lamp.get(0).get_sleep_time());
                        // 취침시간 - 사용자가 설정한 값 *60000밀리세크(1분) 값이 무드등 전원이 켜지는 시간이 된다.
                        Date change_sleep_time = new Date(sleepFormat.getTime() - (Integer.parseInt(lamp.get(0).get_sleep_time_before()) * 60000));
                        // 바로 앞의 change_sleep_time을 format으로 바꾼후 Integer로 변환한다.

                        for (int i = 0; i < 50; i++) {
                            System.out.println(Integer.parseInt(lamp.get(0).get_sleep_time_before()) * 60000);
                        }
                        // 취침시간
                        int check_sleep_time = Integer.parseInt(format.format(sleepFormat.getTime()));
                        // 취침시간 x시간 전
                        int check_sleep_time_before = Integer.parseInt(format.format(change_sleep_time.getTime()));

                        // 현재시간이 취침시간 x시간 전이라면 마냥 1초 주기마다 시간을 받을수는 없겠지? ㅎㅎ
                        if (current_time < check_sleep_time_before) {
                            // 현재시간을 string으로 바꿈
                            String current_time_toString = format_sec.format(time.getTime());
                            // 취침시간 x시간 전을 string으로 바꿈
                            String sleep_time_before_toString = format_sec.format(change_sleep_time.getTime());

                            // for debugging test
                            try {
                                // 취침시간 x시간 전 - 현재시간만큼 재운다.(1초동안 계속 현재시간을 받으면 과부하가 생길 수 있으므로)
                                Thread.sleep(timeDiff_plusSec(current_time_toString, sleep_time_before_toString));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        Calendar time_for_check = Calendar.getInstance(); // sleep에서 깬 후에 현재시간을 한번 더 확인해야 한다.

                        current_time = Integer.parseInt(format.format(time_for_check.getTime())); // format에 맞게 변환된 시간을 int로 변환(format형식은 전역변수참고)

                        // 조건을 만족하기 위해서는 1. 현재시간 = 취침시간 - x (이때는, 취침시간 x시간 전에 자동스위치를 켠 경우
                        // 2. 취침시간 - x <= 현재 시간 <= 취침시간 (이때는, 취침시간 x시간 전 ~ 취침시간 사이에 자동스위치를 켠 경우)
                        if (current_time == check_sleep_time_before || (check_sleep_time_before <= current_time && current_time <= check_sleep_time)) {
                            // 조건을 만족하면 무드등이 켜진다. 참고로 아래의 send_Info에는 setter로 저장한 값을 넣을 예정.
                            // 여러번 코드를 넣은 것은 네오스트립 LED 상태가 안좋아서 넣은것. 상태가 정상이면 하나만 넣어도 됨.

                            BluetoothHelper.send_Data(0, "1" + lamp.get(0).getWeather() + lamp.get(0).getColor());

                            // 현재시간과 취침시간을 String으로 변환한다. (timediff 메소드에 넣어서 시간차를 구하기 위한 작업)
                            try {
                                // 현재시간 String변환 작업
                                Calendar time_recent = Calendar.getInstance(); // 실시간으로 시간을 한번 더 받아온다.

                                String current_time_recent = format_sec.format(time_recent.getTime());
                                for (int i = 0; i < 100; i++) {
                                    System.out.println(current_time_recent);
                                    System.out.println(current_time);
                                }
                                // 취침시간 String변환 작업
                                String change_string_check_sleep_time = lamp.get(0).get_sleep_time() + "00";


                                Thread.sleep(timeDiff_plusSec(current_time_recent, change_string_check_sleep_time));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // 1시간이 되면 무드등이 꺼지고 기상시간 때까지 다시 sleep에 빠진다.
                            BluetoothHelper.send_Data(0, "0" + lamp.get(0).getWeather() + lamp.get(0).getColor());

                            try {
//                            // sleep메소드 안의 timeDiff는 시간차이 값을 반환해준다.
//                            for(int i = 0; i < 30; i++){
//                                System.out.println("********"+timeDiff(lamp.get(0).get_sleep_time(),lamp.get(0).get_wake_time()));
//                            }
                                Thread.sleep(timeDiff(lamp.get(0).get_sleep_time(), lamp.get(0).get_wake_time()));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // 기상시간이 되면 전원이 다시 켜진다(x시간 동안 유지).
                            BluetoothHelper.send_Data(0, "1" + lamp.get(0).getWeather() + lamp.get(0).getColor());

                            try {
                                // 사용자가 정한 값만큼 sleep
                                Thread.sleep(Integer.parseInt(lamp.get(0).get_sleep_time_before()) * 60000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // 전원종료
                            BluetoothHelper.send_Data(0, "0" + lamp.get(0).getWeather() + lamp.get(0).getColor());

                        } else {
                            // "(사용자가 입력한시간 - 1H) = 현재시간" 을 만족하지 않는다면 꺼진상태유지
                            BluetoothHelper.send_Data(0, "0" + lamp.get(0).getWeather() + lamp.get(0).getColor());
                                // 1초간격으로 맞는지 체크 (이제 하는 역할이 거의 없어짐
                                Thread.sleep(1000);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } // while
    } // run() 메소드의 끝


    // 기상시간과 취침시간의 차이를 구하기 위한 메소드
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

    // 기상시간과 취침시간의 차이를 구하기 위한 메소드(+초단위까지 포함)
    public static long timeDiff_plusSec(String sleep_time, String wake_time) {
        long timediff;
        // 받은 시간에 초를 붙였다.
        String check_sleep_time = sleep_time;
        String check_wake_time = wake_time;

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