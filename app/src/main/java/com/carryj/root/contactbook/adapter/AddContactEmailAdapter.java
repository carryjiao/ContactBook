package com.carryj.root.contactbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.data.EmailData;

import java.util.ArrayList;


/**
 * Created by root on 17/5/4.
 */

public class AddContactEmailAdapter extends RecyclerView.Adapter<AddContactEmailAdapter.NumberViewHolder> {


    private Context context;
    private ArrayList<EmailData> emailData;
    private OnItemListener listener;
    private OnItemSpinnerListener spinnerListener;
    private TextChangeListener textChangeListener;
    private boolean updataFlag = false;


    public AddContactEmailAdapter(Context context, ArrayList<EmailData> emailData, boolean updataFlag) {
        this.context = context;
        this.emailData = emailData;
        this.updataFlag = updataFlag;
    }

    //  点击事件
    public void setOnItemListener(OnItemListener onItemListener) {
        this.listener = onItemListener;
    }

    public void setOnItemSpinnerListener(OnItemSpinnerListener onItemSpinnerListener) {
        this.spinnerListener = onItemSpinnerListener;
    }

    public void myAddTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }


    public interface OnItemListener {
        void onClick(int position);
    }

    public interface OnItemSpinnerListener {
        void onItemSelected(AdapterView<?> parent, View view, int position, long id, int listposition);
        void onNothingSelected(AdapterView<?> parent, int listposition);
    }

    public interface TextChangeListener {
        public void beforeTextChanged(CharSequence s, int start, int count, int after, int listposition);
        public void onTextChanged(CharSequence s, int start, int before, int count, int listposition);
        public void afterTextChanged(Editable s, int listposition);
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NumberViewHolder holder = new NumberViewHolder(LayoutInflater.from(
                context).inflate(R.layout.add_contact_email_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.email.setText(emailData.get(position).getEmail());
        if(updataFlag) {
            int spinnerIndex  = 0;
            if(emailData.get(position).getEmailType()!=null) {
                switch (emailData.get(position).getEmailType()) {
                    case "个人":
                        spinnerIndex = 0;
                        break;
                    case "工作":
                        spinnerIndex = 1;
                        break;
                    case "其他":
                        spinnerIndex = 2;
                        break;
                    case "手机":
                        spinnerIndex = 3;
                        break;
                    default:
                        spinnerIndex = 0;
                        break;
                }
            }else {
                spinnerIndex = 0;
            }
            holder.spinner.setSelection(spinnerIndex);
        }
    }

    @Override
    public int getItemCount() {
        return emailData.size();
    }

    public void addEmailData(int position) {
        emailData.add(position,new EmailData());
        notifyItemInserted(position);
    }

    public void  deleteEmailData(int position) {
        emailData.remove(position);
        notifyItemRemoved(position);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        Spinner spinner;
        TextView email;

        public NumberViewHolder(final View view) {
            super(view);
            delete = (ImageView) view.findViewById(R.id.im_add_contact_email_item_delete);
            spinner = (Spinner) view.findViewById(R.id.sp_add_contact_email_item_type);
            email = (TextView) view.findViewById(R.id.et_add_contact_email_item_number);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClick(getLayoutPosition());
                    }
                }
            });


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(spinnerListener!=null) {
                        spinnerListener.onItemSelected(parent, view, position, id, getLayoutPosition());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    if(spinnerListener!=null) {
                        spinnerListener.onNothingSelected(parent, getLayoutPosition());
                    }
                }
            });

            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(textChangeListener!=null){
                        textChangeListener.beforeTextChanged(s, start, count, after, getLayoutPosition());
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(textChangeListener!=null){
                        textChangeListener.onTextChanged(s, start, before, count, getLayoutPosition());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(textChangeListener!=null){
                        textChangeListener.afterTextChanged(s, getLayoutPosition());
                    }
                }
            });
        }

    }
}
