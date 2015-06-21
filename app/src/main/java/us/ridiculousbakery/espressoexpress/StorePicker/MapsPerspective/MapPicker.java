package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import us.ridiculousbakery.espressoexpress.Checkout.ParseQueryHelper;
import us.ridiculousbakery.espressoexpress.InProgress.Delivering.DeliveringActivity;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.PagerFragment;

public class MapPicker implements         GoogleMap.OnMarkerClickListener
{
    private final AppCompatActivity mCtx;
    GoogleMap map;

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    List<Store> stores;

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
        setMarked_stores(MarkedStore.decorateList(getStores()));

    }

    ArrayList<MarkedStore> marked_stores;

    public ArrayList<MarkedStore> getMarked_stores() {
        return marked_stores;
    }

    public void setMarked_stores(ArrayList<MarkedStore> marked_stores) {
        this.marked_stores = marked_stores;
    }

    HashMap<Marker, Store> marker2Store = new HashMap<Marker, Store>();

    public HashMap<Marker, Store> getMarker2Store() {
        return marker2Store;
    }

    HashMap<Marker, Order> marker2Order = new HashMap<Marker, Order>();

    public HashMap<Marker, Order> getMarker2Order() {
        return marker2Order;
    }

    public MapPicker(AppCompatActivity a) {
        mCtx=a;
    }

    Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public void populateMap() {
        for (MarkedStore store : marked_stores) {
            Bitmap src = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    mCtx.getResources(),
                    mCtx.getResources()
                            .obtainTypedArray(R.array.store_logos)
                            .getResourceId(store.getLogo(), R.drawable.philz_twit_logo)), 120, 120, true);


            store.marker = map.addMarker(
                    new MarkerOptions()
                            .position(store.getLatLng())
                            .title(store.getName())
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                    getRoundedCornerBitmap(src)
                            ))
            );
            marker2Store.put(store.marker, store.store);
            Log.i("ZZZZZZ", "populating " + store.getMarkedOrders().size() + " orders");
            for (MarkedOrder order : store.getMarkedOrders()) {
                order.marker = map.addMarker(
                        new MarkerOptions()
                                .position(order.getLatLng())
                                .title(order.getName())
                                .alpha(0.5f)
                                .visible(false)
                                .icon(
                                        BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
                                )
                );
                marker2Order.put(order.marker, order.order);
            }
        }

    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Store store = getMarker2Store().get(marker);
        Order order = getMarker2Order().get(marker);
        if (order != null) showOrderAcceptDialog(order);
        if (store != null) {
            PagerFragment.get_instance().setPageIndex(getStores().indexOf(store));
        }
        return false;
    }
    public void animateToStore(boolean animate, int index) {
        animateToStore(animate, stores.get(index));
    }

    void animateToStore(boolean animate, Store store) {
        LatLng latLng = store.getLatLng();
        boolean visible;
        for (MarkedStore s : marked_stores) {
            if (s.store == store) {
                s.marker.setAlpha(1);
                Log.i("ZZZZZZ", "marking  " + s.getMarkedOrders().size() + " visible orders");
                for (MarkedOrder o : s.getMarkedOrders()) {
                    Log.i("ZZZZZZ", "marker: " + o.getLatLng().toString() + " order" + " " + o.getName() + " " + o.order.getObjectId());
                    o.marker.setVisible(true);
                }
            } else {
                s.marker.setAlpha(0.4f);
                for (MarkedOrder o : s.getMarkedOrders()) {
                    o.marker.setVisible(false);
                }
            }

        }
        moveCamera(animate, latLng);
    }

    void moveCamera(boolean animate, LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        if (animate == true) map.animateCamera(cameraUpdate);
        else map.moveCamera(cameraUpdate);
    }

    public void showOrderAcceptDialog(final Order order) {
        View messageView = LayoutInflater.from(mCtx).
                inflate(R.layout.order_accept_dialog, null);
        TextView tvUserName = (TextView) messageView.findViewById(R.id.tvOrderUserName);
        tvUserName.setText(order.getName());
        TextView tvTOS = (TextView) messageView.findViewById(R.id.tvTOS);
        RoundedImageView ivLogo = (RoundedImageView) messageView.findViewById(R.id.ivLogo);
        ivLogo.setImageBitmap(Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                        mCtx.getResources(),
                        mCtx.getResources()
                                .obtainTypedArray(R.array.store_logos)
                                .getResourceId(order.getStore().getLogo(), R.drawable.philz_twit_logo)), 100, 100, true));
        tvTOS.setText(mCtx.getResources().getString(R.string.confirmation_dialog_message, order.getName(), order.getStore().getName()));
        // Create alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
        // set message_item.xml to AlertDialog builder
        alertDialogBuilder.setView(messageView);

        // Create alert dialog88888
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // Configure dialog button (OK)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "I got it!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // update this order on Parse
                        String parseOrderID = null;
                        try {
                            parseOrderID = ParseQueryHelper.updateSubmittedOrdertoAccepted(order);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.i("ZZZZZZZ", "order for " + order.getName() + " accepted");
                        Intent i = new Intent(mCtx, DeliveringActivity.class);
                        i.putExtra("parseOrderID", parseOrderID);
                        mCtx.startActivity(i);
                        // Define color of marker icon

                    }
                });


        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("ZZZZZZZ", "order for " + order.getName() + " declined");
                        dialog.cancel();
                    }
                });
        // Display the dialog
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        alertDialog.show();

    }
}