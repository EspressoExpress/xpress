package us.ridiculousbakery.espressoexpress.InProgress.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import us.ridiculousbakery.espressoexpress.DisplayHelper;
import us.ridiculousbakery.espressoexpress.InProgress.Delivering.DeliveringHeaderFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Receiving.ReceivingHeaderFragment;
import us.ridiculousbakery.espressoexpress.InProgress.SmartFragmentStatePagerAdapter;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/15/15.
 */
public class OrderInProgressFragment extends Fragment {

    public static OrderInProgressFragment newInstance(boolean isDelivering, String parseOrderID) {
        OrderInProgressFragment fragmentDemo = new OrderInProgressFragment();
        Bundle args = new Bundle();
        args.putBoolean("isDelivering", isDelivering);
        args.putString("parseOrderID", parseOrderID);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    private ReceivingHeaderFragment receivingHeaderFragment;
    private DeliveringHeaderFragment deliveringHeaderFragment;
    private ParseUser otherUser;
    private Button btStartMarker;
    private SampleFragmentPagerAdapter aPager;

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_order_in_progress, container, false);
        //btStartMarker = (Button) v.findViewById(R.id.btStartMarker);

        final boolean isDelivering = false;/*= getArguments().getBoolean("isDelivering");*/
        String parseOrderID = getArguments().getString("parseOrderID");

        //query Parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        ParseObject order_obj = null;
        try {
            order_obj = query.get(parseOrderID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseQuery<ParseObject> user_query = ParseQuery.getQuery("_User");
        ParseUser deliveringUser = null;
        ParseUser receivingUser = null;
        if (order_obj != null) {
            try {
                deliveringUser = (ParseUser) user_query.get((String) order_obj.get("deliverer_id"));
                receivingUser = (ParseUser) user_query.get((String) order_obj.get("receiver_id"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            deliveringUser = (ParseUser) user_query.get("4ND6B9PsYi");//Bert
            receivingUser = (ParseUser) user_query.get("saLdABgOFA"); //me
        } catch (ParseException e) {
            e.printStackTrace();
        }



        // SHOW LOADING UI
        if (isDelivering) {

            deliveringHeaderFragment = DeliveringHeaderFragment.newInstance(DisplayHelper.getProfileUrl(receivingUser.getEmail()), receivingUser.getString("displayName"));
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.flHeaderContainer, deliveringHeaderFragment);
            ft.commit();
        } else {
            receivingHeaderFragment = ReceivingHeaderFragment.newInstance(DisplayHelper.getProfileUrl(deliveringUser.getObjectId()), deliveringUser.getString("displayName"));
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.flHeaderContainer, receivingHeaderFragment);
            ft.commit();
        }

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        aPager = new SampleFragmentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(aPager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        //setupListeners();

        /*ParseQuery<ParseUser> query =  ParseQuery.getQuery("_User");
        query.whereEqualTo("objectID", userID);
        query.getInBackground(userID, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    Log.d("DEBUG", "Got Parse User");
                    otherUser = parseUser;

                    if (isDelivering) {
                        deliveringHeaderFragment = DeliveringHeaderFragment.newInstance(DisplayHelper.getProfileUrl(otherUser.getEmail()), otherUser.getString("displayName"));
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.replace(R.id.flHeaderContainer, deliveringHeaderFragment);
                        ft.commit();
                    } else {
                        receivingHeaderFragment = ReceivingHeaderFragment.newInstance(DisplayHelper.getProfileUrl(otherUser.getObjectId()), otherUser.getString("displayName"));
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.replace(R.id.flHeaderContainer, receivingHeaderFragment);
                        ft.commit();
                    }

                    ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
                    viewPager.setAdapter(new SampleFragmentPagerAdapter(getFragmentManager()));
                    PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
                    tabStrip.setViewPager(viewPager);

                } else {
                    Log.d("DEBUG", e.getLocalizedMessage());
                }
            }
        });*/

        return v;
    }

    private void setupListeners() {
        btStartMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryMapFragment deliveryMapFragment = (DeliveryMapFragment) aPager.getRegisteredFragment(0);
                Toast.makeText(getActivity(), "start Marker clicked", Toast.LENGTH_SHORT).show();
                deliveryMapFragment.getMapReady();
            }
        });
    }

    public class SampleFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {
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
                //return ChatFragment.newInstance(otherUser.getObjectId());
                ParseUser currentUser = null; //me
                try {
                    currentUser = (ParseUser) ParseQuery.getQuery("_User").get("saLdABgOFA");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //ParseUser currentUser = ParseUser.getCurrentUser();
                return ChatFragment.newInstance(currentUser.getObjectId(), currentUser.getEmail());
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}


