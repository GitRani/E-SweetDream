package com.example.ryuon.popup.AutoControl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Activity.GroupControlActivity;
import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.Module_Object.Group;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

public class AutoControlConditionActivity extends AppCompatActivity{

    ArrayList<Integer> hour = new ArrayList<>();
    ArrayList<Integer> minute = new ArrayList<>();
    ArrayList<Integer> temperature = new ArrayList<>();
    ArrayList<Integer> percent = new ArrayList<>();
    ArrayList<Integer> sleep_before_value = new ArrayList<>();

    String sleep_hour_value;
    String sleep_minute_value;
    String sleep_time_before_item;
    String wake_hour_value;
    String wake_minute_value;
    String temperature_item;
    String humidity_item;
    String lux_item;
    String temperature_ud = "0";
    String humidity_ud = "0";
    // 위의 String 정보를 모두 담아 보낸다.
    Group receivedData_selected_group;

    ArrayList<String> auto_condition_info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_control_condition);

        receivedData_selected_group = (Group) getIntent().getSerializableExtra("selectedGroup");
//        bluetoothHelper = (BluetoothHelper) getIntent().getSerializableExtra("bluetoothHelper");


        for(int i = 0; i < 24; i++){
            hour.add(i);
        }
        for(int i = 0; i <= 60; i++){
            minute.add(i);
        }

        for(int i = 10; i < 31; i++){
            temperature.add(i);
        }

        for(int i = 0; i < 101; i++){
            percent.add(i);
        }

        for(int i = 60; i >= 5; i = i - 5){
            sleep_before_value.add(i);
        }
        sleep_before_value.add(1);


        final Spinner sleep_hour = (Spinner)findViewById(R.id.sleep_hour);
        ArrayAdapter<Integer> sleep_hour_adpater = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,hour);
        sleep_hour_adpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleep_hour.setAdapter(sleep_hour_adpater);
        sleep_hour.setSelection(hour.size()/2);

        final Spinner sleep_minute = (Spinner)findViewById(R.id.sleep_minute);
        ArrayAdapter<Integer> sleep_minute_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,minute);
        sleep_minute_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleep_minute.setAdapter(sleep_minute_adapter);
        sleep_minute.setSelection(minute.size()/2);

        final Spinner sleep_time_before_value = (Spinner)findViewById(R.id.sleep_time_before_value);
        ArrayAdapter<Integer> sleep_time_before_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,sleep_before_value);
        sleep_hour_adpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleep_time_before_value.setAdapter(sleep_time_before_adapter);
        sleep_time_before_value.setSelection(sleep_before_value.size()/2);

        final Spinner wake_hour = (Spinner)findViewById(R.id.wake_hour);
        ArrayAdapter<Integer> wake_hour_adpater = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,hour);
        wake_hour_adpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wake_hour.setAdapter(wake_hour_adpater);
        wake_hour.setSelection(hour.size()/2);

        final Spinner wake_minute = (Spinner)findViewById(R.id.wake_minute);
        ArrayAdapter<Integer> wake_minute_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,minute);
        wake_minute_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wake_minute.setAdapter(wake_minute_adapter);
        wake_minute.setSelection(minute.size()/2);

        final Spinner temperature_value = (Spinner)findViewById(R.id.temperature);
        final ArrayAdapter<Integer> temperature_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,temperature);
        temperature_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temperature_value.setAdapter(temperature_adapter);
        temperature_value.setSelection(0);

        final ToggleButton temperature_toggle = (ToggleButton) findViewById(R.id.temperature_ud);
        temperature_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temperature_toggle.isChecked()) {
                    temperature_ud = "1";
                } else {
                    temperature_ud = "0";
                }
            }
        });


        final Spinner humidity_value = (Spinner)findViewById(R.id.humidity);
        ArrayAdapter<Integer> humidity_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,percent);
        humidity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        humidity_value.setAdapter(humidity_adapter);
        humidity_value.setSelection(0);

        final ToggleButton humidity_toggle = (ToggleButton) findViewById(R.id.humidity_ud);
        humidity_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (humidity_toggle.isChecked()) {
                    humidity_ud = "1";
                } else {
                    humidity_ud = "0";
                }
            }
        });


        final Spinner lux_value = (Spinner)findViewById(R.id.lux);
        ArrayAdapter<Integer> lux_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,percent);
        lux_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lux_value.setAdapter(lux_adapter);
        lux_value.setSelection(50);

        sleep_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                sleep_hour_value =  sleep_hour.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        sleep_minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                sleep_minute_value = sleep_minute.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        sleep_time_before_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                sleep_time_before_item = sleep_time_before_value.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        wake_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                wake_hour_value = wake_hour.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        wake_minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                wake_minute_value =  wake_minute.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        temperature_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                temperature_item = temperature_value.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        humidity_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                humidity_item = humidity_value.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

        lux_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                lux_item = lux_value.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        makeInfo();
        Intent intent = new Intent(this,GroupControlActivity.class);
        intent.putExtra("selectedGroup", receivedData_selected_group);
        intent.putExtra("go_GroupControl", auto_condition_info);
//        intent.putExtra("bluetoothHelper", bluetoothHelper);
        setResult(RESULT_OK, intent);
        finish();

        return super.onOptionsItemSelected(item);
    }


    public void makeInfo(){
        if(sleep_hour_value.length() == 1){
            auto_condition_info.add("0" + sleep_hour_value);
        }
        else{
            auto_condition_info.add(sleep_hour_value);
        }

        if(sleep_minute_value.length() == 1){
            auto_condition_info.add("0" + sleep_minute_value);
        }
        else{
            auto_condition_info.add(sleep_minute_value);
        }

        if(wake_hour_value.length() == 1){
            auto_condition_info.add("0" + wake_hour_value);
        }
        else{
            auto_condition_info.add(wake_hour_value);
        }

        if(wake_minute_value.length() == 1){
            auto_condition_info.add("0" + wake_minute_value);
        }
        else{
            auto_condition_info.add(wake_minute_value);
        }

        if(sleep_time_before_item.length() == 1){
            auto_condition_info.add("0" + sleep_time_before_item);
        }else{
            auto_condition_info.add(sleep_time_before_item);
        }

        if(temperature_item.length() == 1){
            auto_condition_info.add("0"+ temperature_item);
        }
        else{
            auto_condition_info.add(temperature_item);
        }
        auto_condition_info.add(temperature_ud);

        if(humidity_item.length() == 1){
            auto_condition_info.add("00" + humidity_item);
        }
        else if(humidity_item.length() == 2){
            auto_condition_info.add("0" + humidity_item);
        }
        else{
            auto_condition_info.add(humidity_item);
        }
        auto_condition_info.add(humidity_ud);

        if(lux_item.length() == 1){
            auto_condition_info.add("00" + lux_item);
        }
        else if(lux_item.length() == 2){
            auto_condition_info.add("0" + lux_item);
        }
        else{
            auto_condition_info.add(lux_item);
        }
    }

    @Override
    public void onBackPressed() {
        CharSequence text="자동제어조건을 설정하세요";
        Toast toast=Toast.makeText(this,text,Toast.LENGTH_LONG);
        toast.show();
    }


}