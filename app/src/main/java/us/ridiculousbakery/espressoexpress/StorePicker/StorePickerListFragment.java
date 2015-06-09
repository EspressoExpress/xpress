package us.ridiculousbakery.espressoexpress.StorePicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.FakeDataSource;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class StorePickerListFragment extends Fragment {


    private GoogleApiClient mGoogleApiClient;
    private ListView lv;
    private ArrayAdapter<Store> aaStores;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stores_list, container, false);

        lv = (ListView) v.findViewById(R.id.lvStores);
        aaStores = new ArrayAdapter<Store>(getActivity(), android.R.layout.simple_list_item_1);
        lv.setAdapter(aaStores);
        aaStores.addAll(FakeDataSource.nearby_stores(new LatLng(2, 2)));

        return v;
    }
    public void notifyNewData(ArrayList<Store>stores){
        if( aaStores!=null){
            aaStores.clear(); aaStores.addAll(stores);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_store_picker_list,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
