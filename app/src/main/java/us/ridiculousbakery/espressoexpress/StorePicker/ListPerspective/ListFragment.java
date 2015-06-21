package us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;
import us.ridiculousbakery.espressoexpress.StorePicker.Xtoggle;

/**
 * Created by bkuo on 6/6/15.
 */
public class ListFragment extends Fragment {


    private ListView lv;
    private StoreListAdapter aaStores;
    private List<Store> stores;
    private ListListener listListener;
    private Switch swMode;
    private LatLng currentLatLng;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        currentLatLng = (LatLng) getArguments().getParcelable("currentLatLng");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listListener = (ListListener) activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stores_list, null, false);
        setMenuVisibility(true);
        lv = (ListView) v.findViewById(R.id.lvStores);

        Xtoggle z = (Xtoggle) v.findViewById(R.id.xToggle);
        z.initialize();
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Store.findInBackground(currentLatLng, new FindCallback<Store>() {
            @Override
            public void done(List<Store> list, ParseException e) {
                if (e == null) {
                    stores = list;
                    Log.i("ZZZZZZZ", "store list: " + stores.size());
                    aaStores = new StoreListAdapter(getActivity(), stores, (StoreElementListener) getActivity());
                    Log.i("ZZZZZZZ", "aaStores " + aaStores.toString());
                    lv.setAdapter(aaStores);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i("ZZZZZZZ", "clicked onNewMapTargetRequest: " + position);
                            Store store  = (Store)parent.getItemAtPosition(position);
                            listListener.onNewMapTarget(store.getObjectId());

                        }
                    });

                } else e.printStackTrace();
            }
        });
        if (savedInstanceState == null) {

        }
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_store_picker_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public interface ListListener {
        void onNewMapTarget(String index);

    }

}
