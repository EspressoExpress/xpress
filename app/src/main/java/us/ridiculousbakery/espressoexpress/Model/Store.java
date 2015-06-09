package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bkuo on 6/3/15.
 */
public class Store implements Serializable {
    public ArrayList<Order> getOrders() {
        return orders;
    }

    private final ArrayList<Order> orders;
    private String name;

    public String getName() {
        return name;
    }

    public int getLogo() {
        return logo;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }

    private int logo;
    private Double lon;
    private Double lat;


    public Store(String name) {
        this.name = name;
        this.orders = new ArrayList<Order>();
    }

    Store(String name, LatLng latlng, int logo, ArrayList<Order> orders) {
        this.name = name;
        this.lon = latlng.longitude;
        this.lat = latlng.latitude;
        this.logo=logo;
        this.orders = orders;
    }

}
