package com.carryj.root.contactbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.data.EmailData;
import com.carryj.root.contactbook.data.PhoneNumberData;

import java.util.ArrayList;


/**
 * Created by root on 17/5/4.
 */

public class ContactPersonalShowEmailAdapter extends RecyclerView.Adapter<ContactPersonalShowEmailAdapter.EmailViewHolder> {


    private Context context;
    private ArrayList<EmailData> emailData;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;



    public ContactPersonalShowEmailAdapter(Context context, ArrayList<EmailData> emailData) {
        this.context = context;
        this.emailData = emailData;
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
    public EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EmailViewHolder holder = new EmailViewHolder(LayoutInflater.from(
                context).inflate(R.layout.email_show_list_item, parent,
                false));
        return holder;
    }



    @Override
    public void onBindViewHolder(EmailViewHolder holder, int position) {
        holder.type.setText(emailData.get(position).getEmailType());
        holder.email.setText(emailData.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return emailData.size();
    }


    class EmailViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView email;

        public EmailViewHolder(final View view) {
            super(view);
            type = (TextView) view.findViewById(R.id.tv_contact_personal_show_email_type);
            email = (TextView) view.findViewById(R.id.tv_contact_personal_show_email);

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
