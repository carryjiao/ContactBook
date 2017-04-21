package com.carryj.root.contactbook.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carryj.root.contactbook.R;


/**
 * Created by root on 17/4/10.
 */

public class DialFragement extends Fragment implements OnClickListener {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private TextView tv_dial_add;
    private TextView tv_show_dial_number;
    private ImageView iv_dial_delete;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn_star;
    private Button btn0;
    private Button btn_jin;
    private Button btn_call;


    public DialFragement() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dial,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_dial_add = (TextView) view.findViewById(R.id.tv_dial_add);
        iv_dial_delete = (ImageView) view.findViewById(R.id.iv_dial_delete);
        tv_show_dial_number = (TextView) view.findViewById(R.id.tv_show_dial_number);

        btn1 = (Button) view.findViewById(R.id.btn_dial_number_1);
        btn2 = (Button) view.findViewById(R.id.btn_dial_number_2);
        btn3 = (Button) view.findViewById(R.id.btn_dial_number_3);
        btn4 = (Button) view.findViewById(R.id.btn_dial_number_4);
        btn5 = (Button) view.findViewById(R.id.btn_dial_number_5);
        btn6 = (Button) view.findViewById(R.id.btn_dial_number_6);
        btn7 = (Button) view.findViewById(R.id.btn_dial_number_7);
        btn8 = (Button) view.findViewById(R.id.btn_dial_number_8);
        btn9 = (Button) view.findViewById(R.id.btn_dial_number_9);
        btn_star = (Button) view.findViewById(R.id.btn_dial_number_xin);
        btn0 = (Button) view.findViewById(R.id.btn_dial_number_0);
        btn_jin = (Button) view.findViewById(R.id.btn_dial_number_jin);
        btn_call = (Button) view.findViewById(R.id.btn_dial_number_call);


        tv_dial_add.setOnClickListener(this);
        iv_dial_delete.setOnClickListener(this);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn_star.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn_jin.setOnClickListener(this);
        btn_call.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //删除拨号最后一个数字
            case R.id.iv_dial_delete:
                String str = tv_show_dial_number.getText().toString();
                tv_show_dial_number.setText(str.substring(0,str.length()-1));
                break;
            case R.id.btn_dial_number_1:
                tv_show_dial_number.append("1");
                break;
            case R.id.btn_dial_number_2:
                tv_show_dial_number.append("2");
                break;
            case R.id.btn_dial_number_3:
                tv_show_dial_number.append("3");
                break;
            case R.id.btn_dial_number_4:
                tv_show_dial_number.append("4");
                break;
            case R.id.btn_dial_number_5:
                tv_show_dial_number.append("5");
                break;
            case R.id.btn_dial_number_6:
                tv_show_dial_number.append("6");
                break;
            case R.id.btn_dial_number_7:
                tv_show_dial_number.append("7");
                break;
            case R.id.btn_dial_number_8:
                tv_show_dial_number.append("8");
                break;
            case R.id.btn_dial_number_9:
                tv_show_dial_number.append("9");
                break;
            case R.id.btn_dial_number_xin:
                tv_show_dial_number.append("*");
                break;
            case R.id.btn_dial_number_0:
                tv_show_dial_number.append("0");
                break;
            case R.id.btn_dial_number_jin:
                tv_show_dial_number.append("#");
                break;
            case R.id.btn_dial_number_call:
                //调用系统方法拨打电话
                testCall(v);
                break;
            default:
                break;
        }

    }

    public void testCall(View view)
    {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else
        {
            callPhone();
        }
    }

    public void callPhone()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + tv_show_dial_number.getText().toString());
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhone();
            } else
            {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
