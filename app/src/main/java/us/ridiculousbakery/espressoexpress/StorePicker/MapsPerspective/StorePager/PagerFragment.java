package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;

/**
 * Created by bkuo on 6/6/15.
 */
public class PagerFragment extends Fragment implements ViewPager.OnPageChangeListener {


    private ViewPager viewPager;
    private List<Store> stores;
    private StorePagerAdapter paStores;
    private Integer mapTarget;
    private View rootView;
    //    private ViewPager.OnPageChangeListener pageChangeListener;
    private PagerListener pagerListener;
    private StoreElementListener storeElementListener;
    private LatLng currentLatLng;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        stores = (ArrayList<Store>) getArguments().getSerializable("stores");
        storeElementListener =  (StoreElementListener) getActivity();

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

            currentLatLng =(LatLng) getArguments().getParcelable("currentLatLng");
            Store.findInBackground(currentLatLng, new FindCallback<Store>() {
                @Override
                public void done(List<Store> store_list, ParseException e) {
                    if (e == null) {
                        stores = store_list;
                        Log.i("ZZZZZZZ", "pager stores: " + stores.size());
                        paStores = new StorePagerAdapter(getActivity(), stores, (StoreElementListener) getActivity());

                        String storeId =getArguments().getString("storeId");
                        currentLatLng =(LatLng) getArguments().getParcelable("currentLatLng");

                        Integer position=0;
                        for(int i = 0; i < stores.size(); i++){
                            if(stores.get(i).getObjectId()==storeId)position=i;
                        }
                        Log.i("ZZZZZZ", "position found at "+position);
                        viewPager.setAdapter(paStores);
                        viewPager.setOnPageChangeListener(PagerFragment.this);
                        viewPager.setCurrentItem(position);
                        pagerListener.onNewMapTargetRequest(position, false);
                        viewPager.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i("ZZZZZZZ", "clicked!!!");
                                storeElementListener.onStoreElementClicked(stores.get(viewPager.getCurrentItem()));
                            }
                        });

                    }
                }
            });


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

    public void setPageIndex(int i) {
        viewPager.setCurrentItem(i);
    }


    public interface PagerListener {
        public void onNewMapTargetRequest(int t, boolean animate);

    }

}
