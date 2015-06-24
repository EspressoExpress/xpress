package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import us.ridiculousbakery.espressoexpress.Model.XpressUser;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/14/15.
 */


public class ChatFragment extends Fragment {

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    private Button btnSend;
    private EditText etMessage;
    private ListView lvChat;

    private String userID;
    private String targetUserID;
    private String otherUserEmail;

    private String chatID;

    private Handler handler = new Handler();

    private static ChatFragment _instance;

    public static ChatFragment instance() {
        if (_instance == null) _instance = new ChatFragment();
        return _instance;
    }

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null) {
            userID = ParseUser.getCurrentUser().getObjectId();
        } else {
            // Throw Error Here?
        }

    }

    public void setAdapter(ListAdapter l) {
        lvChat.setAdapter(l);
    }
    public void scrollToEnd(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        etMessage = (EditText) v.findViewById(R.id.etMessage);
        lvChat = (ListView) v.findViewById(R.id.lvChat);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etMessage.getText().toString();
                JSONObject obj = new JSONObject();
                try {
                    obj.putOpt("action", XpressReceiver.outerAction);
                    obj.putOpt("message", text);
                    obj.putOpt("senderId", userID);
                    obj.putOpt("senderUrl", XpressUser.getGravatarUrl(ParseUser.getCurrentUser()));
                    obj.putOpt("type", "chat");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ParsePush push = new ParsePush();
                ParseQuery query = ParseInstallation.getQuery();
//                push.setChannel();
                // Push the notification to Android users
                query.whereEqualTo("deviceType", "android");
                push.setQuery(query);
                push.setData(obj);
                push.sendInBackground();
                etMessage.setText("");
                etMessage.clearFocus();
            }});


        return v;
    }

//    private void receiveMessages() {
//        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
//        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
//        query.orderByAscending("createdAt");
//        query.whereEqualTo("chatId", chatID);
//        query.findInBackground(new FindCallback<Message>() {
//            public void done(List<Message> messages, ParseException e) {
//                if (e == null) {
//                    Log.d("DEBUG", messages.toString());
//                    aMessages.clear();
//                    aMessages.addAll(messages);
//                    aMessages.notifyDataSetChanged(); // update adapter
//                    lvChat.invalidate(); // redraw listview
//                } else {
//                    Log.d("message", "Error: " + e.getMessage());
//                }
//            }
//        });
//
//    }
}
