package us.ridiculousbakery.espressoexpress.StorePicker;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/21/15.
 */
public class Xtoggle extends FrameLayout implements Checkable {
    private View btn;
    private TextView knob;
    private boolean checked;
    private Float tY;
    private Float tX;

    public Xtoggle(Context context) {
        super(context);
    }

    public Xtoggle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Xtoggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Xtoggle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void initialize() {
        btn = (View) findViewById(R.id.track);
        knob = (TextView) findViewById(R.id.tvKnob);

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(wrappedListener(l));
    }

    private OnClickListener wrappedListener(final OnClickListener l) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ZZZZZZZZ", "here");
                toggle();
                l.onClick(v);
            }
        };
    }

//    @Override
//    public void setOnTouchListener(OnTouchListener l) {
//        super.setOnTouchListener(wrappedTouchListener(l));
//    }
//
//    private OnTouchListener wrappedTouchListener(OnTouchListener l) {
//        return new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if( event.getAction() == MotionEvent.ACTION_DOWN){
//                    tX = event.getRawX();
//                }else if(event.getAction()==MotionEvent.ACTION_MOVE){
//                }
//                return true;
//            }
//        };
//    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        if(!checked) {
            ObjectAnimator fadeAltAnim = ObjectAnimator.ofFloat(knob, View.TRANSLATION_X, 0);
            fadeAltAnim.setDuration(200);
            fadeAltAnim.start();
            knob.setText("Pay for Coffee");
        }else{
            ObjectAnimator fadeAltAnim = ObjectAnimator.ofFloat(knob, View.TRANSLATION_X, btn.getWidth() - knob.getWidth());
            fadeAltAnim.setDuration(200);
            fadeAltAnim.start();
            knob.setText("Run for Coffee");

        }

    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
