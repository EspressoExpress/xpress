package us.ridiculousbakery.espressoexpress.InProgress.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import us.ridiculousbakery.espressoexpress.InProgress.Adapters.ChatListAdapter;
import us.ridiculousbakery.espressoexpress.Model.Message;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/14/15.
 */


public class ChatFragment extends Fragment {

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    private Button btnSend;
    private EditText etMessage;
    private ListView lvChat;
    private ChatListAdapter aMessages;
    private ArrayList<Message> messages;
    private String userID;
    private String targetUserID;
    private String chatID;

    private Handler handler = new Handler();


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
        // Get this from an intent
        targetUserID = "Hello";
        chatID = "bajeezia";

        handler.postDelayed(runnable, 100);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 100);
        }
    };

    private void refreshMessages() {
        receiveMessages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        etMessage = (EditText) v.findViewById(R.id.etMessage);
        lvChat = (ListView) v.findViewById(R.id.lvChat);
        btnSend = (Button) v.findViewById(R.id.btnSend);

        messages = new ArrayList<Message>();
        aMessages = new ChatListAdapter(getActivity(), userID, messages);
        lvChat.setAdapter(aMessages);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etMessage.getText().toString();
                //ParseObject message = new ParseObject("Message");
                Message message = new Message();
                message.setText(text);
                message.setUserId(userID);
                message.setTargetUserId(targetUserID);
                message.setChatId(chatID);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        receiveMessages();
                    }
                });
                etMessage.setText("");

            }
        });

        return v;
    }

    private void receiveMessages() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        query.whereEqualTo("chatId", chatID);
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    Log.d("DEBUG", messages.toString());
                    aMessages.clear();
                    aMessages.addAll(messages);
                    aMessages.notifyDataSetChanged(); // update adapter
                    lvChat.invalidate(); // redraw listview
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });

    }
}
