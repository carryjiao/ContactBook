package com.carryj.root.contactbook;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;

public abstract  class BaseFragmentActivity extends FragmentActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initData();
        initView();
        initEvents();
    }

    protected abstract void initData();
    protected abstract void initView();
    protected abstract void initEvents();
}
