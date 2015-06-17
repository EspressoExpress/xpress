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
    public String getTargetUserId() { return getString("targetUserId"); }
    public String getChatId() { return getString("chatId"); }
    public String getTargetUserEmail() { return getString("targetUserEmail"); }


    public void setUserId(String userId) {
        put("userId", userId);
    }
    public void setText(String text) {
        put("text", text);
    }
    public void setTargetUserId(String userId) { put("targetUserId", userId); }
    public void setChatId(String chatId) { put("chatId", chatId); }
    public void setTargetUserEmail(String chatId) { put("targetUserEmail", chatId); }


    @Override
    public String toString() {
        return getUserId() + ":" + getText();
    }
}
