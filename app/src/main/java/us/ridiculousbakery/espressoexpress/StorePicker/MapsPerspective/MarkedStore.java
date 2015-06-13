package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;

/**
 * Created by bkuo on 6/13/15.
 */
public class MarkedStore {
    public MarkedStore(Store store) {
        this.store = store;
        markedOrders = new ArrayList<MarkedOrder>();
        for(Order v :store.getOrders()) markedOrders.add(new MarkedOrder(v));
    }


    public ArrayList<MarkedOrder> getMarkedOrders() {
        return markedOrders;
    }

    public String getName() {
        return getStore().getName();
    }

    public LatLng getLatLng() {
        return getStore().getLatLng();
    }

    private Store store;
    private Marker marker;
    private ArrayList<MarkedOrder> markedOrders;


    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }


    public static ArrayList<MarkedStore> decorateList(ArrayList<Store> stores) {
        ArrayList<MarkedStore> aa = new ArrayList<MarkedStore>();
        for (Store store : stores) {
            aa.add(new MarkedStore(store));
        }
        return aa;
    }
}
