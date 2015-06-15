package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dd.processbutton.iml.ActionProcessButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments.LoginFragment;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments.SignUpFragment;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective.ListPickerActivity;

public class LoginActivity extends ActionBarActivity {

    private SignUpFragment signUpFragment;
    private LoginFragment loginFragment;
    private boolean isShowingLogin;

    private Button btnSwitchLogin;
    private ActionProcessButton btnAuthenticate;

    private ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSwitchLogin = (Button) findViewById(R.id.btnSwithLogin);
        btnAuthenticate = (ActionProcessButton) findViewById(R.id.btnAuthenticate);
        pbLogin = (ProgressBar) findViewById(R.id.pbLogin);

        btnAuthenticate.setMode(ActionProcessButton.Mode.ENDLESS);

        if (savedInstanceState == null) {
            signUpFragment = new SignUpFragment();
            loginFragment = new LoginFragment();
        }

        isShowingLogin = false;

        btnSwitchLogin.setText("Switch to Login");
        btnAuthenticate.setText("Authenticate");

        pbLogin.setVisibility(ProgressBar.INVISIBLE);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, signUpFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSwitchLogin(View view) {

        if (isShowingLogin) {
            showSignUp();
            btnSwitchLogin.setText("Switch to Login");
        } else {
            showLogin();
            btnSwitchLogin.setText("Switch to Sign Up");
        }

        isShowingLogin = !isShowingLogin;

    }

    private void showLogin() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        if (loginFragment.isAdded()) { // if the fragment is already in container
            ft.show(loginFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.flContainer, loginFragment, "Login");
        }
        if (signUpFragment.isAdded()) { ft.hide(signUpFragment); }

        ft.commit();
    }

    private void showSignUp() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        if (signUpFragment.isAdded()) { // if the fragment is already in container
            ft.show(signUpFragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.flContainer, signUpFragment, "Singup");
        }
        if (loginFragment.isAdded()) { ft.hide(loginFragment); }

        ft.commit();
    }

    public void onAuthenticate(View view) {

        // Authenticate User


        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        EditText etUsername = (EditText) findViewById(R.id.etUsername);

        btnAuthenticate.setProgress(1);


        if (isShowingLogin) {

            // Show spinner UI
            pbLogin.setVisibility(ProgressBar.VISIBLE);
            ParseUser.logInInBackground(etEmail.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    pbLogin.setVisibility(ProgressBar.INVISIBLE);
                    if (parseUser != null) {
                        Log.d("DEBUG", "LOGGEDIN");
                        showInitialActivity();
                    } else {
                        Log.d("DEBUG", e.toString());
                    }
                    btnAuthenticate.setProgress(0);
                }
            });
        } else {
            ParseUser user = new ParseUser();
            user.setEmail(etEmail.getText().toString());
            user.setUsername(etEmail.getText().toString());
            user.setPassword(etPassword.getText().toString());
            user.put("displayName", etUsername.getText().toString());
            // Show spinner UI
            pbLogin.setVisibility(ProgressBar.VISIBLE);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    pbLogin.setVisibility(ProgressBar.INVISIBLE);
                    if (e == null) {
                        Log.d("DEBUG", "LOGGEDIN");
                        showInitialActivity();
                    } else {
                        Log.d("DEBUG", e.toString());
                    }
                    btnAuthenticate.setProgress(0);
                }
            });
        }
    }

    private void showInitialActivity() {
        Intent i = new Intent(LoginActivity.this, ListPickerActivity.class);
        startActivity(i);
    }
}
