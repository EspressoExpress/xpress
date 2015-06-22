package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.parse.FindCallback;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import us.ridiculousbakery.espressoexpress.Checkout.ParseQueryHelper;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.MenuActivity;
import us.ridiculousbakery.espressoexpress.InProgress.Delivering.DeliveringActivity;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.NavDrawer.NavDrawerBaseActivity;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.PagerFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;


public class MapPickerActivity extends NavDrawerBaseActivity implements
        PagerFragment.PagerListener,
        StoreElementListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private MapFragment fgMapStoreFragment;

    private PagerFragment fgPagerStoreFragment;

    private LatLng currentLatLng;

    public GoogleMap getMap() {
        return map;
    }

    private GoogleMap map;
    private List<Store> stores;
    private ArrayList<MarkedStore> marked_stores;


    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_map_picker);
        currentLatLng = (LatLng) getIntent().getParcelableExtra("currentLatLng");
        Store.findInBackground(currentLatLng, new FindCallback<Store>() {
            @Override
            public void done(List<Store> s, ParseException e) {
                if (e == null) {
                    stores = s;
                    Log.i("ZZZZZZZ", "stores: " + stores.size());
                    marked_stores = MarkedStore.decorateList(stores);
                    mGoogleApiClient = new GoogleApiClient.Builder(MapPickerActivity.this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(MapPickerActivity.this)
                            .addOnConnectionFailedListener(MapPickerActivity.this).build();

                    getPagerFragment().setArguments(getIntent().getExtras());
                    activate_map_and_pager_fragments();
                }
            }
        });
        if (savedInstanceState == null) {

//            stores = (ArrayList<Store>)getIntent().getSerializableExtra("stores");


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_store_pick_list) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

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
        if (map == null) {
            Log.i("ZZZZZZZ", "getting map async");
            getMapStoreFragment().getMapAsync(
                    new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if (map == null) {
                                map = googleMap;
                                map.setMyLocationEnabled(true);

                                populateMap();
                                map.setOnMarkerClickListener(MapPickerActivity.this);
                            }
                            animateToStore(animate, index);
                        }
                    }
            );
        } else {
            Log.i("ZZZZZZZ", "map exists");
            MapsInitializer.initialize(this);
            animateToStore(animate, index);

        }

    }

    private HashMap<Marker, Store> marker2Store = new HashMap<>();
    private HashMap<Marker, Order> marker2Order = new HashMap<>();
    private  Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    private void populateMap() {
        for (MarkedStore store : marked_stores) {
            Bitmap src = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    getResources(),
                    getResources()
                            .obtainTypedArray(R.array.store_logos)
                            .getResourceId(store.getLogo(), R.drawable.philz_twit_logo)), 120, 120, true);


            store.marker = map.addMarker(
                    new MarkerOptions()
                            .position(store.getLatLng())
                            .title(store.getName())
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    getRoundedCornerBitmap(src)
                            ))
            );
            marker2Store.put(store.marker, store.store);
            Log.i("ZZZZZZ", "populating "+store.getMarkedOrders().size()+" orders");
            for (MarkedOrder order : store.getMarkedOrders()) {
                order.marker = map.addMarker(
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


            //query submitted order from Parse
//            ArrayList<MarkedOrder> ordersFromParse = ParseQueryHelper.getSubmittedOrderfromParse(store.getName());
//            if (!ordersFromParse.isEmpty()) {
//                for (MarkedOrder markedOrder: ordersFromParse) {
//                    //try to pass down logo
//                    markedOrder.getOrder().setStore(store.getStore());
//                    markedOrder.marker =map.addMarker(
//                            new MarkerOptions()
//                                    .position(markedOrder.getLatLng())
//                                    .title(markedOrder.getName())
//                                    .alpha(0.5f)
//                                    .visible(true)
//                                    .icon(
//                                            BitmapDescriptorFactory
//                                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE) //so we can see what from parse
//                                    ));
//                    marker2Order.put(markedOrder.marker, markedOrder.order);
//                }
//            }
        }
//        getResources().obtainTypedArray(R.array.store_logos).getResourceId()


    }

    private void animateToStore(boolean animate, int index) {
        animateToStore(animate, stores.get(index));
    }

    private void animateToStore(boolean animate, Store store) {
        LatLng latLng = store.getLatLng();
        boolean visible;
        for (MarkedStore s : marked_stores) {
            if (s.store == store) {
                s.marker.setAlpha(1);
                Log.i("ZZZZZZ", "marking  " + s.getMarkedOrders().size() + " visible orders");

                for (MarkedOrder o : s.getMarkedOrders()) {
                    Log.i("ZZZZZZ", "marker: "+o.getLatLng().toString()+" order"+ " "+ o.getName() +" "+ o.order.getObjectId());

                    o.marker.setVisible(true);
                }
            } else {
                s.marker.setAlpha(0.4f);
                for (MarkedOrder o : s.getMarkedOrders()) {
                    o.marker.setVisible(false);
                }
            }

        }
        moveCamera(animate, latLng);
    }

    private void moveCamera(boolean animate, LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        if (animate == true) map.animateCamera(cameraUpdate);
        else map.moveCamera(cameraUpdate);
    }

    @Override
    public void onStoreElementClicked(Store store) {
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("storeId", store.getObjectId());

        startActivity(i);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Store store = marker2Store.get(marker);
        Order order = marker2Order.get(marker);
//        if(store!=null) animateToStore(true, store);
        if (order != null) showOrderAcceptDialog(order);
        if (store != null) {
            getPagerFragment().setPageIndex(stores.indexOf(store));
        }
        return false;
    }

    private void showOrderAcceptDialog(final Order order) {
        View messageView = LayoutInflater.from(this).
                inflate(R.layout.order_accept_dialog, null);
        TextView tvUserName = (TextView) messageView.findViewById(R.id.tvOrderUserName);
        tvUserName.setText(order.getName());
        TextView tvTOS = (TextView) messageView.findViewById(R.id.tvTOS);
        RoundedImageView ivLogo = (RoundedImageView) messageView.findViewById(R.id.ivLogo);
        ivLogo.setImageBitmap(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                        getResources(),
                        getResources()
                                .obtainTypedArray(R.array.store_logos)
                                .getResourceId(order.getStore().getLogo(), R.drawable.philz_twit_logo)), 100, 100, true));
        tvTOS.setText(getResources().getString(R.string.confirmation_dialog_message, order.getName(), order.getStore().getName()));
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
                        // update this order on Parse
                        String parseOrderID = null;
                        try {
                            parseOrderID = ParseQueryHelper.updateSubmittedOrdertoAccepted(order);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.i("ZZZZZZZ", "order for " + order.getName() + " accepted");
                        Intent i = new Intent(MapPickerActivity.this, DeliveringActivity.class);
                        i.putExtra("parseOrderID", parseOrderID);
                        startActivity(i);
                        // Define color of marker icon

                    }
                });


        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("ZZZZZZZ", "order for " + order.getName() + " declined");
                        dialog.cancel();
                    }
                });
        // Display the dialog
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

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
