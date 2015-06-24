package us.ridiculousbakery.espressoexpress.InProgress.Delivering;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import us.ridiculousbakery.espressoexpress.R;

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
        llAccepted.setVisibility(iAccepted);
        llPickedUp.setVisibility(iPickedUp);
        llDelivered.setVisibility(iDelivered);
        ((Button) v.findViewById(R.id.btnAccepted)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ((Button) v.findViewById(R.id.btnPickedUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ((Button) v.findViewById(R.id.btnDelivered)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

}
