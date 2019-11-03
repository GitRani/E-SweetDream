package com.example.ryuon.popup.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuon.popup.DB.DBOpenHelper;
import com.example.ryuon.popup.Module_Object.Blind;
import com.example.ryuon.popup.Module_Object.Group;
import com.example.ryuon.popup.Module_Object.Lamp;
import com.example.ryuon.popup.Module_Object.Plug;
import com.example.ryuon.popup.Module_Object.Sensor;
import com.example.ryuon.popup.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> group_name = new ArrayList<>(); // 그룹 이름 리스트
    ArrayList<ArrayList<String>> module_name = new ArrayList<>(); // 모듈 이름 리스트
    ArrayList<Group> groupList = new ArrayList<>(); // 그룹 리스트

    ArrayAdapter adapter;
    ListView listView;

    DBOpenHelper.DatabaseHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbOpenHelper = new DBOpenHelper.DatabaseHelper(getApplicationContext(), "Group.db", null, 1); // CREATE TABLE IF NOT EXISTS Group_Module (grouping TEXT, module TEXT);
        mDbOpenHelper.initObj();
        group_name = mDbOpenHelper.getGroupList(); // Group을 가져온다.(타입은 스트링 타입의 어레이리스트)
        module_name = mDbOpenHelper.getModuleList();
        initGroupList(group_name, module_name);

        // CustomAdapter로 수정해야함. group은 ArrayList<String>이 아니기 때문이다. (19-07-14. 03-59)
//        adapter = new CustomAdapter_Group(this, android.R.layout.simple_list_item_single_choice, group);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, group_name);
        listView = (ListView) findViewById(R.id.group_list);
        listView.setAdapter(adapter);


        TextView t = (TextView) findViewById(R.id.t);
        t.setText("현재 사용자가 생성한 그룹의 목록입니다.\n※ 그룹 편집을 통하여 해당 그룹에 모듈을 포함시킬 수 있습니다.");

//        TextView tt = (TextView)findViewById(R.id.tt);
//        tt.setText(mDbOpenHelper.test());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (groupList.get(position).getModule_List().isEmpty()) {
                    String msg = "그룹 내에 모듈을 포함시켜주세요";
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(getApplicationContext(), msg, duration).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), GroupControlActivity.class);
                    intent.putExtra("selectedGroup", groupList.get(position));
                    startActivity(intent);
                }

            }
        });
    }

//    //그룹을 일반화시키는 과정
//    ArrayList<module>  group = new ArrayList<module>();
//    module이라는 부모 클래스를 만들어 (무드등, 블라인드, 멀티탭, 센서모듈 클래스는 module 클래스를 상속)
//    모듈 클래스 안에 getter, setter 만들어 (왜냐하면 색상이나, 이름도 설정해야하니까)
//    나중에 필요한 정보는 group 어레이 리스트의 각 요소로부터 getter써가지고 가져와야지

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_group, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items);
//        listView = (ListView) findViewById(R.id.group_list);
//        listView.setAdapter(adapter);
        switch (id) {
            case R.id.action_create :
                int counter = adapter.getCount();
                Group newGroup = new Group();
                newGroup.setName("Group " + Integer.toString(counter + 1));

                groupList.add(newGroup); // 그룹리스트에 새로운 그룹 추가.
                group_name.add(newGroup.getName());

                adapter.notifyDataSetChanged(); // listView 갱신
                // -- ADD CODE -- DB에 INSERT
                mDbOpenHelper.insertGroup(newGroup.getName(),"0");
                return true;
            case  R.id.action_delete:
                Intent delete_intent = new Intent(this, GroupDeletingActivity.class);
                delete_intent.putExtra("group_list_to_Delete", groupList); // GroupDeletingActivity도 수정해야함. (getName() 사용해서 19-07-14)
                startActivityForResult(delete_intent,0);
                return true;
            case R.id.action_edit :
                Intent edit_intent = new Intent(this, GroupEditingListActivity.class);
                edit_intent.putExtra("group_list_to_Edit", groupList); // GroupEditingListActivity에 그룹객체를 담은 그룹리스트 전달, GroupEditingActivity도 수정해야함. (19-07-14)
                startActivityForResult(edit_intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        groupList =  (ArrayList<Group>)data.getSerializableExtra("group_list_to_Main");

        group_name.removeAll(group_name);
        TextView t = (TextView) findViewById(R.id.t);
        switch (requestCode) {
            case 0 :
                for (int i = 0; i < groupList.size(); i ++) {
                    group_name.add(groupList.get(i).getName());
                }
                listView = (ListView) findViewById(R.id.group_list);
                // ****더이상 String이 아닌 Group타입의 데이터 중 Name을 보여줄 것이기 때문에 CustomAdapter를 생성해야함. (19-07-14.03-13)
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, group_name);
                listView.setAdapter(adapter);
                t.setText("현재 사용자가 생성한 그룹의 목록입니다.\n※ 그룹 편집을 통하여 해당 그룹에 모듈을 포함시킬 수 있습니다.");
                TextView tt = (TextView)findViewById(R.id.ttt);
                tt.setText(mDbOpenHelper.test());
                adapter.notifyDataSetChanged();
                break;

            case 1 :
                for (int i = 0; i < groupList.size(); i ++) {
                    group_name.add(groupList.get(i).getName());
                }
                listView = (ListView) findViewById(R.id.group_list);
                // ****더이상 String이 아닌 Group타입의 데이터 중 Name을 보여줄 것이기 때문에 CustomAdapter를 생성해야함. (19-07-14.03-13)
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, group_name);
                listView.setAdapter(adapter);
                t.setText("현재 사용자가 생성한 그룹의 목록입니다.\n※ 그룹 편집을 통하여 해당 그룹에 모듈을 포함시킬 수 있습니다.");
                adapter.notifyDataSetChanged();
                break;
        }
    }

    // DB로부터 그룹 객체에 대한 정보(모듈에 대한 정보 포함)를 가져와서 생성하는 메소드
    public void initGroupList(ArrayList<String> group_name, ArrayList<ArrayList<String>> module_name) {
        for (int i = 0; i < group_name.size(); i++ ){
            Group group = new Group();
            group.setName(group_name.get(i));

            if (module_name.size() != 0) {

                for (int j = 1; j < module_name.get(i).size(); j++) {
                    String module = module_name.get(i).get(j);
                    if (module.contains("무드등")) {
                        Lamp newLamp = new Lamp();
                        newLamp.setName(module);
                        group.addModule_List(newLamp);
                    } else if (module.contains("멀티탭")) {
                        Plug newPlug = new Plug();
                        newPlug.setName(module);
                        group.addModule_List(newPlug);
                    } else if (module.contains("블라인드")) {
                        Blind newBlind = new Blind();
                        newBlind.setName(module);
                        group.addModule_List(newBlind);
                    } else if (module.contains("센서모듈")) {
                        Sensor newSensor = new Sensor();
                        newSensor.setName(module);
                        group.addModule_List(newSensor);
                    }
                }
            }

            groupList.add(group);
        }
    }
}



















//    String fileName = "test.txt";
//                File file2 = new File(getFilesDir(), fileName);
//                file2.setWritable(true, false);
//
//                try {
//                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file2));
//                    out.writeObject(newGroup);
//                    out.close();
//                } catch (FileNotFoundException fe) {
//                    fe.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file2));
//                    Group n = (Group)in.readObject();
//                    String msg = n.getName();
//                    int duration = Toast.LENGTH_LONG;
//                    Toast.makeText(getApplicationContext(), msg, duration).show();
//                } catch (FileNotFoundException fe) {
//                    fe.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }