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
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.Xtoggle;

/**
 * Created by bkuo on 6/6/15.
 */
public class ListFragment extends Fragment {

    public static ListFragment _instance;
    static public ListFragment get_instance(){
        if(_instance==null) _instance = new ListFragment();
        return _instance;
    }
    private ListView lv;
    private StoreListAdapter aaStores;

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    private List<Store> stores;
    private ListListener listListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

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
        z.setOnToggleListener(listListener);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ZZZZZZZ", "clicked onNewMapTargetRequest: " + position);
                Store store = (Store) parent.getItemAtPosition(position);
                listListener.onNewMapTarget(position);

            }
        });

        if (savedInstanceState == null) {

        }
        return v;
    }

    public void setAdapter(StoreListAdapter a){
        lv.setAdapter(a);
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

    public ListAdapter getAdapter() {
        return lv.getAdapter();
    }

    public interface ListListener {
        void onNewMapTarget(int index);
        void onToggle();

    }

}
