package com.example.ryuon.popup.CustomViewAdapter.GroupEdit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ryuon.popup.Module_Object.module;
import com.example.ryuon.popup.R;

import java.util.ArrayList;


public class CustomAdapter_Selected extends BaseAdapter implements View.OnClickListener {
    private ArrayList<module> modules;
    private ListBtnClickListener listBtnClickListener;

    public interface ListBtnClickListener {
        void onListBtnClick(int position);
    }

    public CustomAdapter_Selected(ArrayList<module> list, ListBtnClickListener clickListener) {
        this.modules = list;
        this.listBtnClickListener = clickListener;
    }

    @Override
    public int getCount() {
        return modules.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_select_module_item, parent, false);
        }

        final TextView modulenameText = (TextView) convertView.findViewById(R.id.modulename);

        final module listViewItem = modules.get(position);

        modulenameText.setText(listViewItem.getName());

        Button xbutton = (Button) convertView.findViewById(R.id.XButton);
        xbutton.setTag(pos);
        xbutton.setOnClickListener(this);

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position) {
        return modules.get(position);
    }

    public void onClick(View v) {
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int)v.getTag());
        }
    }

}
