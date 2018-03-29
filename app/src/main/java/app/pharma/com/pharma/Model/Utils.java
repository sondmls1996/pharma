package app.pharma.com.pharma.Model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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



    private static final float BLUR_RADIUS = 25f;


    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth());
        int height = Math.round(image.getHeight());

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
    public static boolean checkNetwork(Context context) {
        try {
            NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();

            if (info == null || !info.isConnected()) {
                return false;
            }
            if (info.isRoaming()) {
                return false;
            }
        } catch (Exception ex) {

        }
        return true;
    }

    public static void loadImagePicasso(String link, ImageView v){
        Picasso.with(Common.context).load(link).placeholder(R.drawable.noimage).into(v);
    }

    public static void loadImagePicasso(int res, ImageView v){
        Picasso.with(Common.context).load(res).placeholder(R.drawable.noimage).into(v);
    }

    public static void loadTransimagePicasso(String link, ImageView v){
        Picasso.with(Common.context).load(link).placeholder(R.drawable.noimage).transform(new TransImage()).into(v);
    }

    public static void loadTransimagePicasso(int res, ImageView v){
        Picasso.with(Common.context).load(res).placeholder(R.drawable.noimage).transform(new TransImage()).into(v);
    }

    public static void dialogNotif(String mess){

            Dialog dialog = new Dialog(Common.context);
            Window view=dialog.getWindow();
            view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window
            view.setBackgroundDrawableResource(R.drawable.border_white);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_notif);
            dialog.setCanceledOnTouchOutside(true);
        TextView tvMess = (TextView)dialog.findViewById(R.id.tv_notif);
        TextView close = (TextView)dialog.findViewById(R.id.tv_close);
        tvMess.setText(mess);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
            dialog.show();
    }

    public static Drawable setProfileDrawable(){
        Drawable drawable = Common.context.getResources().getDrawable(R.drawable.profile);
        drawable.setBounds(10, 0, (int)(drawable.getIntrinsicWidth()*0.4),
                (int)(drawable.getIntrinsicHeight()*0.4));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 10, 10);

        return sd.getDrawable();
    }
    public static void setCompondEdt(int drawable, EditText ed){
        ed.setCompoundDrawables(setDrawableEdt(Common.context.getResources().getDrawable(drawable)),null,null,null);
    }
    public static Drawable setDrawableEdt(Drawable draw){
        Drawable drawable = draw;
        drawable.setBounds(10, 0, (int)(drawable.getIntrinsicWidth()*0.4),
                (int)(drawable.getIntrinsicHeight()*0.4));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 10, 10);

        return sd.getDrawable();
    }

    public static Drawable setPassDrawable(){
        Drawable drawable = Common.context.getResources().getDrawable(R.drawable.padlock);

        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*0.4),
                (int)(drawable.getIntrinsicHeight()*0.4));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 10, 10);


        return sd.getDrawable();
    }


}
