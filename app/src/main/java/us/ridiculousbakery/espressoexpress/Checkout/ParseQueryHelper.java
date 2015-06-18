package us.ridiculousbakery.espressoexpress.Checkout;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.MarkedOrder;

/**
 * Created by mrozelle on 6/17/2015.
 */
public class ParseQueryHelper {

    //
    public static String updateSubmittedOrdertoPickup(Order order) throws ParseException {
        String receiverId = order.getReceiverId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("status", "order submitted");
        query.whereEqualTo("receiver_id", receiverId);
        ParseUser user = ParseUser.getCurrentUser();
        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String orderID = null;
        for (ParseObject submittedOrder_obj : results) { //should be only one order anyway
            submittedOrder_obj.put("status", "order accepted");
            submittedOrder_obj.put("deliverer_id", user.getObjectId());
            submittedOrder_obj.put("deliverer_name", user.get("displayName"));
            submittedOrder_obj.save();
            orderID = submittedOrder_obj.getObjectId();
        }

        return orderID;
    }

    //get submitted order for the other side to pick up
    public static ArrayList<MarkedOrder> getSubmittedOrderfromParse(String store_name) {
        ArrayList<MarkedOrder> markedOrders = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("status", "order submitted");
        query.whereEqualTo("store_name", store_name);
        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (ParseObject order_obj : results) {
            String name = (String) order_obj.get("name");
            String receiverID = (String) order_obj.get("receiver_id");
            Double delivery_lat = (Double) order_obj.get("delivery_lat");
            Double delivery_lng = (Double) order_obj.get("delivery_lng");
            //also need to get lineitems from order
//            Order order = new Order(name, new LatLng(delivery_lat, delivery_lng), receiverID);
//            markedOrders.add(new MarkedOrder(order));
        }
        return markedOrders;
    }

    //get Menu
    public static StoreMenu getMenufromParse(String store_name) {
        StoreMenu menu = new StoreMenu();
        TreeMap<String, ArrayList<Item>> categories = getCategories(store_name);
        menu.setCategories(categories);
        return menu;
    }

    public static TreeMap<String, ArrayList<Item>> getCategories(String store_name) {
        TreeMap<String, ArrayList<Item>> categories = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, ArrayList<String>> options = getOptions(store_name);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CatItems");
        query.whereEqualTo("store_name", store_name);
        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (ParseObject cat_obj : results) {
            ArrayList<Item> items = new ArrayList<>();
            String name = (String) cat_obj.get("category_name");
            //Toast.makeText(CartActivity.this, name, Toast.LENGTH_SHORT).show();
            ArrayList<String> item_names = (ArrayList<String>) cat_obj.get("cat_items");
            for (String item_name: item_names) {
                items.add(new Item(item_name, options));
            }
            categories.put(name, items);

        }
        return categories;
    }

    public static TreeMap<String, ArrayList<String>> getOptions(String store_name) {
        TreeMap<String, ArrayList<String>> options = new TreeMap<>();
        ParseQuery<ParseObject> options_query = ParseQuery.getQuery("Options");
        options_query.whereEqualTo("store_name", store_name);
        List<ParseObject> results = null;
        try {
            results = options_query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (ParseObject option_obj : results) {
            String name = (String) option_obj.get("option_name");
            //Toast.makeText(CartActivity.this, name, Toast.LENGTH_SHORT).show();
            ArrayList<String> option_values = (ArrayList<String>) option_obj.get("options");
            //Toast.makeText(CartActivity.this, option_values.toString(), Toast.LENGTH_SHORT).show();
            options.put(name, option_values);
            //Toast.makeText(CartActivity.this, options.get("Size").toString(), Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(CartActivity.this, options.toString(), Toast.LENGTH_LONG).show();
        return options;
    }





}
