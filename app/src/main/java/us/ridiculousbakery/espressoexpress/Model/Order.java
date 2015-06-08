package us.ridiculousbakery.espressoexpress.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bkuo on 6/3/15.
 */
public class Order implements Serializable{

    private ArrayList<LineItem> lineItems;
    private Double lon;
    private Double lat;
    private long valid_until;
    private long created_at;
    private Store store;
    private User user;

    public ArrayList<LineItem> getLineItems() {
        return lineItems;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
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

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
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
