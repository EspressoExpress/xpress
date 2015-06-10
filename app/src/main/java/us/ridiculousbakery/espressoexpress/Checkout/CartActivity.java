package us.ridiculousbakery.espressoexpress.Checkout;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;

public class CartActivity extends ActionBarActivity implements CartFragment.OnItemClickedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cart);
        //create fake order
        Order order = new Order();
        order.setStore(new Store("Starbucks"));
        ArrayList<LineItem> orderItems = new ArrayList<>();
        orderItems.add(new LineItem(new Item("Latte")));
        orderItems.add(new LineItem(new Item("Frapp")));
        order.setLineItems(orderItems);
        if (savedInstanceState == null) {
            // Create order fragment
            CartFragment cartFragment = CartFragment.newInstance(order);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flCart, cartFragment);
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
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

    @Override
    public void launchAddressMap() {
        FragmentManager fm = getSupportFragmentManager();
        AddressMapFragment addressMapFragment = AddressMapFragment.newInstance();
        addressMapFragment.show(fm, "address_map");
    }
}
