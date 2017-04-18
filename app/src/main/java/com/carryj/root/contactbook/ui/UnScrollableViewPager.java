package com.carryj.root.contactbook.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by root on 17/4/13.
 */

public class UnScrollableViewPager extends ViewPager {

    private boolean noScroll = true; //true 代表不能滑动 //false 代表能滑动

    public UnScrollableViewPager(Context context) {
        super(context);

    }

    public UnScrollableViewPager(Context context, AttributeSet attrs) {

        super(context,attrs);
    }

    public void setNoScroll(boolean noScroll) {

        this.noScroll = noScroll;
    }


    //去除切换动画
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }


    //禁止滑动
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(ev);
    }


}
