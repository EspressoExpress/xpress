package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.MenuActivity;
import us.ridiculousbakery.espressoexpress.InProgress.Delivering.DeliveringActivity;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.NavDrawer.NavDrawerBaseActivity;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.PagerFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.StorePagerAdapter;


public class MapPickerActivity extends NavDrawerBaseActivity implements
         PagerFragment.PagerListener,
        StorePagerAdapter.PagerItemListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private MapFragment fgMapStoreFragment;

    private PagerFragment fgPagerStoreFragment;

    private int position;

    public GoogleMap getMap() {
        return map;
    }

    private GoogleMap map;
    private ArrayList<Store> stores;
    private ArrayList<MarkedStore> marked_stores;


    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stores = (ArrayList<Store>)getIntent().getSerializableExtra("stores");
        marked_stores = MarkedStore.decorateList(stores);
        position =getIntent().getIntExtra("position", 0);

        setContentView(R.layout.activity_map_picker);

        if (savedInstanceState == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            getPagerFragment().setArguments(getIntent().getExtras());
            activate_map_and_pager_fragments();
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_store_pick_list) {
//          finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private MapFragment getMapStoreFragment() {
        if (fgMapStoreFragment != null) return fgMapStoreFragment;
        fgMapStoreFragment = new MapFragment();
        return fgMapStoreFragment;
    }


    private PagerFragment getPagerFragment() {
        if (fgPagerStoreFragment != null) return fgPagerStoreFragment;
        return fgPagerStoreFragment = new PagerFragment();
    }

    private void activate_map_and_pager_fragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fgMapStoreFragment = new MapFragment();
        fgMapStoreFragment.setMenuVisibility(true);
        ft.replace(R.id.flMapContainer, fgMapStoreFragment);
        ft.add(R.id.flPagerContainer, getPagerFragment());
        ft.commit();
    }

    public void reconnect() {
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ZZZZZZZ", "onStart");
        reconnect();

    }
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        reconnect();

                        break;
                }

        }
    }


    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("ZZZZZZ", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

		/*
         * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNewMapTargetRequest(final int index, final boolean animate) {
        Log.i("ZZZZZZZ", "Entered onNewMapTargetRequest " + index);
        if(map==null){
            Log.i("ZZZZZZZ", "getting map async");
            getMapStoreFragment().getMapAsync(
                    new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if(map==null) {
                                map = googleMap;
                                map.setMyLocationEnabled(true);

                                populateMap();
                                map.setOnMarkerClickListener(MapPickerActivity.this);
                            }
                            animateToStore(animate, index);
                        }
                    }
            );
        }else {
            Log.i("ZZZZZZZ", "map exists");
            MapsInitializer.initialize(this);
            animateToStore(animate, index);

        }

    }
    private HashMap<Marker, Store> marker2Store = new HashMap<>();
    private HashMap<Marker, Order> marker2Order = new HashMap<>();

    private void populateMap(){
        for (MarkedStore store : marked_stores) {
            store.marker= map.addMarker(
                   new MarkerOptions()
                            .position(store.getLatLng())
                            .title(store.getName())
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    Bitmap.createScaledBitmap(
                                            BitmapFactory.decodeResource(getResources(), store.getLogo())
                                            , 120, 120, true
                                    )))
                            );
            marker2Store.put(store.marker, store.store);
            for (MarkedOrder order : store.getMarkedOrders()) {
                order.marker =map.addMarker(
                        new MarkerOptions()
                                .position(order.getLatLng())
                                .title(order.getName())
                                .alpha(0.5f)
                                .visible(false)
                                .icon(
                                        BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
                                )
                );
                marker2Order.put(order.marker, order.order);
            }
        }



    }

    private void animateToStore(boolean animate, int index) {
        animateToStore(animate, stores.get(index));
    }

    private void animateToStore(boolean animate, Store store) {
        LatLng latLng = store.getLatLng();
        boolean visible;
        for(MarkedStore s : marked_stores){
            if(s.store == store){
                s.marker.setAlpha(1);
                for(MarkedOrder o : s.getMarkedOrders()){  o.marker.setVisible(true);  }
            }else{
                s.marker.setAlpha(0.4f);
                for(MarkedOrder o : s.getMarkedOrders()){  o.marker.setVisible(false);  }
            }

        }
        moveCamera(animate, latLng);
    }

    private void moveCamera(boolean animate, LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        if(animate==true)    map.animateCamera(cameraUpdate);
        else map.moveCamera(cameraUpdate);
    }


    @Override
    public void gotoMenu(Store store){
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("menu", new StoreMenu(true));
        i.putExtra("store", store);
        startActivity(i);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Store store =marker2Store.get(marker);
        Order order = marker2Order.get(marker);
//        if(store!=null) animateToStore(true, store);
        if(order!=null) showOrderAcceptDialog(order);
        if(store!=null ){
            getPagerFragment().setPageIndex(stores.indexOf(store));
        }
        return false;
    }

    private void showOrderAcceptDialog(final Order order) {
        View messageView = LayoutInflater.from(this).
                inflate(R.layout.order_accept_dialog, null);
        TextView tvUserName= (TextView) messageView.findViewById(R.id.tvOrderUserName);
        tvUserName.setText(order.getUser().getName());
        TextView tvTOS = (TextView) messageView.findViewById(R.id.tvTOS);
        RoundedImageView ivLogo = (RoundedImageView) messageView.findViewById(R.id.ivLogo);
        ivLogo.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), order.getStore().getLogo()), 100, 100, true));
        tvTOS.setText(getResources().getString(R.string.confirmation_dialog_message, order.getUser().getName(), order.getStore().getName()));
        // Create alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set message_item.xml to AlertDialog builder
        alertDialogBuilder.setView(messageView);

        // Create alert dialog88888
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // Configure dialog button (OK)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "I got it!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.i("ZZZZZZZ",  "order for "+ order.getUser().getName() + " accepted");
                        Intent i = new Intent(MapPickerActivity.this, DeliveringActivity.class);
                        startActivity(i);
                        // Define color of marker icon
//                        BitmapDescriptor defaultMarker =
//                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//                        // Extract content from alert dialog
//                        String title = ((EditText) alertDialog.findViewById(R.id.etTitle)).
//                                getText().toString();
//                        String snippet = ((EditText) alertDialog.findViewById(R.id.etSnippet)).
//                                getText().toString();
//

                    }
                });


        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Log.i("ZZZZZZZ",  "order for "+ order.getUser().getName() + " declined");
                        dialog.cancel(); }
                });
        // Display the dialog
        alertDialog.show();
    }


    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }



}
