package io.tyeolrik.tennistring.ui.stringzone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import io.tyeolrik.tennistring.R;
import io.tyeolrik.tennistring.ui.mypage.StringRecordItem;

public class StringListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<HashMap<String, Object>> listViewItemList = new ArrayList<HashMap<String, Object>>() ;

    static class ViewHolder
    {
        TextView need_recommend_string_brand, need_recommend_string_name;
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

        ViewHolder holder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recommend_string_layout, parent, false);
            holder = new ViewHolder();

            holder.need_recommend_string_brand = view.findViewById(R.id.need_recommend_string_brand);
            holder.need_recommend_string_name  = view.findViewById(R.id.need_recommend_string_name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (position == 0) {
            holder.need_recommend_string_brand.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.need_recommend_string_name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        HashMap<String, Object> item = listViewItemList.get(position);
        holder.need_recommend_string_brand.setText(item.get("Brand").toString());
        holder.need_recommend_string_name.setText(item.get("Name").toString());

        return view;
    }

    public void addHashMapItem(HashMap<String, Object> item) {
        listViewItemList.add(item);
    }

    public void addTitle() {
        listViewItemList = new ArrayList<HashMap<String, Object>>() ;
        HashMap<String, Object> title = new HashMap<String, Object>();
        title.put("Brand", "브랜드");
        title.put("Name", "이름");
        addHashMapItem(title);
    }
}
