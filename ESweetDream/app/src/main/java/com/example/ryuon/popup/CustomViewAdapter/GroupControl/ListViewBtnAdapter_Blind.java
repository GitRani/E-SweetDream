package com.example.ryuon.popup.CustomViewAdapter.GroupControl;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.TextView;

        import com.example.ryuon.popup.Module_Object.Blind;
        import com.example.ryuon.popup.R;

        import java.util.ArrayList;

public class ListViewBtnAdapter_Blind extends ArrayAdapter implements View.OnClickListener  {

    private Boolean activate = false;

    public void setActivate(boolean TorF){
        activate = TorF;
    }

    public boolean getActivate(){
        return activate;
    }

    // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.
    public interface ListBtnClickListener4 {
        void onListBtnClick(int position) ;
    }

    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener4 listBtnClickListener4 ;


    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
    public ListViewBtnAdapter_Blind(Context context, int resource, ArrayList<Blind> list, ListBtnClickListener4 clickListener) {
        super(context, resource, list) ;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource ;

        this.listBtnClickListener4 = clickListener ;

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
        final Blind listViewItem = (Blind) getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        textTextView.setText(listViewItem.getName());

        Button button1 = (Button) convertView.findViewById(R.id.button2);
        if (getActivate()) {
            button1.setEnabled(false);
        } else {
            button1.setEnabled(true);
        }

        button1.setTag(position);
        button1.setOnClickListener(this);


        return convertView;

    }

    // button이 눌려졌을 때 실행되는 onClick함수.
    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        if (this.listBtnClickListener4 != null) {
            this.listBtnClickListener4.onListBtnClick(2) ;
        }
    }

}

