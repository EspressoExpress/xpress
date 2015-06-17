package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

/**
 * Created by teddywyly on 5/22/15.
 */
public class ProfileImageHelper {

    public static Transformation roundTransformation() {
        return new RoundedTransformationBuilder()
                .cornerRadiusDp(3)
                .oval(false)
                .build();
    }

    public static Transformation circleTransformation(float width) {
        return new RoundedTransformationBuilder()
                .cornerRadiusDp(width/2)
                .oval(false)
                .build();
    }
}
