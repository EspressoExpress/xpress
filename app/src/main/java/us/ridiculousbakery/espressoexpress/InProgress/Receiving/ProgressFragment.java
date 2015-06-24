package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/23/15.
 */
public class ProgressFragment extends Fragment {
    private static ProgressFragment _instance;
    private ProgressElement elPlaced;
    private ProgressElement elAccepted;
    private ProgressElement elPickedUp;
    private ProgressElement elDelivered;

    public static ProgressFragment instance(){
        if(_instance==null) _instance=new ProgressFragment();
        return _instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _v = inflater.inflate(R.layout.receiving_progress_fragment, null, false);
        ViewGroup v = (ViewGroup)_v;

        elPlaced = ((ProgressElement) v.findViewById(R.id.elPlaced));
        elAccepted = ((ProgressElement) v.findViewById(R.id.elAccepted));
        elPickedUp = ((ProgressElement) v.findViewById(R.id.elPickedUp));
        elDelivered = ((ProgressElement) v.findViewById(R.id.elDelivered));
        elPlaced.init();
        elAccepted.init();
        elPickedUp.init();
        elDelivered.init();
        elPlaced.setText(R.string.progress_placed);
        elAccepted.setText(R.string.progress_accepted);
        elPickedUp.setText(R.string.progress_picked_up);
        elDelivered.setText(R.string.progress_delivered);

        elPlaced.setMatchers(new String[]{Order.SUBMITTED,Order.ACCEPTED,Order.PICKED_UP,Order.DELIVERED});
        elAccepted.setMatchers(new String[]{Order.ACCEPTED,Order.PICKED_UP,Order.DELIVERED});
        elPickedUp.setMatchers(new String[]{Order.PICKED_UP,Order.DELIVERED});
        elDelivered.setMatchers(new String[]{Order.DELIVERED});

        return _v;
    }
    public void activate(String status){
        elPlaced.activate(status);
        elAccepted.activate(status);
        elPickedUp.activate(status);
        elDelivered.activate(status);
    }
}
