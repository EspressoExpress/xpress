package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.support.v4.app.Fragment;

/**
 * Created by bkuo on 6/23/15.
 */
public class MapFragment extends Fragment {
    private static MapFragment _instance;

    public static MapFragment instance() {
        if (_instance == null) _instance = new MapFragment();
        return _instance;
    }

}
