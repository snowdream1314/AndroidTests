package com.example.viewpagertest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	ViewPager pager = null;
	PagerTabStrip tabStrip = null;
	ArrayList<View> viewContainer = new ArrayList<View>();
	ArrayList<String> titleContainer = new ArrayList<String>();
	
//	private ViewPagerAdapter vpAdapter;
	
	public String TAG = "tag";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View view1 = inflater.inflate(R.layout.tab1, null);
//		View view2 = inflater.inflate(R.layout.tab2, null);
		View view3 = inflater.inflate(R.layout.tab1, null);
		//viewPager开始添加view
		viewContainer.add(view1);
//		viewContainer.add(view2);
		viewContainer.add(view3);
		
		//初始化Adapter
//		vpAdapter = new ViewPagerAdapter(viewContainer, this);
		pager = (ViewPager) findViewById(R.id.viewpager);
//		pager.setAdapter(vpAdapter);
//		pager.setOnPageChangeListener(this);
	
		pager.setAdapter(new PagerAdapter() {
			//viewPager中的组件数量
			@Override
			public int getCount() {
				return viewContainer.size();
			}
			//滑动切换时销毁当前组件
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				((ViewPager) container).removeView(viewContainer.get(position));
			}
			//滑动时生成的组件
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				((ViewPager) container).addView(viewContainer.get(position));
				return viewContainer.get(position);
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
			public CharSequence getPageTitle(int position) {
				return titleContainer.get(position);
			}
		});
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                Log.d(TAG, "--------changed:" + arg0);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.d(TAG, "-------scrolled arg0:" + arg0);
                Log.d(TAG, "-------scrolled arg1:" + arg1);
                Log.d(TAG, "-------scrolled arg2:" + arg2);
            }
 
            @Override
            public void onPageSelected(int arg0) {
                Log.d(TAG, "------selected:" + arg0);
            }
		});
	}
}
