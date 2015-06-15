package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import us.ridiculousbakery.espressoexpress.InProgress.Fragments.ChatFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderInProgressFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderPlacedFragment;
import us.ridiculousbakery.espressoexpress.R;

public class ReceivingActivity extends ActionBarActivity {

    private OrderPlacedFragment orderPlacedFragment;
    private OrderInProgressFragment orderInProgressFragment;
   // private ChatFragment chatFragment;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);
        if (savedInstanceState == null) {
            orderPlacedFragment = new OrderPlacedFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, orderPlacedFragment);
            ft.commit();
        }

        handler.postDelayed(runnable, 10000);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d("Runnable", "Running");
            switchToPickupConfirmed();
            //handler.postDelayed(this, 100);
        }
    };

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

    private void switchToPickupConfirmed() {
        Log.d("Replace", "Replace");
        orderInProgressFragment = OrderInProgressFragment.newInstance(false);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, orderInProgressFragment);
        ft.commit();
    }
}
