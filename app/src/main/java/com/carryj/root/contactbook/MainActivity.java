package com.carryj.root.contactbook;


import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.carryj.root.contactbook.activity.BackupActivity;
import com.carryj.root.contactbook.activity.LoginRegisterActivity;
import com.carryj.root.contactbook.activity.RegisterActivity;
import com.carryj.root.contactbook.adapter.MainFragmentPagerAdapter;
import com.carryj.root.contactbook.event.DialEvent;
import com.carryj.root.contactbook.fragments.CollectFragement;
import com.carryj.root.contactbook.fragments.ContactBookFragement;
import com.carryj.root.contactbook.fragments.DialFragement;
import com.carryj.root.contactbook.fragments.RecordFragement;
import com.carryj.root.contactbook.net.LoginRegisterManager;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ViewPager main_viewpager;
    private MainFragmentPagerAdapter main_pagerAdapter;
    private CollectFragement collectFragment;
    private RecordFragement recordFragment;
    private ContactBookFragement contactBookFragement;
    private DialFragement dialFragement;
    private LinearLayout ll_collect;
    private LinearLayout ll_record;
    private LinearLayout ll_contact;
    private LinearLayout ll_dial;
    private List<Fragment> fragments;
    private NavigationView navigationView;
    private DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCurrentPage(2);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();
                switch (id) {
                    case R.id.header_bar:
                        break;
                    case R.id.login:
                        ContactBookApplication applicationLogin = (ContactBookApplication) getApplication();
                        boolean isLogin_Login = applicationLogin.isLogin();
                        if(!isLogin_Login) {
                            Intent loginRegisterIntent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                            startActivity(loginRegisterIntent);
                        }else {
                            Toast.makeText(MainActivity.this, "您已经登录了",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.change_password:
                        ContactBookApplication applicationCPW = (ContactBookApplication) getApplication();
                        String telnum = applicationCPW.getTelnum();
                        Boolean isLoginCPW = applicationCPW.isLogin();
                        if(isLoginCPW) {
                            Intent cIntent = new Intent(MainActivity.this, RegisterActivity.class);
                            cIntent.putExtra(LoginRegisterActivity.TELNUM_EXTRA, telnum);
                            cIntent.putExtra(RegisterActivity.REQUEST_MODE, RegisterActivity.REQUEST_SET_PSW);
                            startActivity(cIntent);
                        }else {
                            Toast.makeText(MainActivity.this, "您还未登录呢",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.data_synchronization:
                        ContactBookApplication applicationSynchronization = (ContactBookApplication) getApplication();
                        boolean isLogin_Synchronization = applicationSynchronization.isLogin();
                        if(isLogin_Synchronization) {
                            Intent backupIntent = new Intent(MainActivity.this, BackupActivity.class);
                            startActivity(backupIntent);
                        }else {
                            Toast.makeText(MainActivity.this, "您还未登录呢",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.about:
                        break;
                    case R.id.logout:
                        ContactBookApplication applicationLogout = (ContactBookApplication) getApplication();
                        telnum = applicationLogout.getTelnum();
                        String psw = applicationLogout.getPsw();
                        boolean isLogin_out = applicationLogout.isLogin();
                        if (isLogin_out) {
                            LoginRegisterManager.getInstance().logout(telnum, psw);
                            applicationLogout.setLogin(false);
                            Toast.makeText(MainActivity.this, "退出登录成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "您还未登录呢",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.exit:
                        finish();
                        break;
                    default:
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }


    @Override
    protected void initData() {



        fragments = new ArrayList<Fragment>();
        if(collectFragment == null) {
            collectFragment = new CollectFragement();
            fragments.add(collectFragment);
        }
        if(recordFragment == null) {
            recordFragment = new RecordFragement();
            fragments.add(recordFragment);
        }
        if(contactBookFragement == null) {
            contactBookFragement = new ContactBookFragement();
            fragments.add(contactBookFragement);
        }
        if(dialFragement == null) {
            dialFragement = new DialFragement();
            fragments.add(dialFragement);
        }

    }

    @Override
    protected void initView() {

        main_viewpager = (ViewPager) findViewById(R.id.main_viewpager);
        ll_collect = (LinearLayout)findViewById(R.id.ll_collect);
        ll_record = (LinearLayout)findViewById(R.id.ll_record);
        ll_contact = (LinearLayout)findViewById(R.id.ll_contact);
        ll_dial = (LinearLayout)findViewById(R.id.ll_dial);

        main_pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        main_viewpager.setOffscreenPageLimit(4);
        main_viewpager.setAdapter(main_pagerAdapter);


    }


    @Override
    protected void initEvents() {

        ll_collect.setOnClickListener(this);
        ll_record.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
        ll_dial.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_collect:
                setCurrentPage(0);
                break;
            case R.id.ll_record:
                //通知刷新数据
                if(recordFragment.dialFlag){
                    EventBus.getDefault().post(new DialEvent(true,false));
                    recordFragment.dialFlag = false;
                }
                setCurrentPage(1);
                break;
            case R.id.ll_contact:
                setCurrentPage(2);
                break;
            case R.id.ll_dial:
                setCurrentPage(3);
                break;
            default:
                break;
        }

    }

    private void setSelectBar(int position) {
        ll_collect.setSelected(false);
        ll_record.setSelected(false);
        ll_contact.setSelected(false);
        ll_dial.setSelected(false);
        switch (position) {
            case 0:
                ll_collect.setSelected(true);
                break;
            case 1:
                ll_record.setSelected(true);
                break;
            case 2:
                ll_contact.setSelected(true);
                break;
            case 3:
                ll_dial.setSelected(true);
                break;
            default:
                break;

        }
    }

    public void setCurrentPage(int position) {

        main_viewpager.setCurrentItem(position);
        setSelectBar(position);

    }

   @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/



}
