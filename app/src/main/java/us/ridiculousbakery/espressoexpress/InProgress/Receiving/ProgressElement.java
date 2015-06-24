package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/23/15.
 */
public class ProgressElement extends LinearLayout {
    private String[] matchers;
    private TextView tv;
    private ImageView iv;
    private int accentColor;

    public ProgressElement(Context context) {
        super(context);
    }

    public ProgressElement(Context context, AttributeSet attrs) {
        super(context, attrs); init();
    }

    public ProgressElement(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);  init();
    }

    public void init() {
        iv = (ImageView) findViewById(R.id.ivIcon);
        tv = (TextView) findViewById(R.id.tvStep);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressElement(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes); init();
    }

    public void setMatchers(String[] matchers) {
        this.matchers = matchers;
    }

    public void activate(String match) {
        Log.i("ZZZZZZ","activate "+ match);

        for (String b : matchers) {
            if (b.equals(match)) {
                Log.i("ZZZZZZZ", ""+b+" matches "+ match+ " so activating!");
                activate();}
        }
    }

    public void activate() {

//        Log.i("ZZZZZZ", "activate " + c);

        if(  iv==null  )    iv = (ImageView) findViewById(R.id.ivIcon);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        int c = typedValue.data;
//        int c = getResources().getColor(R.color.bakers_chocolate);
        iv.setImageTintList(ColorStateList.valueOf(c));
        tv.setTextColor(c);
        setAlpha(1);
    }

//    public void setAccentColor(Colo){
//        accentColor = c;
//    }

    public void setText(int i){
        String s  = getResources().getString(i);
        tv.setText(s);
    }
}
