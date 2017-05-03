package com.carryj.root.contactbook.activity;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.carryj.root.contactbook.R;

/**
 * 
 *@Name: SweepBackActivity
 * 
 *@Description: ���л������ع��ܵ�Activity��������Activity���̳�
 *
 *@author: ���������桢�޼�
 *
 */
public abstract class SweepBackActivity extends BaseActivity implements OnTouchListener {

	private final float X_MIN_DISTANCE = 100;
	private final float Y_LIMIT_DISTANCE = 100;
	private float xDown;
	private float yDown;
	private float xUp;
	private float yUp;

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		View mView = getWindow().getDecorView();
		mView.setClickable(true);
		mView.setOnTouchListener(this);
	}

	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
		View mView = getWindow().getDecorView();
		mView.setClickable(true);
		mView.setOnTouchListener(this);
	}

	/**
	 * ����ʶ��
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDown = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			xUp = event.getX();
			if (xUp - xDown > X_MIN_DISTANCE && Math.abs(yUp - yDown) < Y_LIMIT_DISTANCE) {
				finish();
			}
			break;
		case MotionEvent.ACTION_UP:

			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
