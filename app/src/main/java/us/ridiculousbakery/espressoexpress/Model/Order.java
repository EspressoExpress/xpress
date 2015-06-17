package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bkuo on 6/3/15.
 */
public class Order implements Serializable{
    private ArrayList<LineItem> lineItems;
        private Double lon;;
    private Double lat;
    private long valid_until;
    private long created_at;
    private Store store;
    private User user;
    private String receiverId;

    public Order(String username, LatLng latlng, String receiverId){
        this.lon = latlng.longitude;

        this.lat = latlng.latitude;
        this.receiverId = receiverId;
        this.user= new User(username);
    }

public Order(){}

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lon);
    }

    public ArrayList<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(ArrayList<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public long getValid_until() {
        return valid_until;
    }

    public void setValid_until(long valid_until) {
        this.valid_until = valid_until;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
