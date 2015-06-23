package us.ridiculousbakery.espressoexpress.Checkout;

import android.location.Address;

import java.text.DecimalFormat;

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

    public static String priceToString(Double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(price).toString();
    }
}
