package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mrozelle on 6/21/2015.
 */
public class TempOrder implements Serializable {
    public TempOrder(String username, LatLng latlng){
        this.lon = latlng.longitude;
        this.lat = latlng.latitude;
        this.user= new User(username);
    }
    public TempOrder(){};

    private ArrayList<LineItem> lineItems;

    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    private Double lat;

    public LatLng getLatLng() {
        return new LatLng(lat, lon);
    }

    private long valid_until;
    private long created_at;
    private Store store;
    private User user;

    public ArrayList<LineItem> getLineItems() {
        return lineItems;
    }


    public long getValid_until() {
        return valid_until;
    }

    public long getCreated_at() {
        return created_at;
    }

    public Store getStore() {
        return store;
    }

    public User getUser() {
        return user;
    }

    public void setLineItems(ArrayList<LineItem> lineItems) {
        this.lineItems = lineItems;
    }


    public void setValid_until(long valid_until) {
        this.valid_until = valid_until;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
