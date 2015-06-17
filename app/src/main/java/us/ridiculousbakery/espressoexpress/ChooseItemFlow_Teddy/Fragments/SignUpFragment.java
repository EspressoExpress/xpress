package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/12/15.
 */
public class SignUpFragment extends Fragment {

    public interface SignUpFragmentListener {
        void successfulSignup(ParseUser user);
        void errorSigningUp(String error);
    }

    private EditText etEmail;
    private EditText etPassword;
    private EditText etUsername;
    private SignUpFragmentListener listener;

    //================================================================================
    // Public API
    //================================================================================


    public void setListener(SignUpFragmentListener listener) {
        this.listener = listener;
    }

    public void signup() {
        final ParseUser user = new ParseUser();
        user.setEmail(etEmail.getText().toString());
        user.setUsername(etEmail.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.put("displayName", etUsername.getText().toString());
        // Show spinner UI
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("DEBUG", "LOGGEDIN");
                    listener.successfulSignup(user);
                    //showInitialActivity();
                } else {
                    listener.errorSigningUp(e.getLocalizedMessage());
                    //showErrorToast(e.getLocalizedMessage());
                }
                //btnAuthenticate.setProgress(0);
            }
        });
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        etUsername = (EditText) v.findViewById(R.id.etUsername);
        return v;
    }

}
