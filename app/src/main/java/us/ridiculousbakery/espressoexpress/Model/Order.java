package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bkuo on 6/3/15.
 */
public class Order implements Serializable{
    public Order(String username, LatLng loc){
        this.latLng=loc;
        this.user= new User(username);
    }

    private ArrayList<LineItem> lineItems;

    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
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
