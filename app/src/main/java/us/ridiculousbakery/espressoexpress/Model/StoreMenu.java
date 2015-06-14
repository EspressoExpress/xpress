package us.ridiculousbakery.espressoexpress.Model;

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

    public StoreMenu(boolean largeFakeData) {

        categories = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (int i=0; i<4; i++) {
            ArrayList<Item> newItems = new ArrayList<Item>();
            for (int j=0; j<10; j++) {
                Item item = new Item("Frapppppp");
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
