package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;

/**
 * Created by bkuo on 6/13/15.
 */
public class MarkedOrder {

    public MarkedOrder(Order order){
        this.order = order;
    }
    public Order order;
    public Marker marker;


    public Order getOrder() {
        return order;
    }

    public LatLng getLatLng() {
        return order.getLatLng();
    }

    public ArrayList<LineItem> getLineItems() {
        return order.getLineItems();
    }


    public Store getStore() {
        return order.getStore();
    }

    public String getName(){
        return order.getString("name");
    }
}
