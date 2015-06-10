package us.ridiculousbakery.espressoexpress.Checkout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/10/2015.
 */
public class CCFormFragment extends DialogFragment {

    public CCFormFragment() {

    }

    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  v = inflater.inflate(R.layout.fragment_cc, container, false);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
