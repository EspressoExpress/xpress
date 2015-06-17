package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.parse.ParseUser;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.TutorialActivity;
import us.ridiculousbakery.espressoexpress.InProgress.Delivering.DeliveringActivity;
import us.ridiculousbakery.espressoexpress.InProgress.Receiving.ReceivingActivity;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective.ListPickerActivity;

public class EntryActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
            finish();
        } else {

            Order order = Order.getAcceptedParticipating(currentUser.getObjectId());
            if (order == null) {
                startActivity(new Intent(getApplicationContext(), ListPickerActivity.class));
                finish();
            } else {
                Log.i("ZZZZZZZ", "order " + order.getObjectId() + "found: r:" + order.getReceiverId() + " d:" + order.getDelivererId());
                if (order.matchesDelivererId(currentUser.getObjectId())) {
                    Log.e("ZZZZZZ", "User is in the middle of delivering" + order.toString());
                    Intent i = new Intent(getApplicationContext(), DeliveringActivity.class);
                    i.putExtra("parseOrderID", order.getObjectId());
                    startActivity(i);
                    finish();
                } else if (order.matchesReceiverId((currentUser.getObjectId()))) {
                    Log.e("ZZZZZZ", "User is in the middle of receiving" + order.toString());
                    Intent i = new Intent(getApplicationContext(), ReceivingActivity.class);
                    i.putExtra("parseOrderID", order.getObjectId());
                    startActivity(i);
                    finish();
                }else Log.e("ZZZZZZ", "found participating order, but didn't match??!  " + order.toString());
            }
        }
    }


}





