package us.ridiculousbakery.espressoexpress.Checkout;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.devmarvel.creditcardentry.library.CreditCard;
import com.google.android.gms.maps.model.LatLng;

import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.NavDrawer.NavDrawerBaseActivity;
import us.ridiculousbakery.espressoexpress.R;

public class CartActivity extends NavDrawerBaseActivity implements
        CartFragment.OnWidgetClickedListener,
        AddressMapFragment.OnWidgetClickedListener,
        AddressListFragment.OnWidgetClickedListener,
        CCFormFragment.OnWidgetClickedListener{

    CartFragment cartFragment;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cart);
        order = (Order) getIntent().getSerializableExtra("order");
        //create fake order
        /*Order order = new Order();
        order.setStore(new Store("Starbucks"));
        ArrayList<LineItem> orderItems = new ArrayList<>();
        orderItems.add(new LineItem(new Item("Latte"), null));
        orderItems.add(new LineItem(new Item("Frapp"), null));
        order.setLineItems(orderItems);*/
        (new Intent()).putExtra("order", order);
        if (savedInstanceState == null) {
            // Create order fragment
            cartFragment = CartFragment.newInstance(order);
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

    @Override
    public void launchAddressMap(LatLng latLng) {
        launchAddressMapfromActivity(latLng);
    }

    @Override
    public void launchCCForm() {
        FragmentManager fm = getSupportFragmentManager();
        CCFormFragment ccFormFragment = CCFormFragment.newInstance();
        ccFormFragment.show(fm, "cc_form");
    }

    @Override
    public void onSelectAddress(LatLng latLng, Address address) {
        cartFragment.saveAndShowAddress(latLng, address);
    }

    @Override
    public void onSearchAddress(LatLng latLng, Address address) {
        //Toast.makeText(this, latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
        FragmentManager fm = getSupportFragmentManager();
        AddressListFragment addressListFragment = AddressListFragment.newInstance(latLng.latitude, latLng.longitude, address);
        addressListFragment.show(fm, "address_list");
    }

    @Override
    public void onCancelSearch(LatLng latLng) {
        launchAddressMapfromActivity(latLng);
    }

    @Override
    public void onSelectSearchResult(LatLng latLng) {
        launchAddressMapfromActivity(latLng);
    }

    private void launchAddressMapfromActivity(LatLng latLng) {
        FragmentManager fm = getSupportFragmentManager();
        AddressMapFragment addressMapFragment = AddressMapFragment.newInstance(latLng.latitude, latLng.longitude);
        addressMapFragment.show(fm, "address_map");
    }

    @Override
    public void onSaveCCInfo(CreditCard cc) {
        cartFragment.saveAndShowCCInfo(cc);
    }
}
