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

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class ListFragment extends Fragment {


    private ListView lv;
    private StoreListAdapter aaStores;
    private ArrayList<Store> stores;
    private ListListener listListener;
    private Switch swMode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        stores = (ArrayList<Store>) getArguments().getSerializable("stores");
        aaStores = new StoreListAdapter(getActivity(), stores, (StoreListAdapter.ListItemListener) getActivity());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listListener = (ListListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stores_list, container, false);
        setMenuVisibility(true);
        lv = (ListView) v.findViewById(R.id.lvStores);
        swMode =(Switch) v.findViewById(R.id.swActionMode);

        if (savedInstanceState == null) {
            swMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Switch b=(Switch)v;
                    if(b.isChecked()){
                        b.setText("Schlep for Coffee");
                    }else{
                        b.setText("Pay for Coffee");
                    }
                }
            });
            Log.i("ZZZZZZZ", "aaStores " + aaStores.toString());
            lv.setAdapter(aaStores);
//            lv.addHeaderView(inflater.inflate(R.layout.list_header, container, false));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("ZZZZZZZ", "clicked onNewMapTargetRequest: " + position);
                    listListener.onMapsRequired();
                    listListener.onNewMapTarget(position);

                }

            });
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setMenuVisibility(true);
      Log.i("ZZZZZZZZ", "SHOWING MENU") ;
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_store_picker_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public interface ListListener {
        public void onNewMapTarget(int index);

        public void onMapsRequired();
    }

}
