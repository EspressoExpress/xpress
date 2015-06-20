package us.ridiculousbakery.espressoexpress.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by bkuo on 6/3/15.
 */
public class StoreMenu implements Serializable {

    private TreeMap<String, ArrayList<Item>> categories;
    private ArrayList<Item> items;

    public ArrayList<Item> getItems() {
        return items;
    }

    public TreeMap<String, ArrayList<Item>> getCategories() {
        return categories;
    }

    public StoreMenu() {
        //normal actions performed by class, it's still a normal object!
    }

    public static StoreMenu fromJSON(JSONObject json) {
        StoreMenu storeMenu = new StoreMenu();

        storeMenu.categories = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        try {
            JSONArray jsonCategories = json.getJSONArray("categories");
            for (int i=0; i<jsonCategories.length(); i++) {
                JSONObject categoryJSON = jsonCategories.getJSONObject(i);
                String category = categoryJSON.getString("name");
                JSONArray itemsJSON = categoryJSON.getJSONArray("items");
                ArrayList<Item> itemArrayList = new ArrayList<>();
                for (int j=0; j<itemsJSON.length(); j++) {
                    JSONObject itemJSON = itemsJSON.getJSONObject(j);
                    Item item = Item.fromJSON(itemJSON);
                    itemArrayList.add(item);
                }
                storeMenu.categories.put(category, itemArrayList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return storeMenu;
    }

    public void setCategories(TreeMap<String, ArrayList<Item>> categories) {
        this.categories = categories;
    }

    public StoreMenu(boolean largeFakeData) {

        categories = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (int i=0; i<4; i++) {
            ArrayList<Item> newItems = new ArrayList<Item>();
            for (int j=0; j<10; j++) {
                TreeMap<String, ArrayList<String>> options = new TreeMap<>();
                //1
                ArrayList<String> size = new ArrayList<String>();
                size.add("Small");
                size.add("Medium");
                size.add("Large");
                options.put("Size", size);
                //2
                ArrayList<String> milk = new ArrayList<String>();
                milk.add("None");
                milk.add("Splash");
                milk.add("Medium");
                milk.add("A lot");
                options.put("Milk", milk);
                //3
                ArrayList<String> sugar = new ArrayList<String>();
                sugar.add("None");
                sugar.add("Little");
                sugar.add("A lot");
                options.put("Sugar", sugar);
                Item item = new Item("Frap", options);
                newItems.add(item);
            }
            categories.put("a", newItems);

        }
    }

    //================================================================================
    // Parcelable
    //================================================================================

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeList(items);
//    }
//
//    public static final Parcelable.Creator<StoreMenu> CREATOR
//            = new Parcelable.Creator<StoreMenu>() {
//        @Override
//        public StoreMenu createFromParcel(Parcel in) {
//            return new StoreMenu(in);
//        }
//
//        @Override
//        public StoreMenu[] newArray(int size) {
//            return new StoreMenu[size];
//        }
//    };
//
//    private StoreMenu(Parcel in) {
//        items = in.createTypedArrayList(Item.CREATOR);
//    }
}
