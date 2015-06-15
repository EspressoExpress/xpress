package us.ridiculousbakery.espressoexpress.Checkout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.devmarvel.creditcardentry.library.CreditCardForm;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/10/2015.
 */
public class CCFormFragment extends DialogFragment {

    protected CreditCardForm ccform;
    protected Button btSavePayment;

    public CCFormFragment() {

    }

    public static CCFormFragment newInstance() {
        CCFormFragment ccFormFragment = new CCFormFragment();
        ccFormFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return ccFormFragment;
    }

    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  v = inflater.inflate(R.layout.fragment_cc, container, false);
        ccform = (CreditCardForm) v.findViewById(R.id.credit_card_form);
        ccform.requestFocus();
        //still doesn't show soft keyboard
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btSavePayment = (Button) v.findViewById(R.id.btSavePayment);
        setupListeners();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null) {
            return;
        }
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics  = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float dialogWidth = displayMetrics.widthPixels - 56; // specify a value here
        float dialogHeight = displayMetrics.heightPixels - 56; // specify a value here
        getDialog().getWindow().setLayout(Math.round(dialogWidth), Math.round(dialogHeight));
    }

    private void setupListeners() {
        btSavePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
