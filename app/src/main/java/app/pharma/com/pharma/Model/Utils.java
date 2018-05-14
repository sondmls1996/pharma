package app.pharma.com.pharma.Model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import app.pharma.com.pharma.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Vi on 3/22/2018.
 */

public class Utils {
    Dialog dialogloading;
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

    public static void toast(String mess){
        Toast.makeText(Common.context,mess,Toast.LENGTH_SHORT).show();
    }

    public Utils() {
    }
    public static String convertTimestampToDate(long timestamp){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
    private static final float BLUR_RADIUS = 25f;

    public static void GetServer(Context ct,String link,Response.Listener<String> listener){
        GetCL get = new GetCL(link,listener);
        RequestQueue que = Volley.newRequestQueue(ct);
        que.add(get);
    }

    public static void PostServer(Context ct, String link, Map<String, String> map,
                                  Response.Listener<String> listener){
        PostCL post = new PostCL(link,map,listener);
        RequestQueue que = Volley.newRequestQueue(ct);
        que.add(post);
    }
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
    public static boolean isNetworkEnable(Context context) {
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

    public static boolean isGpsEnable(Context context){
        LocationManager locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);

        // get GPS status
        boolean checkGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(checkGPS){
            return true;
        }else{
            return false;
        }
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

    public static void shareLink(String link){
        if(!link.equals("")){
            Intent shareit = new Intent(Intent.ACTION_SEND);
            shareit.setType("text/plain");
            String shareBody = link;

            shareit.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            Common.context.startActivity(Intent.createChooser(shareit, "Chia sẻ qua"));
        }
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

    public void showLoading(Context context,int time,boolean show){
        if(dialogloading==null){
            dialogloading = new Dialog(context);
            dialogloading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogloading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogloading.setContentView(R.layout.loading);
            dialogloading.setCanceledOnTouchOutside(false);
            ProgressBar mProgressBar = dialogloading.findViewById(R.id.pr_loading);
            mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
                    .getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
            final Handler handler  = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (dialogloading.isShowing()) {
                        dialogloading.dismiss();
                        Utils.toast(Common.context.getResources().getString(R.string.network_err));
                    }
                }
            };
            handler.postDelayed(runnable, time);
            dialogloading.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    handler.removeCallbacks(runnable);
                    dialogloading=null;

                }
            });
        }

        if(show){
            dialogloading.show();
        }else{
            if(dialogloading.isShowing()){
                dialogloading.dismiss();
            }

        }

    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isLogin(){

        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.USER_SHARE, Context.MODE_PRIVATE);

        return preferences.getBoolean("isLogin",false);
    }

    public static void setLogin(boolean isLogin){
        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.USER_SHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putBoolean("isLogin",isLogin);
        editor.commit();
    }

    public static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }





}
