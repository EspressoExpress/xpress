package us.ridiculousbakery.espressoexpress.StorePicker.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.Adapters.StorePagerAdapter;
import us.ridiculousbakery.espressoexpress.StorePicker.StorePickerActivity;

/**
 * Created by bkuo on 6/6/15.
 */
public class PagerFragment extends Fragment {


    private ViewPager vp;
    private ArrayList<Store> stores;
    private StorePagerAdapter paStores;
    private Integer mapTarget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stores = (ArrayList<Store>) getArguments().getSerializable("stores");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stores_map, container, false);
        vp = (ViewPager) v.findViewById(R.id.vpStores);
        if (paStores == null) paStores = new StorePagerAdapter(getActivity(), stores);
        vp.setAdapter(paStores);
//        paStores.addAll(FakeDataSource.nearby_stores(new LatLng(2, 2)));
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if(mapListener!=null){
                    ((StorePickerActivity)getActivity()).onNewMapTarget(position);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (mapTarget != null) vp.setCurrentItem(mapTarget);
        return v;
    }

    public void notifyNewData(ArrayList<Store> stores) {
        if (paStores == null) paStores = new StorePagerAdapter(getActivity());
        vp.setAdapter(paStores);

        if (paStores != null) {
            paStores.clear();
            paStores.addAll(stores);
        }
    }

    public void setAdapter(StorePagerAdapter paStores) {
//        if(paStores == null)paStores = new StorePagerAdapter(getActivity());
        vp.setAdapter(paStores);

    }


    public void setMapTarget(int index) {

        mapTarget = index;
        if (vp != null) {
            Log.i("ZZZZZZ", "setMapTArget  " + index);
            vp.setCurrentItem(index);
        }

    }
}
