package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bkuo on 6/3/15.
 */

@ParseClassName("Order")

public class Order extends ParseObject {
    private ArrayList<LineItem> lineItems;

    private User user;

    public boolean matchesDelivererId(String id) {
        return get("deliverer_id").equals(id);
    }

    public boolean matchesReceiverId(String id) {
        return get("receiver_id").equals(id);
    }

    public static void getInBackground(String id, GetCallback<Order> cb) {
        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.getInBackground(id, cb);
    }
//    public static Order get(String id ) {
//        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
//        return query.getFirst(id);
//
//    }

    public static void findUnacceptedInBackground(LatLng ll, FindCallback<Order> cb) {
        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.whereEqualTo("status", "placed");
        query.whereGreaterThan("delivery_lat", ll.latitude - 0.025);
        query.whereLessThan("delivery_lat", ll.latitude + 0.025);
        query.whereGreaterThan("delivery_lat", ll.longitude - 0.025);
        query.whereLessThan("delivery_lon", ll.longitude + 0.025);
        query.findInBackground(cb);
    }

    public static void getFirstAcceptedbyDelivererInBackground(String schelperId, GetCallback<Order> cb) {
        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.whereEqualTo("status", "accepted");
        query.whereEqualTo("deliverer_id", schelperId);
        query.getFirstInBackground(cb);
    }

    public static void getFirstAcceptedbyReceiverInBackground(String receiverId, GetCallback<Order> cb) {
        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.whereEqualTo("status", "accepted");
        query.whereEqualTo("receiver_id", receiverId);
        query.getFirstInBackground(cb);
    }

    public static void getAcceptedParticipatingInBackground(String userId, GetCallback<Order> cb) {
        ParseQuery<Order> dquery = ParseQuery.getQuery(Order.class);
        ParseQuery<Order> rquery = ParseQuery.getQuery(Order.class);
        ParseQuery<Order> squery = ParseQuery.getQuery(Order.class);
        dquery.whereEqualTo("deliverer_id", userId);
        rquery.whereEqualTo("receiver_id", userId);
        List<ParseQuery<Order>> queries = new ArrayList<ParseQuery<Order>>();
        queries.add(dquery);
        queries.add(rquery);
        ParseQuery<Order> qmain = ParseQuery.or(queries);
        qmain.whereEqualTo("status", "accepted");
        qmain.getFirstInBackground(cb);
    }

    public static Order getAcceptedParticipating(String userId) {
        ParseQuery<Order> dquery = ParseQuery.getQuery(Order.class);
        ParseQuery<Order> rquery = ParseQuery.getQuery(Order.class);
        ParseQuery<Order> squery = ParseQuery.getQuery(Order.class);
        dquery.whereEqualTo("deliverer_id", userId);
        rquery.whereEqualTo("receiver_id", userId);
        List<ParseQuery<Order>> queries = new ArrayList<ParseQuery<Order>>();
        queries.add(dquery);
        queries.add(rquery);
        ParseQuery<Order> qmain = ParseQuery.or(queries);
        qmain.whereEqualTo("status", "order picked up"); //TODO shoudl or "order submitted"
        try {
            return qmain.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void findForStoreInBackground(Store store, FindCallback<Order> cb) {
        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.whereEqualTo("storeId", store.getObjectId());
        query.findInBackground(cb);
    }

    public static List<Order> findForStore(Store store) {
        ParseQuery<Order> query = ParseQuery.getQuery(Order.class);
        query.include("Store");
        query.whereEqualTo("status", "order submitted");
        query.whereEqualTo("store_name", store.getName());
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateOrderToPickedUp(Order order) throws ParseException {
        String receiverId = order.getReceiverId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("receiver_id", receiverId);
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject result;
        try {
            result = query.getFirst();
            result.put("status", "order accepted");
            result.put("deliverer_id", user.getObjectId());
            result.put("deliverer_name", user.get("displayName"));
            result.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Order() {
    }

    public void setDelivery_lat(Double delivery_lat) {
        put("delivery_lat", delivery_lat);
    }

    public void setDelivery_lon(Double delivery_lon) {
        put("delivery_lon", delivery_lon);
    }

    public LatLng getLatLng() {
        return new LatLng(getDouble("delivery_lat"), getDouble("delivery_lng"));
    }

    public ArrayList<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(ArrayList<LineItem> lineItems) {
        this.lineItems = lineItems;
    }


    public Store getStore() {
        return (Store) getParseObject("Store");
    }

    public void setStore(Store store) {
        put("Store", store);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReceiverId() {
        return getString("receiver_id");
    }

    public String getDelivererId() {
        return getString("deliverer_id");
    }

    public String getName() {
        return getString("name");
    }

    public String bob() {

        return new JSONArray(new ArrayList<>()).toString();
    }
}
