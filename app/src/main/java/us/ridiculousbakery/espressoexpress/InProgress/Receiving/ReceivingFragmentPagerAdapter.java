package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by bkuo on 5/31/15.
 */
public class ReceivingFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    final int PAGE_COUNT = 1;
    private Fragment[] fgTimelines;
    private String[] tabTitles;

    public ReceivingFragmentPagerAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
        super(fm);
        tabTitles = titles;
        fgTimelines= fragments;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return fgTimelines[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}