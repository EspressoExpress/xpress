package us.ridiculousbakery.espressoexpress.InProgress.Delivering;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.ProfileImageHelper;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/15/15.
 */
public class DeliveringHeaderFragment extends Fragment {

    //================================================================================
    // PublicAPI
    //================================================================================

    public static DeliveringHeaderFragment newInstance(String profileURL, String username) {
        DeliveringHeaderFragment fragment = new DeliveringHeaderFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("profileURL", profileURL);
        fragment.setArguments(args);
        return fragment;
    }

    //================================================================================
    // InstanceVariables
    //================================================================================

    private Handler handler = new Handler();
    private String username;
    private long targetTimestamp;

    private TextView tvStatus;
    private Button btnCompleted;

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

        btnCompleted = (Button) v.findViewById(R.id.btnCompleted);
        ImageView ivReceiving = (ImageView) v.findViewById(R.id.ivReceiving);
        tvStatus = (TextView) v.findViewById(R.id.tvStatus);

        btnCompleted.setText("Picked Up");

        String url = getArguments().getString("profileURL", "");
        username = getArguments().getString("username", "");

        targetTimestamp = (new Date().getTime() + 150000)/1000;

        if (url != null) {
            Picasso.with(getActivity()).load(url).fit().transform(ProfileImageHelper.roundTransformation()).into(ivReceiving);
        }

        handler.postDelayed(runnable, 0);

        return v;
    }

    private String currentText() {

        return "You have " + ((targetTimestamp-currentTime())) + " to pickup " + "Sam's " + "coffee";
    }

    private long currentTime() {
        return (new Date().getTime()/1000);
    }

    private void updateStatusText() {
        if (username != null) {
            tvStatus.setText(currentText());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateStatusText();
            handler.postDelayed(this, 1000);
        }
    };
}
