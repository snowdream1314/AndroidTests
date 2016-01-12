package com.example.viewpagertest;

import java.util.List;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter {
	
	//�����б�
	private List<View> views;
	private Activity activity;
	private static final String SHAREDPREFERENCE_NAME = "first_pref";
	public ViewPagerAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
	}
	
	//����arg1λ�õĽ���
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}
	
	@Override
	public void finishUpdate(View arg0) {
	}
	
	//��õ�ǰ������
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}
	
	//��ʼ��arg1λ�õĽ���
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1));
		if (arg1 == views.size()-1) {
			
		}
		return views.get(arg1);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
	
	@Override  
    public void restoreState(Parcelable arg0, ClassLoader arg1) {  
    }  
  
    @Override  
    public Parcelable saveState() {  
        return null;  
    }  
  
    @Override  
    public void startUpdate(View arg0) {  
    }  
}
