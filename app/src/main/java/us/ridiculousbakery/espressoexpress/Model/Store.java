package us.ridiculousbakery.espressoexpress.Model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bkuo on 6/3/15.
 */
public class Store implements Serializable {
    private final ArrayList<Order> orders;
    private String name;
    private Integer logo;
    private Double lon;
    private Double lat;
    private Integer background;

    public StoreMenu getStoreMenu() {
        return storeMenu;
    }

    public void setStoreMenu(StoreMenu storeMenu) {
        this.storeMenu = storeMenu;
    }

    private StoreMenu storeMenu;

    public Integer getBackground() {
        return background;
    }

    public Store(String name) {
        this.name = name;
        this.orders = new ArrayList<Order>();
    }

    Store(String name, LatLng latlng, Integer logo, Integer background, ArrayList<Order> orders, StoreMenu menu) {
        this.name = name;
        this.background = background;
        this.lon = latlng.longitude;
        this.lat = latlng.latitude;
        this.logo = logo;
        this.storeMenu = menu;
        this.orders = orders;
        for (Order order : orders) {
            order.setStore(this);
        }
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public String getName() {
        return name;
    }

    public Integer getLogo() {
        return logo;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lon);
    }

    public static Store fromParseStore(ParseStore pstore) {

    }

    public static void StoresfromParse(LatLng ll, FindCallback<ParseStore> cb) {
        ParseQuery<ParseStore> query = ParseQuery.getQuery("Store");
        query.whereLessThan("lat", ll.latitude + .0250);
        query.whereLessThan("lon", ll.longitude + 0.025);
        query.whereGreaterThan("lat", ll.latitude - .0250);
        query.whereGreaterThan("lon", ll.longitude - 0.025);
        query.findInBackground(cb);
    }

//    new FindCallback<ParseStore>() {
//        public void done(List<ParseStore> pstores, ParseException e) {
//
//            if (e == null) {
//                ArrayList<Store> new_stores = new ArrayList<Store>();
//                for (ParseStore ps : pstores) {
//                    new_stores.add(fromParseStore(ps));
//                }
//
//                Log.d("score", "Retrieved " + pstores.size() + " scores");
//            } else {
//                Log.d("parse store", "Error: " + e.getMessage());
//            }
//
//        }
//    }

    static public ArrayList<Store> stores = new ArrayList<Store>();

    static public class ParseStore extends ParseObject {

    }
}
