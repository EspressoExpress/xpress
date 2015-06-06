package us.ridiculousbakery.espressoexpress.StorePicker;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import us.ridiculousbakery.espressoexpress.R;


public class StorePickerActivity extends ActionBarActivity {

    private StorePickerMapFragment fgMapStoreFragment;
    private StorePickerListFragment fgListStoreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_picker);
        FrameLayout flContainer = (FrameLayout) findViewById(R.id.flContainer);
//        fgMapStoreFragment = new fgMapStoreFragment();
//        MapF
//        storePickerListFragment = new StorePickerListFragment();

        if (savedInstanceState == null) {
            activate_list_fragment();

        }
//                ((SupportfgMapStoreFragment) getSupportFragmentManager().findFragmentById(R.id.map));
//        if (fgMapStoreFragment != null) {
//            fgMapStoreFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap map) {
//                    loadMap(map);
//                }
//            });
//        } else {
//            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_store_pick_list) {
            activate_list_fragment();

        } else if (id == R.id.action_store_pick_map) {
            activate_map_fragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private StorePickerMapFragment getMapStoreFragment() {
        if (fgMapStoreFragment != null) return fgMapStoreFragment;
        return fgMapStoreFragment = new StorePickerMapFragment();
    }

    private StorePickerListFragment getListStoreFragment() {
        if (fgListStoreFragment != null) return fgListStoreFragment;
        return fgListStoreFragment = new StorePickerListFragment();
    }

    private void activate_map_fragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, getMapStoreFragment());
        ft.commit();
    }

    private void activate_list_fragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, getListStoreFragment());
        ft.commit();

    }

}
