package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/21/15.
 */
public class TutorialSlidePageFragment extends Fragment {

    public static TutorialSlidePageFragment newInstance(String text, int imageResource) {
        TutorialSlidePageFragment fragmentDemo = new TutorialSlidePageFragment();
        Bundle args = new Bundle();
        args.putString("text", text);
        args.putInt("imageResource", imageResource);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tutorial_slide_page, container, false);
        TextView tvInstruction = (TextView)rootView.findViewById(R.id.tvInstruction);
        ImageView ivBackground = (ImageView)rootView.findViewById(R.id.ivBackground);

        String text = getArguments().getString("text");
        int resource = getArguments().getInt("imageResource");

        tvInstruction.setText(text);
        ivBackground.setImageResource(resource);

        return rootView;
    }



}
