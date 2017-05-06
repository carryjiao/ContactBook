package com.carryj.root.contactbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.data.ImData;

import java.util.ArrayList;


/**
 * Created by root on 17/5/4.
 */

public class ContactPersonalShowImAdapter extends RecyclerView.Adapter<ContactPersonalShowImAdapter.ImViewHolder> {


    private Context context;
    private ArrayList<ImData> imData;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;



    public ContactPersonalShowImAdapter(Context context, ArrayList<ImData> imData) {
        this.context = context;
        this.imData = imData;
    }

    //  点击事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onLongClick(View v, int position);
    }


    @Override
    public ImViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImViewHolder holder = new ImViewHolder(LayoutInflater.from(
                context).inflate(R.layout.im_show_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ImViewHolder holder, int position) {
        holder.type.setText(imData.get(position).getImType());
        holder.im.setText(imData.get(position).getIm());
    }

    @Override
    public int getItemCount() {
        return imData.size();
    }


    class ImViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView im;

        public ImViewHolder(final View view) {
            super(view);
            type = (TextView) view.findViewById(R.id.tv_contact_personal_show_im_type);
            im = (TextView) view.findViewById(R.id.tv_contact_personal_show_im);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onClick(getLayoutPosition());
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemLongClickListener!=null) {
                        onItemLongClickListener.onLongClick(v, getLayoutPosition());
                    }
                    return true;
                }
            });

        }

    }
}
