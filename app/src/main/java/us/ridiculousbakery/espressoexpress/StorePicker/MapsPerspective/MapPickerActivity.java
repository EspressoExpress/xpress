package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.NavDrawer.NavDrawerLocationBaseActivity;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.PagerFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;


public class MapPickerActivity extends NavDrawerLocationBaseActivity implements
        PagerFragment.PagerListener,
        StoreElementListener,
        GoogleMap.OnMarkerClickListener {
    private final MapPicker mapPicker = new MapPicker(this);
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_picker);
    }


    private void activate_map_and_pager_fragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MapFragment.get_instance().setMenuVisibility(true);
        ft.replace(R.id.flMapContainer, MapFragment.get_instance());
        ft.add(R.id.flPagerContainer, PagerFragment.get_instance());
        ft.commit();
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
                                mapPicker.getMap().setOnMarkerClickListener(MapPickerActivity.this);
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

    private  Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        return mapPicker.getRoundedCornerBitmap(bitmap);
    }
    private void populateMap() {

        mapPicker.populateMap();
    }

    private void animateToStore(boolean animate, int index) {
        mapPicker.animateToStore(animate, index);
    }

    private void animateToStore(boolean animate, Store store) {
        mapPicker.animateToStore(animate, store);
    }

    private void moveCamera(boolean animate, LatLng latLng) {
        mapPicker.moveCamera(animate, latLng);
    }

    @Override
    public void onListStoreElementClicked(int store) {
//        Intent i = new Intent(this, MenuActivity.class);
//        i.putExtra("storeId", store.getObjectId());
//
//        startActivity(i);i
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Store store = mapPicker.getMarker2Store().get(marker);
        Order order = mapPicker.getMarker2Order().get(marker);
        if (order != null) mapPicker.showOrderAcceptDialog(order);
        if (store != null) {
            PagerFragment.get_instance().setPageIndex(mapPicker.getStores().indexOf(store));
        }
        return false;
    }

    private void showOrderAcceptDialog(final Order order) {
        // Create alert dialog builder
        // set message_item.xml to AlertDialog builder

        // Create alert dialog88888
        // Configure dialog button (OK)


        // Configure dialog button (Cancel)
        // Display the dialog

        mapPicker.showOrderAcceptDialog(order);
    }

    @Override
    public void onConnected() {
        Store.findInBackground(currentlatLng(), new FindCallback<Store>() {
            @Override
            public void done(List<Store> s, ParseException e) {
                if (e == null) {
                    mapPicker.setStores(s);
                    Log.i("ZZZZZZZ", "stores: " + mapPicker.getStores().size());
                    mapPicker.setMarked_stores(MarkedStore.decorateList(mapPicker.getStores()));
                    PagerFragment.get_instance().setArguments(getIntent().getExtras());
                    activate_map_and_pager_fragments();
                }
            }
        });
    }


}
