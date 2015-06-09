package us.ridiculousbakery.espressoexpress.StorePicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.FakeDataSource;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class StorePickerPagerFragment extends Fragment {


    private ViewPager vp;
    //    private ArrayAdapter<Store> aaStores;
    private StorePagerAdapter paStores;
    private Integer mapTarget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stores_map, container, false);
        vp = (ViewPager) v.findViewById(R.id.vpStores);
        paStores = new StorePagerAdapter(getActivity(), R.layout.store_item);
        vp.setAdapter(paStores);
        paStores.addAll(FakeDataSource.nearby_stores(new LatLng(2, 2)));
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                ((StorePagerAdapter)vp.getAdapter()).g
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (mapTarget != null)vp.setCurrentItem(mapTarget);
        return v;
    }

    public void notifyNewData(ArrayList<Store> stores) {
        if (paStores != null) {
            paStores.clear();
            paStores.addAll(stores);
        }
    }

    public void setMapTarget(int index) {

        mapTarget = index;
        if (vp != null) {
            Log.i("ZZZZZZ", "setMapTArget  "+ index);
            vp.setCurrentItem(index);
        }

    }
}
