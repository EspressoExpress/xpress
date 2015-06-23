package us.ridiculousbakery.espressoexpress;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by teddywyly on 6/15/15.
 */
public class DisplayHelper {

    public static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    public static String getProfileUrlFromEmail(final String email) {
//        return "http://2.gravatar.com/avatar/ac73bb914aef6ef42af47e0c37696e05.jpg";

        String hash =  MD5Util.md5Hex(email.toLowerCase().trim());
        return " http://2.gravatar.com/avatar/" + hash + ".jpg?d=identicon";

    }

   //ac73bb914aef6ef42af47e0c37696e05
}
