package us.ridiculousbakery.espressoexpress;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by bkuo on 6/6/15.
 */
public class EspressoExpress extends Application{
    private static Context context;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        EspressoExpress.context = this;
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this).build();
    }

}
