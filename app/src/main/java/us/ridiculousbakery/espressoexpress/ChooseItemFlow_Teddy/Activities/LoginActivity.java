package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.parse.ParseUser;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSwitchLogin = (Button) findViewById(R.id.btnSwithLogin);
        btnAuthenticate = (ActionProcessButton) findViewById(R.id.btnAuthenticate);

        btnAuthenticate.setMode(ActionProcessButton.Mode.ENDLESS);

        if (savedInstanceState == null) {
            signUpFragment = new SignUpFragment();
            signUpFragment.setListener(new SignUpFragment.SignUpFragmentListener() {
                @Override
                public void successfulSignup(ParseUser user) {
                    showInitialActivity(user);
                    btnAuthenticate.setProgress(0);
                }

                @Override
                public void errorSigningUp(String error) {
                    showErrorToast(error);
                    btnAuthenticate.setProgress(0);
                }
            });
            loginFragment = new LoginFragment();
            loginFragment.setListener(new LoginFragment.LoginFragmentListener() {
                @Override
                public void successfulLogin(ParseUser parseUser) {
                    showInitialActivity(parseUser);
                    btnAuthenticate.setProgress(0);
                }

                @Override
                public void errorLoggingin(String error) {
                    showErrorToast(error);
                    btnAuthenticate.setProgress(0);
                }
            });
        }

        isShowingLogin = false;

        btnSwitchLogin.setText("Switch to Login");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, signUpFragment);
        ft.commit();
        btnAuthenticate.setText("Sign up");

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
        } else {
            showLogin();

        }

        isShowingLogin = !isShowingLogin;

    }

    private void showLogin() {

        btnSwitchLogin.setText("Switch to Sign Up");
        btnAuthenticate.setText("Login");

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
        btnSwitchLogin.setText("Switch to Login");
        btnAuthenticate.setText("Sign up");

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
            loginFragment.login();
        } else {
            signUpFragment.signup();
        }
    }

    private void showInitialActivity( ParseUser user) {

        Intent i = new Intent(LoginActivity.this, ListPickerActivity.class);
        startActivity(i);
    }

    private void showErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
