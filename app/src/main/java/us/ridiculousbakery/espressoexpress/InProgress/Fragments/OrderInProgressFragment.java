package us.ridiculousbakery.espressoexpress.InProgress.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.DisplayHelper;
import us.ridiculousbakery.espressoexpress.InProgress.Delivering.DeliveringHeaderFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Receiving.ReceivingHeaderFragment;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/15/15.
 */
public class OrderInProgressFragment extends Fragment {

    public static OrderInProgressFragment newInstance(boolean isDelivering) {
        OrderInProgressFragment fragmentDemo = new OrderInProgressFragment();
        Bundle args = new Bundle();
        args.putBoolean("isDelivering", isDelivering);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    private ReceivingHeaderFragment receivingHeaderFragment;
    private DeliveringHeaderFragment deliveringHeaderFragment;

    private ParseUser otherUser;

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        boolean isDelivering = getArguments().getBoolean("isDelivering");

        // Get real user here
        otherUser = ParseUser.getCurrentUser();

        if (isDelivering) {
            deliveringHeaderFragment = DeliveringHeaderFragment.newInstance(DisplayHelper.getProfileUrl(otherUser.getObjectId()), otherUser.getUsername());
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.flHeaderContainer, deliveringHeaderFragment);
            ft.commit();
        } else {
            receivingHeaderFragment = ReceivingHeaderFragment.newInstance(DisplayHelper.getProfileUrl(otherUser.getObjectId()), otherUser.getUsername());
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.flHeaderContainer, receivingHeaderFragment);
            ft.commit();
        }

        View v = inflater.inflate(R.layout.fragment_order_in_progress, container, false);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);
        return v;
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "Map", "Chat" };

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new DeliveryMapFragment();
            } else {
                return new ChatFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}


