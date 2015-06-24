package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by bkuo on 5/31/15.
 */
public class InProgressPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private ArrayList<Fragment> fgTimelines;
    private ArrayList<String> tabTitles;

    public InProgressPagerAdapter(FragmentManager fm, ArrayList<String> titles, ArrayList<Fragment> fragments) {
        super(fm);
        tabTitles = titles;
        fgTimelines= fragments;
    }

    @Override
    public int getCount() {
        return fgTimelines.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fgTimelines.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles.get(position);
    }
}