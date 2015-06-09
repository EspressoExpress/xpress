package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class FakeDataSource {

    public static ArrayList<Store> nearby_stores(LatLng latlng) {
        Double lat = latlng.latitude;
        Double lon = latlng.longitude;
        ArrayList<Store> list = new ArrayList<Store>();
        list.add(new Store("Starbucks", new LatLng(lat - 0.002, lon - 0.002), R.drawable.sbux_twit_logo, new ArrayList<Order>()));
        list.add(new Store("Starbucks", new LatLng(lat - 0.002, lon + 0.002),  R.drawable.sbux_twit_logo, new ArrayList<Order>()));
        list.add(new Store("BlueBottle", new LatLng(lat + 0.002, lon - 0.002),  R.drawable.blue_twit_logo, new ArrayList<Order>()));
        list.add(new Store("Philz", new LatLng(lat + 0.002, lon + 0.002),  R.drawable.philz_twit_logo, new ArrayList<Order>()));
        return list;
    }
}
