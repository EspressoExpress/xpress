package us.ridiculousbakery.espressoexpress.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by bkuo on 6/3/15.
 */
public class Item implements Serializable {


    private static final long serialVersionUID = 4455604316807152635L;
    private String name;
    private String priceRange;
    private String imageURL;
    private TreeMap <String, ArrayList<String>> options;

    //================================================================================
    // Constructors
    //================================================================================

    public Item(String name) {
        this.name = name;
    }
    public Item() {
    }
    public Item(String name, TreeMap options) {
        this.name = name;
        this.options = options;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public static Item fromJSON(JSONObject json) {
        Item item = new Item();
        try {
            String itemName = json.getString("name");
            item.name = itemName;
            String priceRange = json.getString("price_range");
            Log.d("PRICERANGE", priceRange);
            item.priceRange = priceRange;

            JSONArray sizes = json.getJSONArray("sizes");
            TreeMap<String, ArrayList<String>> newOptions = new TreeMap<>();
            ArrayList<String> size = new ArrayList<String>();
            for (int i=0; i<sizes.length(); i++) {
                JSONObject sizeJSON = sizes.getJSONObject(i);
                String sizeName = sizeJSON.getString("name");
                double sizePrice = sizeJSON.getDouble("price");
                size.add(sizeName + "-" + Double.toString(sizePrice));
            }
            newOptions.put("Size", size);
            //2
            ArrayList<String> milk = new ArrayList<String>();
            milk.add("None");
            milk.add("Splash");
            milk.add("Medium");
            milk.add("A lot");
            newOptions.put("Milk", milk);
            //3
            ArrayList<String> sugar = new ArrayList<String>();
            sugar.add("None");
            sugar.add("Little");
            sugar.add("A lot");
            newOptions.put("Sugar", sugar);

            item.options = newOptions;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    //================================================================================
    // Getters
    //================================================================================

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public TreeMap<String, ArrayList<String>> getOptions() {
        return options;
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
//        dest.writeString(name);
//    }
//
//    public static final Parcelable.Creator<Item> CREATOR
//            = new Parcelable.Creator<Item>() {
//        @Override
//        public Item createFromParcel(Parcel in) {
//            return new Item(in);
//        }
//
//        @Override
//        public Item[] newArray(int size) {
//            return new Item[size];
//        }
//    };
//
//    private Item(Parcel in) {
//        name = in.readString();
//    }

}
