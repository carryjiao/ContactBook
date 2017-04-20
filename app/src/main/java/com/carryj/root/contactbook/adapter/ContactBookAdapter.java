package com.carryj.root.contactbook.adapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carryj.root.contactbook.R;

import java.util.ArrayList;

/**
 * Created by root on 17/4/20.
 */

public class ContactBookAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> nameListData;

    public ContactBookAdapter(Context mContext, ArrayList<String> nameListData) {
        this.mContext =mContext;
        this.nameListData = nameListData;

    }

    @Override
    public int getCount() {
        return nameListData.size();
    }

    @Override
    public String getItem(int position) {
        return nameListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            View.inflate(mContext, R.layout.contact_book_list_item, null);
            new ViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tv_name.setText(nameListData.get(position));

        return convertView;
    }

    class ViewHolder {

        TextView tv_name;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_user_name);
            view.setTag(this);
        }
    }
}
