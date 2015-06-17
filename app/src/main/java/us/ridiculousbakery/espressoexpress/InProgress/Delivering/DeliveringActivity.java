package us.ridiculousbakery.espressoexpress.InProgress.Delivering;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderInProgressFragment;
import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderPlacedFragment;
import us.ridiculousbakery.espressoexpress.R;

public class DeliveringActivity extends ActionBarActivity {

    private OrderInProgressFragment orderInProgressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivering);
        if (savedInstanceState == null) {

            String userID = getIntent().getStringExtra("userID");
            userID = "HmR0es0hPp";

            orderInProgressFragment = OrderInProgressFragment.newInstance(true, userID);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, orderInProgressFragment);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delivering, menu);
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
}
