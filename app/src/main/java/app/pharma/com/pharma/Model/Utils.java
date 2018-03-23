package app.pharma.com.pharma.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/22/2018.
 */

public class Utils {

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


//    public static RelativeLayout setCiclerImage(RelativeLayout v){
//
//
//        GradientDrawable shape =  new GradientDrawable();
//        shape.setCornerRadius(20);
//        shape.setColor(Constant.resources.getColorStateList(R.color.white));
//
//        v.setBackground(shape);
//        v.setPadding(5,5,5,5);
//        return v;
//    }



}
