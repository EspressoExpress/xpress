package us.ridiculousbakery.espressoexpress.InProgress.Delivering;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import us.ridiculousbakery.espressoexpress.InProgress.Fragments.OrderInProgressFragment;
import us.ridiculousbakery.espressoexpress.R;

public class DeliveringActivity extends AppCompatActivity {

    private OrderInProgressFragment orderInProgressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivering);
        //String parseOrderID = getIntent().getStringExtra("parseOrderID");
        String parseOrderID = "az8P3CaZTR";
        //String userID = getIntent().getStringExtra("userID");
        //String userID = "HmR0es0hPp";
        if (savedInstanceState == null) {
            orderInProgressFragment = OrderInProgressFragment.newInstance(false, parseOrderID);
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
