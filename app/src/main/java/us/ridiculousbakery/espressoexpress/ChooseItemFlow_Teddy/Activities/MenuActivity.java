package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters.MenuAdapter;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments.MenuFragment;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;


public class MenuActivity extends AppCompatActivity {
    private MenuAdapter aMenu;
    private StoreMenu storeMenu;
    private Store store;
    private TextView tvStoreName;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.XpressTheme);
        setContentView(R.layout.activity_menu);

        final MenuFragment fragmentMenu = MenuFragment.newInstance(getIntent().getStringExtra("1NoCwWrzM5"));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, fragmentMenu);
        ft.commit();

        setupActionBar();
        tvStoreName = (TextView) findViewById(R.id.tvStoreName);
        final View header = findViewById(R.id.fgHeader);

        String storeID = "1NoCwWrzM5";

        Store.getInBackground(storeID, new GetCallback<Store>() {
            @Override
            public void done(final Store store, ParseException e) {

                if (e == null) {
                    Log.d("Success in findg store", "Success in finding stre");
                    String menuString = (String) store.get("menu");
                    JSONObject menuJSON = null;
                    try {
                        menuJSON = new JSONObject(menuString);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    storeMenu = StoreMenu.fromJSON(menuJSON);


                    //storeMenu = store.getStoreMenu();
                    aMenu = new MenuAdapter(MenuActivity.this, storeMenu);
                    fragmentMenu.setAdapter(aMenu);
                    for (int i = 0; i < aMenu.getGroupCount(); i++) {
                        fragmentMenu.expandGroup(i);
                    }

                }
            }
        });
        fragmentMenu.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final float headerHeight = header.getHeight() - getSupportActionBar().getHeight();
//                            Log.d("HEIGHT", headerHeight + "");
//                            Log.d("HEIGHT", header.getTop() + "");

                float head = header.getTop() * -1;
                float base = (float) (headerHeight / 2.5);
                float scalingFactor = 1 - (head / base);

//                            Log.d("FACTOR", scalingFactor + "");
//                            Log.d("HEAD", head + "");
//
//
                ActionBar bar = getSupportActionBar();

                if (headerHeight - head < bar.getHeight() / 6) {
                    bar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                    bar.setTitle(Html.fromHtml("<font color='#ffffff'>" + store.getName() + "</font>"));
                    bar.setTitle(store.getName());
                } else {
                    bar.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                    bar.setTitle("");
                }


                float magic = headerHeight / 2;

                if (head > magic) {
//                    menuHeaderFragment.translateTitleTextVertical(head - magic);
                }

                float alphaScale = head / headerHeight;
                if (alphaScale < 0.4) {
                    alphaScale = 0.4f;
                }
//                menuHeaderFragment.setOverlayAlpha(alphaScale);

                if (scalingFactor > 0.5f) {
//                    menuHeaderFragment.scaleTitleText(scalingFactor);
                }


            }
        });
    }

    private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_custom_up);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }
}
