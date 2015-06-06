package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by bkuo on 6/6/15.
 */
public class FakeDataSource {

    public static ArrayList<Store> nearby_stores(LatLng latlng) {
        Double lat = latlng.latitude;
        Double lon = latlng.longitude;
        ArrayList<Store> list = new ArrayList<Store>();
        list.add(new Store("Starbucks", new LatLng(lat - 0.002, lon - 0.002), "http://test.icsc.org/2013APC/images/logo-Starbucks.png"));
        list.add(new Store("Starbucks", new LatLng(lat - 0.002, lon + 0.002), "http://test.icsc.org/2013APC/images/logo-Starbucks.png"));
        list.add(new Store("Starbucks", new LatLng(lat + 0.002, lon - 0.002), "http://test.icsc.org/2013APC/images/logo-Starbucks.png"));
        list.add(new Store("Philz", new LatLng(lat + 0.002, lon + 0.002), "http://www.philzcoffee.com/c.1145465/site/images/header_logo_v2.png"));
        return list;
    }
}
