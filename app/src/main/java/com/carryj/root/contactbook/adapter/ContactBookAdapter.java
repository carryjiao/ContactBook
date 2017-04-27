package com.carryj.root.contactbook.adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.data.ContactListViewItemData;

import java.util.ArrayList;

/**
 * Created by root on 17/4/20.
 */

public class ContactBookAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<ContactListViewItemData> mData;

    public ContactBookAdapter(Context mContext, ArrayList<ContactListViewItemData> mData) {
        this.mContext =mContext;
        this.mData = mData;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ContactListViewItemData getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.contact_book_list_item, null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.context_book_display_name);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ContactListViewItemData data = getItem(position);
        if (data != null) {
            viewHolder.tv_name.setText(data.getName());

        }

        return convertView;
    }

    private static class ViewHolder {

        TextView tv_name;

        public ViewHolder() {

        }

    }
}
