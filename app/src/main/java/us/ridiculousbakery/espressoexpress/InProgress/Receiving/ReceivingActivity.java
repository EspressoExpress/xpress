package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderInProgressFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderPlacedFragment;
import us.ridiculousbakery.espressoexpress.R;

public class ReceivingActivity extends ActionBarActivity {

    private OrderPlacedFragment orderPlacedFragment;
    private OrderInProgressFragment orderInProgressFragment;
    private String deliverID;
    private String orderID;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);

//        userID = getIntent().getStringExtra("userID");
//        userID = "HmR0es0hPp";

        orderID = getIntent().getStringExtra("orderID");
        //orderID = "EmQOsUivdw";

        if (savedInstanceState == null) {
            orderPlacedFragment = new OrderPlacedFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, orderPlacedFragment);
            ft.commit();
        }

        handler.postDelayed(runnable, 1000);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkOrderStatus();
            handler.postDelayed(this, 1000);
        }
    };

    private void checkOrderStatus() {

        if (deliverID == null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
            query.getInBackground(orderID, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        deliverID = (String) object.get("deliverer_id");
                        if (deliverID != null) {
                            switchToPickupConfirmed(deliverID);
                        }
                        // object will be your game score
                    } else {
                        // something went wrong
                    }
                }
            });
        }


//        switchToPickupConfirmed(userID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receiving, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchToPickupConfirmed(String fromUserID) {
        orderInProgressFragment = OrderInProgressFragment.newInstance(false, fromUserID);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, orderInProgressFragment);
        ft.commit();
    }
}
