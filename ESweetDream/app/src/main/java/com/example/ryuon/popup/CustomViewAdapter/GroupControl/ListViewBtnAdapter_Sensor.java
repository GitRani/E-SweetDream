package com.example.ryuon.popup.CustomViewAdapter.GroupControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ryuon.popup.Module_Object.Sensor;
import com.example.ryuon.popup.R;

import java.util.ArrayList;

public class ListViewBtnAdapter_Sensor extends ArrayAdapter {

    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;

    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
    public ListViewBtnAdapter_Sensor(Context context, int resource, ArrayList<Sensor> list) {
        super(context, resource, list) ;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource ;

    }

    // 새롭게 만든 Layout을 위한 View를 생성하는 코드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        final TextView textTextView = (TextView) convertView.findViewById(R.id.textView2);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final Sensor listViewItem = (Sensor) getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        textTextView.setText(listViewItem.getName());
        return convertView;

    }

}