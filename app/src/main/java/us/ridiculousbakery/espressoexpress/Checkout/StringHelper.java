package us.ridiculousbakery.espressoexpress.Checkout;

import android.location.Address;

/**
 * Created by mrozelle on 6/14/2015.
 */
public class StringHelper {
    public static String addressToString(Address address) {
        StringBuilder stringBuilder = new StringBuilder();
        String divider = ", ";
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            stringBuilder.append(divider).append(address.getAddressLine(i));
        }
        return stringBuilder.toString().substring(divider.length());
    }
}
