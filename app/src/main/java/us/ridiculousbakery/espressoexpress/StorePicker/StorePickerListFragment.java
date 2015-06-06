package us.ridiculousbakery.espressoexpress.StorePicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class StorePickerListFragment extends Fragment {
    public StorePickerListFragment() {
    }

    public static StorePickerListFragment fromBundle(Bundle bundle) {
        StorePickerListFragment lf = new StorePickerListFragment();
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
        View v = inflater.inflate(R.layout.fragment_stores_list, container, false);

        return v;
    }
}
