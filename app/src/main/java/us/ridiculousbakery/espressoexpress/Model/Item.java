package us.ridiculousbakery.espressoexpress.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bkuo on 6/3/15.
 */
public class Item implements Parcelable {

    private String name;

    public Item(String name) {
        this.name = name;
    }

    //================================================================================
    // Getters
    //================================================================================

    public String getName() {
        return name;
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
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Item> CREATOR
            = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    private Item(Parcel in) {
        name = in.readString();
    }

}
