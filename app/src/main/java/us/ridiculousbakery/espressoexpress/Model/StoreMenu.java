package us.ridiculousbakery.espressoexpress.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by bkuo on 6/3/15.
 */
public class StoreMenu implements Parcelable {

    private ArrayList<Item> items;

    public ArrayList<Item> getItems() {
        return items;
    }

    public StoreMenu() {
        //normal actions performed by class, it's still a normal object!
    }

    public StoreMenu(boolean largeFakeData) {
        items = new ArrayList<Item>();
        for (int i=0; i<30; i++) {
            Item item = new Item("Frapppppp");
            items.add(item);
        }
    }

    //================================================================================
    // Parcelable
    //================================================================================

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(items);
    }

    public static final Parcelable.Creator<StoreMenu> CREATOR
            = new Parcelable.Creator<StoreMenu>() {
        @Override
        public StoreMenu createFromParcel(Parcel in) {
            return new StoreMenu(in);
        }

        @Override
        public StoreMenu[] newArray(int size) {
            return new StoreMenu[size];
        }
    };

    private StoreMenu(Parcel in) {
        items = in.createTypedArrayList(Item.CREATOR);
    }
}
