package us.ridiculousbakery.espressoexpress.InProgress.Delivering;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import us.ridiculousbakery.espressoexpress.InProgress.Receiving.XpressReceiver;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective.ListPickerActivity;

/**
 * Created by bkuo on 6/24/15.
 */
public class DeliveryStepsFragment extends Fragment {
    private ViewGroup elAccepted;
    private ViewGroup elPickedUp;
    private ViewGroup elDelivered;
    private static DeliveryStepsFragment _instance;
    private Integer iAccepted = View.VISIBLE;
    private Integer iPickedUp = View.GONE;
    private Integer iDelivered = View.GONE;
    private LinearLayout llAccepted;
    private LinearLayout llPickedUp;
    private LinearLayout llDelivered;

    public static DeliveryStepsFragment instance() {
        if (_instance == null) _instance = new DeliveryStepsFragment();
        return _instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _v = inflater.inflate(R.layout.delivery_fragment, null, false);
        ViewGroup v = (ViewGroup) _v;
        llAccepted = (LinearLayout) v.findViewById(R.id.llAccepted);
        llPickedUp = (LinearLayout) v.findViewById(R.id.llPickedUp);
        llDelivered = (LinearLayout) v.findViewById(R.id.llDelivered);



        refreshVisibility();
        publishStatusUpdate(Order.ACCEPTED);

        ((Button) v.findViewById(R.id.btnAccepted)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishStatusUpdate(Order.PICKED_UP);
                iAccepted = View.GONE;
                iPickedUp = View.VISIBLE;
                iDelivered = View.GONE;
                refreshVisibility();


            }
        });
        ((Button) v.findViewById(R.id.btnPickedUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishStatusUpdate(Order.DELIVERED);
                iAccepted = View.GONE;
                iPickedUp = View.GONE;
                iDelivered = View.VISIBLE;
                refreshVisibility();


            }
        });
        ((Button) v.findViewById(R.id.btnDelivered)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListPickerActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return v;
    }

    private void refreshVisibility() {
        llAccepted.setVisibility(iAccepted);
        llPickedUp.setVisibility(iPickedUp);
        llDelivered.setVisibility(iDelivered);
    }

    void publishStatusUpdate(String status){
        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("action", XpressReceiver.outerAction);
            obj.putOpt("status", status);
            obj.putOpt("type", "status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ParsePush push = new ParsePush();
        ParseQuery query = ParseInstallation.getQuery();
//                push.setChannel();
        // Push the notification to Android users
        query.whereEqualTo("deviceType", "android");
        push.setQuery(query);
        push.setData(obj);
        push.sendInBackground();
    }

}
