package us.ridiculousbakery.espressoexpress.InProgress.Delivering;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.ProfileImageHelper;
import us.ridiculousbakery.espressoexpress.DisplayHelper;
import us.ridiculousbakery.espressoexpress.InProgress.RateExperienceDialogFragment;
import us.ridiculousbakery.espressoexpress.Model.PickupPhase;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective.ListPickerActivity;

/**
 * Created by teddywyly on 6/15/15.
 */
public class DeliveringHeaderFragment extends Fragment implements RateExperienceDialogFragment.RateExperienceDialogFragmentListener {

    //================================================================================
    // PublicAPI
    //================================================================================

    public static DeliveringHeaderFragment newInstance(String email, String displayName) {
        DeliveringHeaderFragment fragment = new DeliveringHeaderFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("displayName", displayName);
        fragment.setArguments(args);
        return fragment;
    }

    //================================================================================
    // InstanceVariables
    //================================================================================

    private Handler handler = new Handler();
    private long targetTimestamp;
    private ArrayList<PickupPhase> phases;
    private int currentPhaseIndex;

    private TextView tvStatus;
    private Button btnCompleted;

    private String displayName;
    private String email;

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delivering_header, container, false);

        email = getArguments().getString("email", "");
        displayName = getArguments().getString("displayName", "");

        btnCompleted = (Button) v.findViewById(R.id.btnCompleted);
        ImageView ivReceiving = (ImageView) v.findViewById(R.id.ivReceiving);
        tvStatus = (TextView) v.findViewById(R.id.tvStatus);

        setupPickupPhases();

        btnCompleted.setText("Picked Up");
        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextPhaseOfPickup();
            }
        });

        targetTimestamp = (new Date().getTime() + 150000)/1000;

        if (email != null) {
            String gravitarString = DisplayHelper.getProfileUrlFromEmail(email);
            Picasso.with(getActivity()).load(gravitarString).fit().transform(ProfileImageHelper.circleTransformation(80)).into(ivReceiving);
//            Picasso.with(getActivity()).load(url).fit().transform(ProfileImageHelper.roundTransformation()).into(ivReceiving);
        }
        handler.postDelayed(runnable, 0);

        return v;
    }

    private void setupPickupPhases() {
        phases = new ArrayList<>();
        PickupPhase first = new PickupPhase(150, "pickup coffee for " + displayName, "Completed Pickup");
        phases.add(first);
        PickupPhase second = new PickupPhase(150, "deliver Coffee to " + displayName, "Completed Delivery");
        phases.add(second);
    }

    private void startNextPhaseOfPickup() {
        currentPhaseIndex++;
        if (currentPhaseIndex < phases.size()) {
            PickupPhase next = phases.get(currentPhaseIndex);
            targetTimestamp = currentTime() + next.getTargetTime();
            btnCompleted.setText(next.getActionTask());
            tvStatus.setText(currentText(next));
        } else {
            presentRatingDialog();
        }
    }

    private String currentText(PickupPhase phase) {
        return "You have " + ((targetTimestamp-currentTime())) + " seconds" +  " to " + phase.getTask();
    }

    private long currentTime() {
        return (new Date().getTime()/1000);
    }

    private void updateStatusText() {
        if (displayName != null) {
            if (currentPhaseIndex < phases.size()) {
                PickupPhase phase = phases.get(currentPhaseIndex);
                tvStatus.setText(currentText(phase));
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateStatusText();
            handler.postDelayed(this, 1000);
        }
    };

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
