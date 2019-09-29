package com.example.ryuon.popup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Lamp;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {

    ArrayList<Lamp> lamp;
    String send_Info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        lamp = (ArrayList<Lamp>)getIntent().getSerializableExtra("lamp");




    }

    public void onRedButtonClicked(View view){
        send_color_Data("1");
        System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
    }
    public void onOrangeButtonClicked(View view){

    }
    public void onYellowButtonClicked(View view){
        send_color_Data("3");
    }

    public void onGreenButtonClicked(View view){

    }

    public void onBlueButtonClicked(View view){
        send_color_Data("5");
    }

    void send_color_Data(String color) {
        try{
            lamp.get(0).setColor(color);
            send_Info=lamp.get(0).getPower()+lamp.get(0).getWeather()+lamp.get(0).getColor();
            // 데이터 송신
            BluetoothHelper.send_Data(0, send_Info);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    //취소 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}