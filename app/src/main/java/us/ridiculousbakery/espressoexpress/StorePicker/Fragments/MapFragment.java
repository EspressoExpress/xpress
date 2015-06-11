package us.ridiculousbakery.espressoexpress.StorePicker.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class MapFragment extends SupportMapFragment {


    private LatLng initialMapTarget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_store_picker_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setMenuVisibility(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }



}
