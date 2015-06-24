package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;
import com.parse.PushService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderInProgressFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderPlacedFragment;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.R;

public class ReceivingActivity extends AppCompatActivity {

    private OrderPlacedFragment orderPlacedFragment;
    private OrderInProgressFragment orderInProgressFragment;
    private String deliverID;
    private String orderID;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private ChatArrayAdapter aMessages;//= new ChatArrayAdapter(this,)
    private ArrayList<String> messages = new ArrayList<String>();
    private ReceivingFragmentPagerAdapter receivingFragmentPagerAdapter;
    private ArrayList<String> titles = new ArrayList<String>();
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabsStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiving_activity);
        PushService.setDefaultPushCallback(this, ReceivingActivity.class);
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragments.add(ProgressFragment.instance());
        titles.add("Progress");

        receivingFragmentPagerAdapter = new ReceivingFragmentPagerAdapter(getSupportFragmentManager(), titles, fragments);

        if (savedInstanceState == null) {
            viewPager.setAdapter(receivingFragmentPagerAdapter);
            tabsStrip.setViewPager(viewPager);

        }

    }

    public void addChatFragment() {
        if (fragments.indexOf(ChatFragment.instance()) >= 0) return;
        fragments.add(ChatFragment.instance());
        titles.add("Chat");
        receivingFragmentPagerAdapter.notifyDataSetChanged();
        tabsStrip.notifyDataSetChanged();
    }

    public void addMapFragment() {
        if (fragments.indexOf(MapFragment.instance()) >= 0) return;
        fragments.add(MapFragment.instance());
        titles.add("Track");
        receivingFragmentPagerAdapter.notifyDataSetChanged();
        tabsStrip.notifyDataSetChanged();
    }

    public void addOrderConfirmFragment() {
    }


    private void updateStatus(JSONObject json) {
        String orderId = json.optString("orderId", null);
        String status = json.optString("status", null);

        if (status.equals(Order.ACCEPTED)) addChatFragment();
        if (status.equals(Order.PICKED_UP) || status.equals(Order.DELIVERED)) {
            addChatFragment();
            addMapFragment();
        }
        ProgressFragment.instance().activate(status);
    }

    private void applyChat(JSONObject json) {
        addChatFragment();
        String senderId = json.optString("senderId", null);
//        if(senderId.equals(ParseUser.getCurrentUser().getObjectId())) return;
        String message = json.optString("message", null);
        aMessages.add(new Message(
                message,
                senderId.equals(ParseUser.getCurrentUser().getObjectId()) ? 0 : 1
        ));
        ChatFragment.instance().scrollToEnd();
    }

    Order order;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//                Log.i("ZZZZZZ R_A", "onReceive!  "+status+" /"+orderId);

                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                String type = json.optString("type", "");
                if (type.equals("status")) updateStatus(json);
                if (type.equals("chat")) applyChat(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("ZZZZZZ R_A", "onReceive!  CELEBRATE");
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        Log.i("ZZZZZZ", "called unregister");
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(XpressReceiver.intentAction));
        Log.i("ZZZZZZZ", "called register on " + XpressReceiver.intentAction);
    }
}
//    private void checkOrderStatus() {
//
//        if (deliverID == null) {
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
//            query.getInBackground(orderID, new GetCallback<ParseObject>() {
//                public void done(ParseObject object, ParseException e) {
//                    if (e == null) {
//                        deliverID = (String) object.get("deliverer_id");
//                        if (deliverID != null) {
//                            switchToPickupConfirmed(deliverID);
//                        }
//                        // object will be your game score
//                    } else {
//                        // something went wrong
//                    }
//                }
//            });
//        }
////        switchToPickupConfirmed(userID);
//
//    }
//
//
//    private void switchToPickupConfirmed(String fromUserID) {
//        orderInProgressFragment = OrderInProgressFragment.newInstance(false, fromUserID);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flContainer, orderInProgressFragment);
//        ft.commit();
//    }
//}
