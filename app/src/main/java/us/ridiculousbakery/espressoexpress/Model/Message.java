package us.ridiculousbakery.espressoexpress.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by teddywyly on 6/14/15.
 */
@ParseClassName("Message")
public class Message extends ParseObject {

    public String getUserId() {
        return getString("userId");
    }

    public String getText() {
        return getString("text");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public void setText(String text) {
        put("text", text);
    }

}
