package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/12/15.
 */
public class LoginFragment extends Fragment {

    public interface LoginFragmentListener {
        void successfulLogin(ParseUser parseUser);
        void errorLoggingin(String error);
    }

    private EditText etEmail;
    private EditText etPassword;
    private LoginFragmentListener listener;

    public void setListener(LoginFragmentListener listener) {
        this.listener = listener;
    }

    //================================================================================
    // Public API
    //================================================================================

    public void login() {
        Log.d("LOGGINGINWITHPASSWORD", etPassword.getText().toString() + etEmail.getText().toString());
        ParseUser.logInInBackground(etEmail.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Log.d("DEBUG", "LOGGEDIN");
                    listener.successfulLogin(parseUser);
                    //showInitialActivity();
                } else {
                    listener.errorLoggingin(e.getLocalizedMessage());
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
        EditText etUsername = (EditText) v.findViewById(R.id.etUsername);
        etUsername.setVisibility(EditText.INVISIBLE);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        return v;
    }

}
