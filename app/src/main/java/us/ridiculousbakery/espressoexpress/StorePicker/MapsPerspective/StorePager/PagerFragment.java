package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;

/**
 * Created by bkuo on 6/6/15.
 */
public class PagerFragment extends Fragment implements ViewPager.OnPageChangeListener {
    public static PagerFragment _instance;
    private StorePagerAdapter intendedAdapter;

    static public PagerFragment get_instance() {
        if (_instance == null) _instance = new PagerFragment();
        return _instance;
    }

    private ViewPager viewPager;
    private List<Store> stores;
    private StorePagerAdapter paStores;
    private Integer mapTarget;
    private View rootView;
    //    private ViewPager.OnPageChangeListener pageChangeListener;
    private PagerListener pagerListener;
    private StoreElementListener storeElementListener;

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        stores = (ArrayList<Store>) getArguments().getSerializable("stores");
        storeElementListener = (StoreElementListener) getActivity();

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

            if (intendedAdapter != null) viewPager.setAdapter(intendedAdapter);
            viewPager.setOnPageChangeListener(PagerFragment.this);
            viewPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storeElementListener.onListStoreElementClicked(viewPager.getCurrentItem());
                }
            });

        }

        return rootView;
    }

    public void setAdapter(StorePagerAdapter paStores) {
        if (viewPager == null) intendedAdapter = paStores;
        else viewPager.setAdapter(paStores);
    }

    public void setCurrentItem(int position) {
        viewPager.setCurrentItem(position);

        pagerListener.onNewMapTargetRequest(position, false);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        pagerListener.onNewMapTargetRequest(position, true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void setPageIndex(int i) {
        viewPager.setCurrentItem(i);
    }


    public interface PagerListener {
        void onNewMapTargetRequest(int t, boolean animate);

    }

}
