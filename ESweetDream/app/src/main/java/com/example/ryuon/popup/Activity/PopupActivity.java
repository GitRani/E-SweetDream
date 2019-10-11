package com.example.ryuon.popup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Group;
import com.example.ryuon.popup.Module_Object.Lamp;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {

    ArrayList<Lamp> lamp;
    String send_Info;
    Group receivedData_selected_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        receivedData_selected_group = (Group) getIntent().getSerializableExtra("selectedGroup");
        lamp = (ArrayList<Lamp>)getIntent().getSerializableExtra("lamp");




    }

    public void onRedButtonClicked(View view){
        Intent intent = new Intent(this,GroupControlActivity.class);
        intent.putExtra("selectedGroup", receivedData_selected_group);
        setResult(100, intent);
        finish();
    }
    public void onYellowButtonClicked(View view){
        Intent intent = new Intent(this,GroupControlActivity.class);
        intent.putExtra("selectedGroup", receivedData_selected_group);
        setResult(101, intent);
        finish();
    }
    public void onBlueButtonClicked(View view){
        Intent intent = new Intent(this,GroupControlActivity.class);
        intent.putExtra("selectedGroup", receivedData_selected_group);
        setResult(102, intent);
        finish();
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