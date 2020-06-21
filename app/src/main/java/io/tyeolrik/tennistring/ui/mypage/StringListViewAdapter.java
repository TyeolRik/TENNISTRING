package io.tyeolrik.tennistring.ui.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.tyeolrik.tennistring.R;

public class StringListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<StringRecordItem> listViewItemList = new ArrayList<StringRecordItem>() ;

    public StringListViewAdapter() {

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.string_record_item_layout, parent, false);
        }

        TextView stringRecordDate   = view.findViewById(R.id.stringRecordDate);
        TextView stringBrand        = view.findViewById(R.id.stringBrand);
        TextView stringName         = view.findViewById(R.id.stringName);
        TextView stringTension      = view.findViewById(R.id.stringTension);

        StringRecordItem stringRecordItem = listViewItemList.get(position);

        stringRecordDate.setText(stringRecordItem.getStringRecordDate());
        stringBrand.setText(stringRecordItem.getStringBrand());
        stringName.setText(stringRecordItem.getStringName());
        stringTension.setText(stringRecordItem.getStringTension());

        return view;
    }

    public void addItem(String date, String brand, String name, String tension) {
        StringRecordItem item = new StringRecordItem(date, brand, name, tension);
        listViewItemList.add(item);
    }

    public void swapItem(String date, String brand, String name, String tension) {
        StringRecordItem item = new StringRecordItem(date, brand, name, tension);
        listViewItemList.set(listViewItemList.size() - 1, item);
    }

    public ArrayList<StringRecordItem> getStringRecordList() {
        return listViewItemList;
    }
}
