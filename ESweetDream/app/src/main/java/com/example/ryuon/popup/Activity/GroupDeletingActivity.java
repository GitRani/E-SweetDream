package com.example.ryuon.popup.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ryuon.popup.DB.DBOpenHelper;
import com.example.ryuon.popup.Module_Object.Group;
import com.example.ryuon.popup.R;

import java.util.ArrayList;
import java.util.Collections;


public class GroupDeletingActivity extends AppCompatActivity{

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<Group> items;
    ArrayList<Group> receivedData = new ArrayList<>();
    ArrayList<String> group_name = new ArrayList<>();;

    DBOpenHelper.DatabaseHelper mDbOpenHelper;

    private void setReceivedData(ArrayList<Group> items){
        receivedData.addAll(items);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_deleting);
        items = (ArrayList<Group>) getIntent().getSerializableExtra("group_list_to_Delete");
        setReceivedData(items);

        mDbOpenHelper = new DBOpenHelper.DatabaseHelper(getApplicationContext(), "Group.db", null, 1); // CREATE TABLE IF NOT EXISTS Group_Module (grouping TEXT, module TEXT);

        for (int i = 0; i < items.size(); i++) {
            group_name.add(items.get(i).getName());
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, group_name);
        listView = (ListView) findViewById(R.id.group_list);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        TextView t = (TextView) findViewById(R.id.t_d);
        t.setText("삭제를 원하는 그룹을 체크한 후 삭제버튼을 눌러주세요.");

//        // delete button에 대한 이벤트 처리.
//        Button deleteButton = (Button)findViewById(R.id.delete) ;
//        deleteButton.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
//                int count = adapter.getCount() ;
//
//                for (int i = count-1; i >= 0; i--) {
//                    if (checkedItems.get(i)) {
//                        items.remove(i) ;
//                    }
//                }
//
//                // 모든 선택 상태 초기화.
//                listView.clearChoices() ;
//
//                adapter.notifyDataSetChanged();
//            }
//        }) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_group_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        final TextView textView = (TextView) findViewById(R.id.test);

        int id = item.getItemId();
        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
        int count = adapter.getCount();

//        String TorF = "";
        ArrayList<String> saveReceivedData = new ArrayList<>();

        if (id == R.id.action_delete) {
            for (int i = count - 1; i >= 0; i--) {
                if (checkedItems.get(i)) {
                    saveReceivedData.add(receivedData.get(i).getName());
                    receivedData.remove(i);
                }
            }

//            Collections.reverse(saveReceivedData);
            mDbOpenHelper.deleteGroup(saveReceivedData); // 삭제하고 남은 그룹을 인자로 보내어 그 외의 그룹은 삭제.

            // 모든 선택 상태 초기화.
            listView.clearChoices();
            adapter.notifyDataSetChanged();

            Intent delete_intent = new Intent(this, MainActivity.class);
            delete_intent.putExtra("group_list_to_Main", receivedData);
            setResult(RESULT_OK, delete_intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("group_list_to_Main", items);
        setResult(RESULT_OK, intent);
        finish();
    }
}


