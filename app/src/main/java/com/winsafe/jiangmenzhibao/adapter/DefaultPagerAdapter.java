package com.winsafe.jiangmenzhibao.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by shijie.yang on 2017/5/19.
 */

public class DefaultPagerAdapter extends PagerAdapter {
    private List<? extends View> views;

    public DefaultPagerAdapter(List<? extends View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return this.views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View view, int position, Object object) {
        ((ViewPager) view).removeView(this.views.get(position));
    }

    @Override
    public Object instantiateItem(View view, int position) {
        ((ViewPager) view).addView(this.views.get(position));
        return this.views.get(position);
    }
}
