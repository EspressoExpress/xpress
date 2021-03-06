package us.ridiculousbakery.espressoexpress.InProgress.Delivering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseUser;
import com.parse.PushService;

import org.json.JSONObject;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.InProgress.Fragments.DeliveryMapFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Receiving.ChatArrayAdapter;
import us.ridiculousbakery.espressoexpress.InProgress.Receiving.ChatFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Receiving.InProgressPagerAdapter;
import us.ridiculousbakery.espressoexpress.InProgress.Receiving.Message;
import us.ridiculousbakery.espressoexpress.InProgress.Receiving.ReceivingActivity;
import us.ridiculousbakery.espressoexpress.R;

public class DeliveringActivity extends AppCompatActivity {

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private ArrayList<Message> messages = new ArrayList<Message>();
    private ChatArrayAdapter aMessages;

    private InProgressPagerAdapter receivingFragmentPagerAdapter;
    private ArrayList<String> titles = new ArrayList<String>();
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabsStrip;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiving_activity);
        PushService.setDefaultPushCallback(this, ReceivingActivity.class);
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragments.add(DeliveryStepsFragment.instance());
        titles.add("Pick Up");
        aMessages= new ChatArrayAdapter(this,messages);
        receivingFragmentPagerAdapter = new InProgressPagerAdapter(getSupportFragmentManager(), titles, fragments);

        if (savedInstanceState == null) {
            viewPager.setAdapter(receivingFragmentPagerAdapter);
            tabsStrip.setViewPager(viewPager);
        }
        addChatFragment();
    }

    public void addChatFragment() {
        if (fragments.indexOf(ChatFragment.instance()) >= 0) return;
        fragments.add(ChatFragment.instance());
        titles.add("Chat");
        receivingFragmentPagerAdapter.notifyDataSetChanged();
        tabsStrip.notifyDataSetChanged();
        ChatFragment.instance().setAdapter(aMessages);
    }

    public void addMapFragment() {
        if (fragments.indexOf(DeliveryMapFragment.instance()) >= 0) return;
        fragments.add(DeliveryMapFragment.newInstance(new LatLng(37.403731, -122.112364), new LatLng(37.402794, -122.116398)));
        titles.add("Track");
        receivingFragmentPagerAdapter.notifyDataSetChanged();
        tabsStrip.notifyDataSetChanged();
    }

//    private void updateStatus(JSONObject json) {
//        String orderId = json.optString("orderId", null);
//        String status = json.optString("status", null);
//
//        if (status.equals(Order.ACCEPTED)) addChatFragment();
//        if (status.equals(Order.PICKED_UP) || status.equals(Order.DELIVERED)) {
//            addChatFragment();
//            addMapFragment();
//        }
//        DeliveryStepsFragment.instance();//.activate(status);
//    }

    private void applyChat(JSONObject json) {
        addChatFragment();
        String senderId = json.optString("senderId", null);
//        if(senderId.equals(ParseUser.getCurrentUser().getObjectId())) return;
        String message = json.optString("message", null);
        String url = json.optString("senderUrl", null);

        aMessages.add(new Message(
                message,
                senderId.equals(ParseUser.getCurrentUser().getObjectId()) ? 0 : 1,
                url
        ));
        aMessages.notifyDataSetChanged();
        Log.i("ZZZZZZ", "applied to chat");
        ChatFragment.instance().scrollToEnd();
    }

//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            try {
//                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
//                String type = json.optString("type", "");
//                if (type.equals("status")) updateStatus(json);
//                if (type.equals("chat")) applyChat(json);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.i("ZZZZZZ R_A", "onReceive!  CELEBRATE");
//        }
//    };
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
//        Log.i("ZZZZZZ", "called unregister");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(XpressReceiver.intentAction));
//        Log.i("ZZZZZZZ", "called register on " + XpressReceiver.intentAction);
//    }


}
