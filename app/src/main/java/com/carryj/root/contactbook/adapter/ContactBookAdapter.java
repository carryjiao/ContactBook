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

import java.util.ArrayList;

/**
 * Created by root on 17/4/20.
 */

public class ContactBookAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> mContactsName;
    private ArrayList<Bitmap> mContactsPhonto;
    private ArrayList<Long> mContactsID;

    public ContactBookAdapter(Context mContext, ArrayList<String> mContactsName, ArrayList<Bitmap>
            mContactsPhonto, ArrayList<Long> mContactsID) {
        this.mContext =mContext;
        this.mContactsName = mContactsName;
        this.mContactsPhonto = mContactsPhonto;
        this.mContactsID = mContactsID;

    }

    @Override
    public int getCount() {
        return mContactsName.size();
    }

    @Override
    public String getItem(int position) {
        return mContactsName.get(position);
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
            viewHolder.im_contact_book_list_item_icon =
                    (ImageView) convertView.findViewById(R.id.im_contact_book_list_item_icon);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String str = getItem(position);
        if (str != null) {
            viewHolder.tv_name.setText(mContactsName.get(position));
            viewHolder.im_contact_book_list_item_icon.setImageBitmap(mContactsPhonto.get(position));
        }

        return convertView;
    }

    private static class ViewHolder {

        TextView tv_name;
        ImageView im_contact_book_list_item_icon;

        public ViewHolder() {

        }

    }
}
