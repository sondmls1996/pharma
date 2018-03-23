package app.pharma.com.pharma.Model;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by Vi on 11/4/2016.
 */
public class TransImage implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {

        return  Utils.getCircularBitmapWithWhiteBorder(source,3);
    }

    @Override
    public String key() {
        return "circle";
    }
}