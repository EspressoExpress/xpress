package us.ridiculousbakery.espressoexpress.Model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bkuo on 6/3/15.
 */
@ParseClassName("Store")

public class Store extends ParseObject {
    private transient ArrayList<Order> orders;
    private String name;
    private Integer logo;
    private Double lon;
    private Double lat;
    private Integer background;
    private transient StoreMenu storeMenu;

//    public Store(String name) {
//        this.name = name;
//        this.orders = new ArrayList<Order>();
//    }
    public  Store(){}

    public  Store(String name){ this.name = name;}

    Store(String name, LatLng latlng, Integer logo, Integer background, ArrayList<Order> orders, StoreMenu menu) {
        this.name = name;
        this.background = background;
        this.lon = latlng.longitude;
        this.lat = latlng.latitude;
        this.logo=logo;
        this.storeMenu=menu;
        this.orders = orders;
        for(Order order : orders){order.setStore(this);}
    }

    public static Store getInForeground(String id) {
        ParseQuery<Store> query = ParseQuery.getQuery(Store.class);
        List<Store> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    public static void getInBackground(String id, GetCallback<Store> cb ){
        ParseQuery<Store> query = ParseQuery.getQuery(Store.class);
        query.getInBackground(id, cb);
    }

    public static void findInBackground(LatLng ll, FindCallback<Store> cb ){
        Log.i("ZZZZZZ", "looking for stores at " + ll.toString());
        ParseQuery<Store> query = ParseQuery.getQuery(Store.class);
        query.whereGreaterThan("lat", ll.latitude - 002.5);
        query.whereLessThan("lat", ll.latitude+002.5);
        query.whereGreaterThan("lat", ll.longitude-002.5);
        query.whereLessThan("lon", ll.longitude+002.5);
        query.findInBackground(cb);
    }

    public StoreMenu getStoreMenu() {
        return storeMenu;
    }

    public void setStoreMenu(StoreMenu storeMenu) {
        this.storeMenu = storeMenu;
    }

    public Integer getBackground() {
        return getInt("background");
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public String getName() {
        return getString("name");
    }

    public Integer getLogo() {
        return getInt("background");
    }

    public Double getLon() {
        return getDouble("lon");
    }

    public Double getLat() {
        return getDouble("lat");
    }

    public LatLng getLatLng() {
        return new LatLng(getLat(), getLon());
    }

}
