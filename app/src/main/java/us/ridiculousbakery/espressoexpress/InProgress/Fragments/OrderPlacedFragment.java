package us.ridiculousbakery.espressoexpress.InProgress.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Titanic;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.TitanicTextView;
import us.ridiculousbakery.espressoexpress.InProgress.Adapters.ChatListAdapter;
import us.ridiculousbakery.espressoexpress.Model.Message;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/15/15.
 */
public class OrderPlacedFragment extends Fragment {

    private Button btnCancel;
    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_placed, container, false);

        TitanicTextView tvTitanic = (TitanicTextView) v.findViewById(R.id.tvTitanic);
        tvTitanic.setTextColor(getResources().getColor(android.R.color.white));
        Titanic titanic = new Titanic();
        titanic.start(tvTitanic);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast saying would you like to cancel this order
            }
        });

        return v;
    }
}
