package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

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

    public void setLogo(Integer logo) {
        this.logo = logo;
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
        this.logo=logo;
        this.storeMenu=menu;
        this.orders = orders;
        for(Order order : orders){order.setStore(this);}
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

}
