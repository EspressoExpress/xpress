package us.ridiculousbakery.espressoexpress;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Message;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;

/**
 * Created by teddywyly on 6/12/15.
 */
public class EspressoApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "Qdu88JcFCg97IGfVYvIXDCjZnMhocduPeRzDq3rN";
    public static final String YOUR_CLIENT_KEY = "rTBgE3Js7XPDCcgZ2CFWx2mGo5XGF93uLGpNCtvy";
    private ArrayList<Store> stores;
    @Override
    public void onCreate() {
        super.onCreate();
        //doesn't work
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("open-sans/OpenSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );*/
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Order.class);
        ParseObject.registerSubclass(Store.class);


//        // Test Parse
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
    }

    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext((Context) CalligraphyContextWrapper.wrap(base));
    }*/
}
