package us.ridiculousbakery.espressoexpress.InProgress.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/14/15.
 */
public class ChatFragment extends Fragment {


    private Button btnSend;
    private EditText etMessage;

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        etMessage = (EditText) v.findViewById(R.id.etMessage);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etMessage.getText().toString();
                ParseObject message = new ParseObject("Message");
                message.put("from_user", 5);
                message.put("to_user", 6);
                message.put("text", text);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        etMessage.setText("");
                    }
                });

            }
        });

        return v;
    }
}
