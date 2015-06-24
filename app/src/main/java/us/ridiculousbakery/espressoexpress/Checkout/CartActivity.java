package us.ridiculousbakery.espressoexpress.Checkout;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.devmarvel.creditcardentry.library.CreditCard;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.TempOrder;
import us.ridiculousbakery.espressoexpress.NavDrawer.NavDrawerLocationBaseActivity;
import us.ridiculousbakery.espressoexpress.R;

public class CartActivity extends NavDrawerLocationBaseActivity implements
        CartFragment.OnWidgetClickedListener,
        AddressMapFragment.OnWidgetClickedListener,
        AddressListFragment.OnWidgetClickedListener,
        CCFormFragment.OnWidgetClickedListener,
        CartItemAdapterSwipe.OnItemDeleteListener{

    CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_cart);


        //test object

        TempOrder order = new TempOrder();
        ArrayList<LineItem> orderItems = new ArrayList<>();
        orderItems.add(new LineItem(new Item("Latte"), null, 3.00));
        orderItems.add(new LineItem(new Item("Frapp"), null, 3.00));
        order.setLineItems(orderItems);

        //order.setStore(new Store("Starbucks"));
        String storeID = "1NoCwWrzM5";
        if (savedInstanceState == null) {
            // Create order fragment
            //cartFragment = CartFragment.newInstance(getIntent().getStringExtra("orderId"));
            cartFragment = CartFragment.newInstance(order, storeID);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flCart, cartFragment);
            ft.commit();
        }

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

    @Override
    public void onDeleteItem(int position) {
        cartFragment.deleteItem(position);
    }
}
