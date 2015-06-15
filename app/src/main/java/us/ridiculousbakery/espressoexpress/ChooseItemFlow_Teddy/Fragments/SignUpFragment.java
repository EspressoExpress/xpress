package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters.MenuAdapter;
import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/12/15.
 */
public class SignUpFragment extends Fragment {

    public interface SignUpFragmentListener {
        void successfulSignup();
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
        ParseUser user = new ParseUser();
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
                    listener.successfulSignup();
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
