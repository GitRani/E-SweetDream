package com.example.ryuon.popup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Bluetooth.BluetoothHelper;
import com.example.ryuon.popup.DB.DBOpenHelper;
import com.example.ryuon.popup.Module_Object.Blind;
import com.example.ryuon.popup.Module_Object.Group;
import com.example.ryuon.popup.Module_Object.Lamp;
import com.example.ryuon.popup.Module_Object.Plug;
import com.example.ryuon.popup.Module_Object.Sensor;
import com.example.ryuon.popup.Module_Object.module;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

public class GroupEditingActivity_new extends AppCompatActivity {
    static ArrayList<String> moduleList = new ArrayList<String>(); // 내가 선택할 블루투스 모듈들
    ArrayList<module> group_module_List = new ArrayList<>();
    ArrayList<Group> receivedData;
    ListView selectedModuleList;
    ArrayAdapter adapter;
    int group_position;
    DBOpenHelper.DatabaseHelper mDbOpenHelper;
    BluetoothHelper bluetoothHelper;

    ArrayList<ArrayList<String>> tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_editing_new);
        TextView t = (TextView) findViewById(R.id.t);

        selectedModuleList = (ListView)findViewById(R.id.module_list);

        receivedData = (ArrayList<Group>) getIntent().getSerializableExtra("group_list_to_Edit");
        group_position = getIntent().getIntExtra("group_position", 0);
        group_module_List = receivedData.get(group_position).getModule_List();

        bluetoothHelper = new BluetoothHelper();
        mDbOpenHelper = new DBOpenHelper.DatabaseHelper(getApplicationContext(), "Group.db", null, 1); // CREATE TABLE IF NOT EXISTS Group_Module (grouping TEXT, module TEXT);

        mDbOpenHelper.initObj();
        tmp = mDbOpenHelper.getModuleList(); // 테이블에서 모든 그룹에 포함되어 있는 모듈들의 목록

        this.setTitle(receivedData.get(group_position).getName());

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, moduleList);
        selectedModuleList.setAdapter(adapter);

        t.setText("블루투스 등록된 모듈 목록입니다.\n※ 체크된 항목은 해당 그룹에 포함된 모듈입니다.");

        for (int i = 0; i < moduleList.size(); i++) {
            if (tmp.get(group_position).contains(moduleList.get(i))){ // moduleList는 블루투스에 등록되어 있는 애들
                selectedModuleList.setItemChecked(i, true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_group_editing, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int count = adapter.getCount();
        SparseBooleanArray checkedItems = selectedModuleList.getCheckedItemPositions();

        for (int i = 0; i < count; i++) {
            if (checkedItems.get(i)) {
                if (moduleList.get(i).contains("무드등")) {
                    Lamp newLamp = new Lamp();
                    newLamp.setName(selectedModuleList.getItemAtPosition(i).toString());
                    receivedData.get(group_position).addModule_List(newLamp);
                } else if (moduleList.get(i).contains("멀티탭")) {
                    Plug newPlug = new Plug();
                    newPlug.setName(selectedModuleList.getItemAtPosition(i).toString());
                    receivedData.get(group_position).addModule_List(newPlug);
                } else if (moduleList.get(i).contains("블라인드")) {
                    Blind newBlind = new Blind();
                    newBlind.setName(selectedModuleList.getItemAtPosition(i).toString());
                    receivedData.get(group_position).addModule_List(newBlind);
                } else if (moduleList.get(i).contains("센서모듈")) {
                    Sensor newSensor = new Sensor();
                    newSensor.setName(selectedModuleList.getItemAtPosition(i).toString());
                    receivedData.get(group_position).addModule_List(newSensor);
                }
            }
        }

        mDbOpenHelper.updateGroup(receivedData.get(group_position).getName(), group_module_List);

        // 포함모듈에 대한 정보를 그룹관리 인터페이스로 전달 (최종 목적지 : GroupControlActivity)
        Intent intent = new Intent();
        intent.putExtra("group_list_to_Main", receivedData);
        setResult(RESULT_OK, intent);
        finish();


//        // Group 클래스에 Serializeable 인터페이스 implements
//        setResult(RESULT_OK, intent);
//        finish();
//
//        int id = item.getItemId();
//        ArrayList<String> module = new ArrayList<String>();
//        for (int i = 0; i < selectedList.size(); i++) {
//            module.add(selectedListe.get(i).getModulename());
//        }
        return super.onOptionsItemSelected(item);
    }


    public static void setModuleList(ArrayList<String> modules){
        moduleList.clear();

        for (int i = 0; i < modules.size(); i++) {
            moduleList.add(modules.get(i));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("group_list_to_Main", receivedData);
        setResult(RESULT_OK, intent);
        finish();
    }

}
