package app.pharma.com.pharma.Model;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by Vi on 3/23/2018.
 */

public class BlurImagePicasso implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {

        source = Utils.blur(Common.context,source);

        return source;
    }

    @Override
    public String key() {
        return "blur";
    }
}
