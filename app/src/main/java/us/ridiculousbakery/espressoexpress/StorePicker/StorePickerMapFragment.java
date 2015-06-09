package us.ridiculousbakery.espressoexpress.StorePicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class StorePickerMapFragment extends Fragment {


    private ViewPager vp;
//    private ArrayAdapter<Store> aaStores;
    private StorePagerAdapter paStores;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stores_map, container, false);
//        vp = (ViewPager)v.findViewById(R.id.vpStores);
//        paStores = new StorePagerAdapter(getActivity(), android.R.layout.simple_list_item_1);
//        vp.setAdapter(paStores);
//        paStores.addAll(FakeDataSource.nearby_stores(new LatLng(2, 2)));

        return v;
    }
    public void notifyNewData(ArrayList<Store> stores){
//        if( paStores!=null){
//            paStores.clear(); paStores.addAll(stores);
////            paStores.setPrimaryItem(0);
//        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_store_picker_map,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
