package com.example.ryuon.popup.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import com.example.ryuon.popup.Module_Object.module;

import java.util.ArrayList;




public class DBOpenHelper {

    private static final String DB_NAME = "Group.db"; // DB명
    private static final int DB_VER = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;
    private static String tbName = DataBase.CreateDB._TABLENAME;



    public static class DatabaseHelper extends SQLiteOpenHelper {
        SQLiteDatabase db_write = getWritableDatabase();
        SQLiteDatabase db_read  = getReadableDatabase();
        String groupName = DataBase.CreateDB.NAME;

        public DatabaseHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            db_write.execSQL(DataBase.CreateDB._CREATE);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBase.CreateDB._CREATE);
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+tbName);
            onCreate(db);
        }

        // 테이블에 값 삽입(그룹,모듈)
        public void insertGroup(String group, String module) {
            String sqlInsert = "INSERT INTO GroupList(GroupName, Module) VALUES(" + "'" + group + "'" + ", " + "'" + module + "'" +");" ;
            db_write.execSQL(sqlInsert);
        }
        // 테이블에서 그룹 가져오기

//        public ArrayList<String> selectGroup() {
//            result = new ArrayList<>();
//            int cursorIndex_group = 0;
//
//            String sqlSelect = "SELECT DISTINCT GroupName FROM GroupList" ;
//            Cursor cursor = db_read.rawQuery(sqlSelect, null);
//
//            // 커서가 찾을 값이 있으면. 만족안하면 수행안함.
//            if(cursor != null && cursor.getCount() > 0) {
//                // 커서가 다음으로 움직임 (while루프를 탈출하는 조건은 다음으로 탐색할 것이 없으면 탈출한다. 왜냐하면 이때 반환값이 -1이기 때문이다!)
//                while (cursor.moveToNext()) {
//                    // cursorIndex = 0. Attribute가 0인 즉, 그룹을 getString으로 받아서 어레이리스트에 add한다.
//                    result.add(cursor.getString(cursorIndex_group));
//                }
//            }
//            return result;
//        }

        // 모듈의 목록 가져오기
        ArrayList<String> groupList;
        ArrayList<ArrayList<String>> moduleList;
        ArrayList<String> moduleInGroup;
        public void initObj() {
            // 테이블 내의 모든 그룹에 대한 정보 추출 후, groupList에 추가.
            groupList = new ArrayList<>();
            int cursorIndex_group = 0;

            String sqlSelect = "SELECT DISTINCT GroupName FROM GroupList ORDER BY GroupName" ;
            Cursor cursor = db_read.rawQuery(sqlSelect, null);

            if(cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    groupList.add(cursor.getString(cursorIndex_group));
                }
            }

            // 각 그룹별 모듈 추출 후, 그룹별로 모듈 추가
            moduleList = new ArrayList<>();
            int cursorIndex_module = 0;
            for (int i = 0; i < groupList.size(); i++) {
                moduleInGroup = new ArrayList<>();
                String index = Integer.toString(i+1);
                String group = "Group "+index;
                String sqlSelect_module = "SELECT Module FROM GroupList WHERE GroupName = ? ORDER BY GroupName";
                Cursor cursor_module = db_read.rawQuery(sqlSelect_module, new String[]{group});
                if(cursor_module != null && cursor_module.getCount() > 0) {
                    moduleInGroup.add(group); // moduleInGroup의 0번째 인덱스에는 해당하는 그룹명이 들어간다.
                    while (cursor_module.moveToNext()) {
                        moduleInGroup.add(cursor_module.getString(cursorIndex_module)); // 그 이후 1번째 인덱스부터는 해당하는 그룹의 모듈이름이 들어간다.
                    }
                    moduleList.add(moduleInGroup); // moduleList에는 moduleInGroup 리스트가 추가된다.
                }
            }
        }

        public ArrayList<String> getGroupList() {
            return groupList;
        }

        public ArrayList<ArrayList<String>> getModuleList() {
            return moduleList;
        }

        // 어레이리스트로 받아온 그룹을 제거하는 메소드
        public void deleteGroup(ArrayList<String> groups) {
            for (int i = 0; i < groups.size(); i++) {
                String sqlDelete = "DELETE FROM GroupList WHERE GroupName = " + "'" + groups.get(i) + "';";
                db_write.execSQL(sqlDelete);
            }
        }

        public void updateGroup(String groupName, ArrayList<module> moduleName) {
            String sqlRemoveNull =  "DELETE FROM GroupList WHERE GroupName = " + "'" + groupName +  "'" + "AND Module = 0";
            db_write.execSQL(sqlRemoveNull);

            String finding_module = "";
            for (int i = 0; i < moduleName.size(); i++) {
                String sqlfindRow = "SELECT Module FROM GroupList WHERE Module = ?";
                Cursor cursor_finding = db_read.rawQuery(sqlfindRow, new String[]{moduleName.get(i).getName()});
                if(cursor_finding != null && cursor_finding.getCount() > 0) {
                    while(cursor_finding.moveToNext()){
                        finding_module = cursor_finding.getString(0);
                    }
                }

                String sqlInsertRow =  "INSERT INTO GroupList(GroupName, Module) VALUES(" + "'" + groupName + "', '" + moduleName.get(i).getName() + "')";
                if (finding_module.length() == 0) {
                    db_write.execSQL(sqlInsertRow);
                }
            }
        }

        public String test() {
            String testString = "";
            String sqlTest = "SELECT * FROM GroupList";


            Cursor cursor_test = db_read.rawQuery(sqlTest, null);
            // 커서가 찾을 값이 있으면. 만족안하면 수행안함.
            if(cursor_test != null && cursor_test.getCount() > 0) {
                while (cursor_test.moveToNext()) {
                    testString += cursor_test.getString(0) + " ";
                    testString += cursor_test.getString(1) + " ";
                    testString += cursor_test.getString(2) + " ";
                }
            }
            return testString;
        }

        // 테이블 삭제하기 (테이블이 뭔가 망했을 때 이용하시길... --> 사용법 : MainActivity의 onCreate부 테이블 생성 전에 넣으면 됨)
        public void dropTable() {
            db_write.execSQL("DROP TABLE '"+tbName+"'");
        }
    }

    // 생성자
    public DBOpenHelper(Context context) {
        this.mCtx = context;
    }

    public DBOpenHelper open() {
        mDBHelper = new DatabaseHelper(mCtx, DB_NAME, null, DB_VER);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }
}