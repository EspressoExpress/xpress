package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class PagerFragment extends Fragment implements ViewPager.OnPageChangeListener {


    private ViewPager viewPager;
    private ArrayList<Store> stores;
    private StorePagerAdapter paStores;
    private Integer mapTarget;
    private View rootView;
    //    private ViewPager.OnPageChangeListener pageChangeListener;
    private PagerListener pagerListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stores = (ArrayList<Store>) getArguments().getSerializable("stores");
        paStores = new StorePagerAdapter(getActivity(), stores, (StorePagerAdapter.PagerItemListener) getActivity());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        pagerListener = (PagerListener) activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stores_pager, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.vpStores);
        if (savedInstanceState == null) {
            Integer position =getArguments().getInt("position");
            viewPager.setAdapter(paStores);
            viewPager.setOnPageChangeListener(this);
            viewPager.setCurrentItem(position);
            pagerListener.onNewMapTargetRequest(position, false);


        }

        return rootView;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        pagerListener.onNewMapTargetRequest(position, true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {   }

    public interface PagerListener {
        public void onNewMapTargetRequest(int t, boolean animate);

    }

}