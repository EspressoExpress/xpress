package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.DisplayHelper;
import us.ridiculousbakery.espressoexpress.InProgress.RateExperienceDialogFragment;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective.ListPickerActivity;

/**
 * Created by teddywyly on 6/15/15.
 */
public class ReceivingHeaderFragment extends Fragment implements RateExperienceDialogFragment.RateExperienceDialogFragmentListener {

    //================================================================================
    // PublicAPI
    //================================================================================

    public static ReceivingHeaderFragment newInstance(String profileURL, String username) {
        ReceivingHeaderFragment fragment = new ReceivingHeaderFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("profileURL", profileURL);
        fragment.setArguments(args);
        return fragment;
    }

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_receiving_header, container, false);
        String url = getArguments().getString("profileURL", "");
        String username = getArguments().getString("username", "");
        TextView tvDeliverTo = (TextView) v.findViewById(R.id.tvDeliverTo);
        TextView tvProgress = (TextView) v.findViewById(R.id.tvProgress);
        ImageView ivProfile = (ImageView) v.findViewById(R.id.ivProfile);

        tvDeliverTo.setText(username + " is bringing you your coffee");
        tvProgress.setText("Step 1");
        Picasso.with(getActivity()).load(DisplayHelper.getProfileUrl(url)).into(ivProfile);


        return v;
    }

    private void presentRatingDialog() {
        FragmentManager fm = getChildFragmentManager();
        RateExperienceDialogFragment editNameDialog = RateExperienceDialogFragment.newInstance("Teddy", "");
        editNameDialog.setListener(this);
        editNameDialog.show(fm, "fragment_edit_name");

    }

    @Override
    public void dialogWillDismiss() {
        Intent i = new Intent(getActivity(), ListPickerActivity.class);
        startActivity(i);
    }
}
