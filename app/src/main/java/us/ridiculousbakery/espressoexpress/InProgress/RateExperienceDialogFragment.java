package us.ridiculousbakery.espressoexpress.InProgress;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.ProfileImageHelper;
import us.ridiculousbakery.espressoexpress.DisplayHelper;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/17/15.
 */
public class RateExperienceDialogFragment extends DialogFragment {

    public interface RateExperienceDialogFragmentListener {
        void dialogWillDismiss();
    }

    public void setListener(RateExperienceDialogFragmentListener listener) {
        this.listener = listener;
    }

    private RateExperienceDialogFragmentListener listener;

    private EditText mEditText;

    public RateExperienceDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static RateExperienceDialogFragment newInstance(String email, String displayName) {
        RateExperienceDialogFragment frag = new RateExperienceDialogFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("displayName", displayName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_rate, container);
        Button btnRate = (Button) view.findViewById(R.id.btnRate);
        ImageView ivRateProfile = (ImageView) view.findViewById(R.id.ivRateProfile);

        RatingBar rbRate = (RatingBar) view.findViewById(R.id.rbRate);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        String name = getArguments().getString("displayName", "Teddy");
        String email = getArguments().getString("email", "");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btnRate.setText("Rate");

        getDialog().setTitle("Rate " + name);
        tvTitle.setText("How was your experience with " + name + " ?");

        String gravitarString = DisplayHelper.getProfileUrlFromEmail(email);
        Picasso.with(getActivity()).load(gravitarString).fit().transform(ProfileImageHelper.circleTransformation(100)).into(ivRateProfile);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateExperienceDialogFragment.this.listener.dialogWillDismiss();
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        this.listener.dialogWillDismiss();
    }
}
