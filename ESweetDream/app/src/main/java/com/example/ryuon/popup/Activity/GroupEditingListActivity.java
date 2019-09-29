package com.example.ryuon.popup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ryuon.popup.Module_Object.Group;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

// MainActivity로부터 넘어온 ArrayList<String> 타입의 그룹 목록을 ArrayList<Group>으로 대치시켜야함. (19-07-14) 손 하나도 안댐
public class GroupEditingListActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<Group> items;
    final ArrayList<Group> receievedData = new ArrayList<>();
    final ArrayList<String> group_name = new ArrayList<>();

    private void setReceievedData(ArrayList<Group> items) {
        receievedData.addAll(items);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_editing_list);
        items = (ArrayList<Group>) getIntent().getSerializableExtra("group_list_to_Edit");

        for (int i = 0; i < items.size(); i++) {
            group_name.add(items.get(i).getName());
        }

        setReceievedData(items);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, group_name); // CustomAdapter - MainActivity에서 만든 커스텀 어댑터 사용. (07-21, 05:40)
        listView = (ListView) findViewById(R.id.group_list);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final ListView listView = (ListView) findViewById(R.id.group_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), GroupEditingActivity_new.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                intent.putExtra("group_list_to_Edit", receievedData);
                intent.putExtra("group_position", position);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("group_list_to_Main", items);
        setResult(RESULT_OK, intent);
        finish();
    }
}
