package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/7/15.
 */
public class MenuHeaderFragment extends Fragment {

    //================================================================================
    // Lifecycle
    //================================================================================

    public static MenuHeaderFragment newInstance(String title, String imageURL) {
        MenuHeaderFragment dialog = new MenuHeaderFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("imageURL", imageURL);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_header, container, false);

        String title = getArguments().getString("title");
        String imageURL = getArguments().getString("imageURL");

        Log.d("IMAGE", imageURL);

        ImageView ivHeader = (ImageView) v.findViewById(R.id.ivHeader);

        Log.d("VIEW", ivHeader.toString());

        Picasso.with(getActivity()).load(imageURL).fit().into(ivHeader);

        return v;
    }
}
