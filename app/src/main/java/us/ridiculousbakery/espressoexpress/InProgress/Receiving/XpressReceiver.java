package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by bkuo on 6/23/15.
 */
public class XpressReceiver extends BroadcastReceiver {
    private static final String TAG = "ZZZZZZZ XpressReceiver:";
    public static final String intentAction = "SEND_PUSH_XPRS";
    public static final String outerAction = "SEND_PUSH";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.d(TAG, "Receiver intent null");
        } else {
            // Parse push message and handle accordingly
            processPush(context, intent);
        }
    }

    private void processPush(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "got action " + action);
        if (action.equals(outerAction))
        {
            String channel = intent.getExtras().getString("com.parse.Channel");
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
                // Iterate the parse keys if needed
                Iterator<String> itr = json.keys();
                Intent i = new Intent(intentAction);
                i.putExtras(intent);
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    Log.d(TAG, "..." + key + " => " + json.getString(key));
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);

            } catch (JSONException ex) {
                Log.d(TAG, "JSON failed!");
            }
        }
    }


    // Handle push notification by sending a local broadcast
    // to which the activity subscribes to
    private void triggerBroadcastToActivity(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(intentAction));
    }
}
