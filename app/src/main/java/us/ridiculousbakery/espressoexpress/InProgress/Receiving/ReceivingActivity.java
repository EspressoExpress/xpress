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
import com.parse.PushService;

import org.json.JSONException;
import org.json.JSONObject;

import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderInProgressFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderPlacedFragment;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.R;

public class ReceivingActivity extends AppCompatActivity {

    private OrderPlacedFragment orderPlacedFragment;
    private OrderInProgressFragment orderInProgressFragment;
    private String deliverID;
    private String orderID;


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                String orderId = json.optString("orderId", null);
                String status = json.optString("status", null);
                Log.i("ZZZZZZ R_A", "onReceive!  "+status+" /"+orderId);
                updateStatus(orderId, status);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("ZZZZZZ R_A", "onReceive!  CELEBRATE");
//            Toast.makeText(getApplicationContext(), "onReceive invoked!", Toast.LENGTH_LONG).show();
        }
    };

    private void updateStatus(String orderId, String status) {
//        if(this.orderID.equals(orderId))
            ProgressFragment.instance().activate(status);
    }

    Order order;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiving_activity);
        PushService.setDefaultPushCallback(this, ReceivingActivity.class);

//        userID = getIntent().getStringExtra("userID");
//        userID = "HmR0es0hPp";

        orderID = getIntent().getStringExtra("orderID");
        //orderID = "EmQOsUivdw";
        order = new Order();
        order.setStatus(Order.SUBMITTED);
        if (savedInstanceState == null) {

            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            String[] titles = {"Progress"};
            Fragment[] fragments = {
                    ProgressFragment.instance()

            };
            viewPager.setAdapter(new ReceivingFragmentPagerAdapter(getSupportFragmentManager(), titles, fragments));

            // Give the PagerSlidingTabStrip the ViewPager
            PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            // Attach the view pager to the tab strip
            tabsStrip.setViewPager(viewPager);

//            orderPlacedFragment = new OrderPlacedFragment();
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.flContainer, orderPlacedFragment);
//            ft.commit();
        }

//        handler.postDelayed(runnable, 1000);

    }
}

//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            checkOrderStatus();
//            handler.postDelayed(this, 1000);
//        }
//    };
//
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
