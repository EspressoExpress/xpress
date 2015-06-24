package us.ridiculousbakery.espressoexpress.InProgress.Delivering;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/24/15.
 */
public class DeliveryStepsFragment extends Fragment {

    private static DeliveryStepsFragment _instance;

    public static DeliveryStepsFragment instance(){
        if(_instance==null) _instance=new DeliveryStepsFragment();
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
        return v;
    }

}
