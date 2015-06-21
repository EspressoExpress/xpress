package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments.MenuFragment;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;


public class MenuActivity extends AppCompatActivity {

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (savedInstanceState == null) {
            // Get Menu


            MenuFragment fragmentMenu = MenuFragment.newInstance(getIntent().getStringExtra("storeId"));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentMenu);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) finish();
//            Intent i = new Intent(this, CartActivity.class);
//            startActivity(i);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
