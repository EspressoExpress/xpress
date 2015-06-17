package us.ridiculousbakery.espressoexpress.Checkout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

import us.ridiculousbakery.espressoexpress.R;

public class ParseUploadActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_upload);
        uploadStores();
    }

    private void uploadStores() {
        ParseObject storeObj = new ParseObject("Store");
        storeObj.put("name", "Philz");
        storeObj.put("lat_offset", 0.01);
        storeObj.put("lng_offset", 0.01);
        storeObj.put("name", "Philz");
        storeObj.put("lat_offset", 0.02);
        storeObj.put("lng_offset", 0.02);
        storeObj.put("name", "Starbucks");
        storeObj.put("lat_offset", -0.02);
        storeObj.put("lng_offset", -0.02);
        storeObj.put("name", "Starbucks");
        storeObj.put("lat_offset", -0.02);
        storeObj.put("lng_offset", 0.02);
        storeObj.put("name", "BlueBottle");
        storeObj.put("lat_offset", 0.02);
        storeObj.put("lng_offset", -0.02);
        storeObj.saveInBackground();

        //menu
        ParseObject menuObj = new ParseObject("Menu");
        menuObj.put("name", "Philz_menu");
        menuObj.saveInBackground();

        //category
        /*ParseObject catObj = new ParseObject("Category");
        catObj.put("name", "Dark Roast");
        catObj.saveInBackground();
        ParseObject catObj2 = new ParseObject("Category");
        catObj2.put("name", "Decaf");
        catObj2.saveInBackground();
*/
        //save first to add to relation later
        final ParseObject catObj = new ParseObject("Category");
        catObj.put("name", "Dark Roast");
        catObj.saveInBackground();
        final ParseObject catObj2 = new ParseObject("Category");
        catObj2.put("name", "Decaf");
        catObj2.saveInBackground();

        //retrieve and hookup relation
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Menu");
        query.getInBackground("FoWBrwJNvH", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    //Toast.makeText(CartActivity.this, (String) parseObject.get("name"), Toast.LENGTH_LONG).show();
                    ParseRelation<ParseObject> relation = parseObject.getRelation("Categories");
                    relation.add(catObj);
                    relation.add(catObj2);
                    //Toast.makeText(CartActivity.this, relation.toString(), Toast.LENGTH_LONG).show();
                    parseObject.saveInBackground();
                } else {
                    // something went wrong
                }
            }
        });

        // check if relation is saved
        /*ParseObject obj = null;
        try {
            obj = query.get("FoWBrwJNvH");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseRelation<ParseObject> relation = obj.getRelation("Categories");
        relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, com.parse.ParseException e) {
                if (e != null) {
                    // There was an error
                    // something went wrong
                    Toast.makeText(CartActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                } else {
                    // results have all the Posts the current user liked.
                    Toast.makeText(CartActivity.this, results.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });*/

        //try getting store menu from parse
        ParseQuery<ParseObject> store_query = ParseQuery.getQuery("Store");

        store_query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, com.parse.ParseException e) {
                for (ParseObject store_obj : results) {
//                    Store store = new Store((String) store_obj.get("name"));
                }
            }
        });

        //hookup existing store to existing menu
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Store");
        ParseObject store = null;
        try {
            store = query1.get("1NoCwWrzM5");
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Menu");
        ParseObject menu = null;
        try {
            menu = query2.get("FoWBrwJNvH");
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        ParseRelation<ParseObject> relation = store.getRelation("store_menu");
        relation.add(menu);
        //Toast.makeText(CartActivity.this, relation.toString(), Toast.LENGTH_LONG).show();
        store.saveInBackground();

         /*options_query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, com.parse.ParseException e) {
                for (ParseObject option_obj : results) {
                    String name = (String) option_obj.get("option_name");
                    Toast.makeText(CartActivity.this, name, Toast.LENGTH_SHORT).show();
                    ArrayList<String> option_values = (ArrayList<String>) option_obj.get("options");
                    //Toast.makeText(CartActivity.this, option_values.toString(), Toast.LENGTH_SHORT).show();
                    options.put(name, option_values);
                    Toast.makeText(CartActivity.this, options.get("Size").toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });*/
        //Toast.makeText(CartActivity.this, (options.size() > 0? "treemap not empty" : "empty"), Toast.LENGTH_LONG).show();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parse_upload, menu);
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
}
