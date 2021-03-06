package us.ridiculousbakery.espressoexpress.InProgress.Fragments;

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
    private LatLng startLatLng;
    private LatLng endLatLng;
    private Marker startMarker;
    private Marker endMarker;
    private List<LatLng> directionSteps;
    private static DeliveryMapFragment _instance;

    public DeliveryMapFragment() {

    }

    public static DeliveryMapFragment instance() {
        if (_instance == null)
            _instance = new DeliveryMapFragment();
        return _instance;
    }

    public static DeliveryMapFragment newInstance(LatLng startAddrLatLng, LatLng endAddrLatLng) {
            _instance = new DeliveryMapFragment();
            Bundle args = new Bundle();
            args.putParcelable("start_address", startAddrLatLng);
            args.putParcelable("end_address", endAddrLatLng);
            _instance.setArguments(args);
        return _instance;
    }


    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapFragment = new SupportMapFragment();
        Bundle args = getArguments();
        if (args != null) {
            startLatLng = (LatLng) args.getParcelable("start_address");
            endLatLng = (LatLng) args.getParcelable("end_address");
        } else {
            startLatLng = DEFAULT_COFFEE_SHOP_ADDRESS;
            endLatLng = DEFAULT_DELIVERY_ADDRESS;
        }

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
        return v;
    }

    public void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);
            //showStartIcon();
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

    public void getMapReady() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                loadMap(map);
            }
        });
    }

    public void showStartIcon() {
        BitmapDescriptor startMarkerIcon =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        startMarker = map.addMarker((new MarkerOptions()).position(DEFAULT_COFFEE_SHOP_ADDRESS).icon(startMarkerIcon));
        startMarker.setPosition(DEFAULT_COFFEE_SHOP_ADDRESS);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_COFFEE_SHOP_ADDRESS, 17);
        map.animateCamera(cameraUpdate);
    }

    private void mapAnimationStart() {
        //Picasso.with(getActivity()).load(DisplayHelper.getProfileUrlFromEmail("wylynout@gmail.com").fit().transform(ProfileImageHelper.circleTransformation(67)).into(profileView);

        BitmapDescriptor startMarkerIcon =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        startMarker = map.addMarker((new MarkerOptions()).position(startLatLng).icon(startMarkerIcon));
        startMarker.setPosition(startLatLng);
        BitmapDescriptor endMarkerIcon =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        endMarker = map.addMarker((new MarkerOptions()).position(endLatLng).icon(endMarkerIcon));
        endMarker.setPosition(endLatLng);
        getBestZoom(startLatLng, endLatLng);
        (new connectAsyncTask()).execute(startLatLng, endLatLng);
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
        /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(start);
        builder.include(end);
        LatLngBounds bounds = builder.build();*/
        int padding = 16; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newLatLngBounds(createBoundsWithMinDiagonal(start, end), padding);
        map.animateCamera(cameraUpdate);
    }

    public LatLngBounds createBoundsWithMinDiagonal(LatLng start, LatLng end) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //builder.include(firstMarker.getPosition());
        //builder.include(secondMarker.getPosition());
        builder.include(start);
        builder.include(end);

        LatLngBounds tmpBounds = builder.build();
        /** Add 2 points 1000m northEast and southWest of the center.
         * They increase the bounds only, if they are not already larger
         * than this.
         * 1000m on the diagonal translates into about 709m to each direction. */
        LatLng center = tmpBounds.getCenter();
        LatLng northEast = move(center, 209, 209);
        LatLng southWest = move(center, -209, -209);
        builder.include(southWest);
        builder.include(northEast);
        return builder.build();
    }

    private static final double EARTHRADIUS = 6366198;
    /**
     * Create a new LatLng which lies toNorth meters north and toEast meters
     * east of startLL
     */
    private static LatLng move(LatLng startLL, double toNorth, double toEast) {
        double lonDiff = meterToLongitude(toEast, startLL.latitude);
        double latDiff = meterToLatitude(toNorth);
        return new LatLng(startLL.latitude + latDiff, startLL.longitude
                + lonDiff);
    }

    private static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * EARTHRADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }

    private static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / EARTHRADIUS;
        return Math.toDegrees(rad);
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
            /*for(int z = 0; z<directionSteps.size()-1;z++){
                LatLng src= directionSteps.get(z);
                LatLng dest= directionSteps.get(z+1);
                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                        .width(8)
                        .color(Color.BLUE).geodesic(true));
            }*/

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
                    getBestZoom(startMarker.getPosition(), endMarker.getPosition());
                }
            }, MARKER_MOVE_INTERVAL*z);


        }
    }


}
