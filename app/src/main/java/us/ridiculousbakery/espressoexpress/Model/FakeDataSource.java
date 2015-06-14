package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/6/15.
 */
public class FakeDataSource {

    public static ArrayList<Store> nearby_stores(LatLng latlng) {
        Double lat = latlng.latitude;
        Double lon = latlng.longitude;
        ArrayList<Store> list = new ArrayList<Store>();
        list.add(new Store("Starbucks", new LatLng(lat - 0.02, lon - 0.02), R.drawable.sbux_twit_logo, nearby_orders(new LatLng(lat - 0.02, lon - 0.02))));
        list.add(new Store("Starbucks", new LatLng(lat - 0.02, lon + 0.02), R.drawable.sbux_twit_logo, nearby_orders(new LatLng(lat - 0.02, lon + 0.02))));
        list.add(new Store("BlueBottle", new LatLng(lat + 0.02, lon - 0.02), R.drawable.blue_twit_logo, nearby_orders(new LatLng(lat + 0.02, lon - 0.02))));
        list.add(new Store("Philz", new LatLng(lat + 0.02, lon + 0.02), R.drawable.philz_twit_logo, nearby_orders(new LatLng(lat + 0.02, lon + 0.02))));
        list.add(new Store("Philz", new LatLng(lat + 0.01, lon + 0.01), R.drawable.philz_twit_logo, nearby_orders(new LatLng(lat + 0.01, lon + 0.01))));
        return list;
    }

    public static ArrayList<Order> nearby_orders(LatLng latlng) {
        Double lat = latlng.latitude;
        Double lon = latlng.longitude;
        ArrayList<Order> list = new ArrayList<Order>();
        list.add(new Order("Alice", new LatLng(lat - 0.008, lon - 0.008)));
        list.add(new Order("Bob", new LatLng(lat - 0.008, lon + 0.008)));
        list.add(new Order("Eve", new LatLng(lat + 0.008, lon - 0.008)));
        return list;
    }

    public static StoreMenu fakeMenu() {

        StoreMenu menu = new StoreMenu();
        TreeMap<String, ArrayList<Item>> categories;
        categories = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        List<String> cats = Arrays.asList("Hot", "Cold", "Food");
        List<String> prods = Arrays.asList("Coffee", "Frap", "Cookie");


        for (int i=0; i<4; i++) {
            ArrayList<Item> newItems = new ArrayList<Item>();
            for (int j=0; j<10; j++) {
                TreeMap<String, ArrayList<String>> options = new TreeMap<>();
                //1
                ArrayList<String> size = new ArrayList<String>();
                size.add("Small");
                size.add("Medium");
                size.add("Large");
                options.put("Size", size);
                //2
                ArrayList<String> milk = new ArrayList<String>();
                milk.add("None");
                milk.add("Splash");
                milk.add("Medium");
                milk.add("A lot");
                options.put("Milk", milk);
                //3
                ArrayList<String> sugar = new ArrayList<String>();
                sugar.add("None");
                sugar.add("Little");
                sugar.add("A lot");
                options.put("Sugar", sugar);
                int idx = new Random().nextInt(prods.size());
                String name = prods.get(idx);
                Item item = new Item(name, options);
                newItems.add(item);
            }
            int idx = new Random().nextInt(cats.size());
            String cat = cats.get(idx);
            categories.put(cat, newItems);

        }

        menu.setCategories(categories);

        return menu;
    }
}
