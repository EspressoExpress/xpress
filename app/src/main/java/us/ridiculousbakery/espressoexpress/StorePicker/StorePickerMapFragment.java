package us.ridiculousbakery.espressoexpress.StorePicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bkuo on 6/6/15.
 */
public class StorePickerMapFragment extends Fragment {
    public static StorePickerMapFragment fromBundle(Bundle bundle) {
        StorePickerMapFragment lf = new StorePickerMapFragment();
        lf.setArguments(bundle);
        return lf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return null;
    }

}
