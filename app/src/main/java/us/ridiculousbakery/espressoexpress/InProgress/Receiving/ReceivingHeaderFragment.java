package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.codec.binary.StringUtils;
import com.squareup.picasso.Picasso;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.ProfileImageHelper;
import us.ridiculousbakery.espressoexpress.DisplayHelper;
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

    public static ReceivingHeaderFragment newInstance(String email, String displayName) {
        ReceivingHeaderFragment fragment = new ReceivingHeaderFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("displayName", displayName);
        fragment.setArguments(args);
        return fragment;
    }

    //================================================================================
    // FAKE PUSH NOTIFICATION
    //================================================================================

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            animateProgressBar(1.0f);
            presentRatingDialog();
        }
    };

    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            coffeePickedUpPushReceived();
        }
    };

    //================================================================================
    // Lifecycle
    //================================================================================

    private String email;
    private String displayName;
    private TextView tvDeliverTo;
    private ProgressBar pbDeliveryProgress;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.postDelayed(runnable, 15000);
        handler.postDelayed(runnable2, 3000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_receiving_header, container, false);
        email = getArguments().getString("email", "");
        displayName = getArguments().getString("displayName", "");
        tvDeliverTo = (TextView) v.findViewById(R.id.tvDeliverTo);
        pbDeliveryProgress = (ProgressBar) v.findViewById(R.id.pbDeliveryProgress);
        TextView tvProgress = (TextView) v.findViewById(R.id.tvProgress);
        ImageView ivProfile = (ImageView) v.findViewById(R.id.ivProfile);

        tvDeliverTo.setText(displayName + " has agreed to bring you your coffee");
        tvProgress.setText("(" + "Step 1" + ")");
        String gravitarString = DisplayHelper.getProfileUrlFromEmail(email);

        Log.d("DEBUG", gravitarString + " end");
        String string = "http://2.gravatar.com/avatar/ac73bb914aef6ef42af47e0c37696e05.jpg";
        Log.d("DEBUG", string + " end");
        if (string.equals(gravitarString)) {
            Log.d("Same!", "Same!");
        }

        Picasso.with(getActivity()).load(gravitarString).fit().transform(ProfileImageHelper.circleTransformation(80)).into(ivProfile);

        return v;
    }

    private void coffeePickedUpPushReceived() {
        tvDeliverTo.setText(displayName + " is bringing you your coffee");
        animateProgressBar(0.75f);
    }

    private void animateProgressBar(float percentComplete) {
        int progress = (int)(percentComplete*pbDeliveryProgress.getMax());
        ObjectAnimator animation = ObjectAnimator.ofInt(pbDeliveryProgress, "progress", progress);
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void presentRatingDialog() {
        FragmentManager fm = getChildFragmentManager();
        RateExperienceDialogFragment editNameDialog = RateExperienceDialogFragment.newInstance(email, displayName);
        editNameDialog.setListener(this);
        editNameDialog.show(fm, "fragment_edit_name");

    }

    @Override
    public void dialogWillDismiss() {
        Intent i = new Intent(getActivity(), ListPickerActivity.class);
        startActivity(i);
    }
}
