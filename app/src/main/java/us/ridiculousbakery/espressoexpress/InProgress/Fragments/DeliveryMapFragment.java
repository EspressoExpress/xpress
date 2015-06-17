package us.ridiculousbakery.espressoexpress.InProgress.Fragments;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import us.ridiculousbakery.espressoexpress.InProgress.DirectionsJSONParser;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/15/15.
 */
public class DeliveryMapFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private long MARKER_MOVE_INTERVAL = 2000;
    private static final LatLng DEFAULT_DELIVERY_ADDRESS = new LatLng(37.402794, -122.116398); //Box HQ
    private static final LatLng DEFAULT_COFFEE_SHOP_ADDRESS = new LatLng(37.403731, -122.112364); //StarBucks near Box HQ
    private Marker startMarker;
    private Marker endMarker;
    private List<LatLng> directionSteps;


    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                loadMap(map);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delivery_map, container, false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.deliveryMapView, mapFragment).commit();
        /*if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }*/
        return v;
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);
            mapAnimationStart();


            // Now that map has loaded, let's get our location!
            /*mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();*/

        } else {
            Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void mapAnimationStart() {
        BitmapDescriptor startMarkerIcon =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        startMarker = map.addMarker((new MarkerOptions()).position(DEFAULT_COFFEE_SHOP_ADDRESS).icon(startMarkerIcon));
        startMarker.setPosition(DEFAULT_COFFEE_SHOP_ADDRESS);
        BitmapDescriptor endMarkerIcon =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        endMarker = map.addMarker((new MarkerOptions()).position(DEFAULT_DELIVERY_ADDRESS).icon(endMarkerIcon));
        endMarker.setPosition(DEFAULT_DELIVERY_ADDRESS);
        getBestZoom(DEFAULT_COFFEE_SHOP_ADDRESS, DEFAULT_DELIVERY_ADDRESS);
        (new connectAsyncTask()).execute(DEFAULT_COFFEE_SHOP_ADDRESS, DEFAULT_DELIVERY_ADDRESS);
    }


    @Override
    public void onConnected(Bundle bundle) {
        LatLng addressLatLng = null;
        if (addressLatLng == null) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                addressLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(getActivity(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
            }
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(addressLatLng, 17);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void getBestZoom(LatLng start, LatLng end) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(start);
        builder.include(end);
        LatLngBounds bounds = builder.build();
        int padding = 128; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newLatLngBounds(bounds, padding);
        map.animateCamera(cameraUpdate);
    }

    private String getDirectionsUrl(LatLng start,LatLng dest){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(start.latitude));
        urlString.append(",");
        urlString.append(Double.toString(start.longitude));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(dest.latitude));
        urlString.append(",");
        urlString.append(Double.toString(dest.longitude));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    private class connectAsyncTask extends AsyncTask<LatLng, Void, String> {
        //private ProgressDialog progressDialog;
        String directionQueryURL;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(LatLng... params) {
            LatLng start = params[0];
            LatLng end = params[1];
            directionQueryURL = getDirectionsUrl(start, end);
            DirectionsJSONParser jParser = new DirectionsJSONParser();
            String resultString = jParser.getStringResultFromUrl(directionQueryURL);
            return resultString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.hide();
            if(result!=null){
                drawPath(result);
                animateMarker();
            }
        }
    }

    public void drawPath(String  result) {
        //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            directionSteps = decodePoly(encodedString);
            //Toast.makeText(getActivity(), list.toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), list.size()+"", Toast.LENGTH_SHORT).show();
            for(int z = 0; z<directionSteps.size()-1;z++){
                LatLng src= directionSteps.get(z);
                LatLng dest= directionSteps.get(z+1);
                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                        .width(8)
                        .color(Color.BLUE).geodesic(true));
            }

        }
        catch (JSONException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        catch (NullPointerException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    private void animateMarker() {
        Handler handler = new Handler();
        for(int z = 0; z <= directionSteps.size()-1;z++){
            final LatLng src= directionSteps.get(z);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMarker.setPosition(src);
                }
            }, MARKER_MOVE_INTERVAL*z);
        }
    }


}
