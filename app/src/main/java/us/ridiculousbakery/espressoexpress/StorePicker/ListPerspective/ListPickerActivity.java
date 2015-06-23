package us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Checkable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.MenuActivity;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.NavDrawer.NavDrawerLocationBaseActivity;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.MapFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.MapPicker;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.PagerFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.StorePagerAdapter;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;


public class ListPickerActivity extends NavDrawerLocationBaseActivity implements
        PagerFragment.PagerListener,
        StoreElementListener,
        ListFragment.ListListener {
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private ListFragment fgListStoreFragment;

    private List<Store> stores;
    private boolean list_fragment_activated = false;
    private final MapPicker mapPicker = new MapPicker(this);
//    private StoreListAdapter aaStores;
//    private StorePagerAdapter paStores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_picker);

        start_list_fragment();
        start_map_and_pager_fragments();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_store_pick_map:
                activate_map_and_pager_fragments(null);
                break;
            case R.id.action_store_pick_list:
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
        return true;
    }

    private void activate_map_and_pager_fragments(Integer position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(
                R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.hide(ListFragment.get_instance());
        ft.show(MapFragment.get_instance());
        ft.show(PagerFragment.get_instance());
        ft.addToBackStack("map");
        ft.commit();
        if (position == null) position = 0;
        PagerFragment.get_instance().setCurrentItem(position);
    }

    private void start_list_fragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, ListFragment.get_instance());
        ft.commit();
    }

    private void activate_list_fragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(ListFragment.get_instance());
        ft.hide(MapFragment.get_instance());
        ft.hide(PagerFragment.get_instance());

        ft.commit();
    }

    private void start_map_and_pager_fragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MapFragment.get_instance().setMenuVisibility(true);
        ft.replace(R.id.flMapContainer, MapFragment.get_instance());
        ft.add(R.id.flPagerContainer, PagerFragment.get_instance());
        ft.hide(MapFragment.get_instance());
        ft.hide(PagerFragment.get_instance());
        ft.commit();
    }

    private void startMenuActivity(Store store) {
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("storeId", store.getObjectId());
        startActivity(i);
    }

    public void onConnected() {
        if (currentlatLng() == null) return;
        if (list_fragment_activated) return;
//
        if (stores != null && !stores.isEmpty()) return;
        Store.findInBackground(currentlatLng(), new FindCallback<Store>() {
            @Override
            public void done(List<Store> s, ParseException e) {
                if (e == null) {
                    stores = s;
                    Log.i("ZZZZZZZ", "stores: " + s.size());

                    ListFragment.get_instance().setAdapter(new StoreListAdapter(ListPickerActivity.this, stores, ListPickerActivity.this));
                    PagerFragment.get_instance().setAdapter(new StorePagerAdapter(ListPickerActivity.this, stores, new StoreElementListener() {
                        public void onListStoreElementClicked(int position) {
                            startMenuActivity(stores.get(position));
                        }
                    }));
                    mapPicker.setStores(s);

                    activate_list_fragment();
                    list_fragment_activated = true;

                    Log.i("ZZZZZZZ", "stores: " + mapPicker.getStores().size());
//                    mapPicker.setMarked_stores(MarkedStore.decorateList(mapPicker.getStores()));
//                    PagerFragment.get_instance().setArguments(getIntent().getExtras());
//                    start_map_and_pager_fragments();
                }
            }
        });
    }

    @Override
    public void onNewMapTarget(int position) {
        activate_map_and_pager_fragments(position);
    }

    public void onListStoreElementClicked(int position) {
        Checkable s = (Checkable) findViewById(R.id.xToggle);
        if (s != null && s.isChecked()) {
            activate_map_and_pager_fragments(position);
        } else {
            startMenuActivity(stores.get(position));
        }
    }

    @Override
    public void onNewMapTargetRequest(final int index, final boolean animate) {
        Log.i("ZZZZZZZ", "Entered onNewMapTargetRequest " + index);
        if (mapPicker.getMap() == null) {
            Log.i("ZZZZZZZ", "getting map async");
            MapFragment.get_instance().getMapAsync(
                    new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if (mapPicker.getMap() == null) {
                                mapPicker.setMap(googleMap);
                                mapPicker.getMap().setMyLocationEnabled(true);

                                mapPicker.populateMap();
                                mapPicker.getMap().setOnMarkerClickListener(mapPicker);
                            }
                            mapPicker.animateToStore(animate, index);
                        }
                    }
            );
        } else {
            Log.i("ZZZZZZZ", "map exists");
            MapsInitializer.initialize(this);
            mapPicker.animateToStore(animate, index);

        }

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
}
