package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Titanic;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.TitanicTextView;
import us.ridiculousbakery.espressoexpress.R;

public class TutorialActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        TitanicTextView tvTitle = (TitanicTextView) findViewById(R.id.tvTitle);
        Titanic titanic = new Titanic();
        titanic.start(tvTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
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

    public void OnStartPressed(View view) {
        Intent i = new Intent(TutorialActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
