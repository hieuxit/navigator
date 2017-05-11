package io.fruitful.navigator.sample.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.fruitful.navigator.NavigatorFragment;
import io.fruitful.navigator.sample.R;

/**
 * Created by hieuxit on 6/21/16.
 */

public class ViewPagerFragment extends NavigatorFragment {

    public static ViewPagerFragment newInstance() {
        return new ViewPagerFragment();
    }

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public boolean handleBackIfNeeded() {
        NavigatorFragment fragment = pagerAdapter.getFragments().get(viewPager.getCurrentItem());
        if (!fragment.getChildNavigator().isBackStackEmpty()) {
            fragment.getChildNavigator().goBack(false);
            return true;
        }
        return super.handleBackIfNeeded();
    }

    static class MyPagerAdapter extends FragmentStatePagerAdapter {

        private SparseArray<NavigatorFragment> mFragments = new SparseArray<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ViewPagerItemFragment.newInstance(String.format("Fragment %d", position));
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.format("tab%d", position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                mFragments.put(position, (NavigatorFragment) obj);
            }
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public SparseArray<NavigatorFragment> getFragments() {
            return mFragments;
        }
    }
}
