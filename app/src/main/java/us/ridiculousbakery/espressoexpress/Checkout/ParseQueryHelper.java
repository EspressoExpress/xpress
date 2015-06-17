package us.ridiculousbakery.espressoexpress.Checkout;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;

/**
 * Created by mrozelle on 6/17/2015.
 */
public class ParseQueryHelper {

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
