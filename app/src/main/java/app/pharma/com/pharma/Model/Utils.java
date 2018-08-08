package app.pharma.com.pharma.Model;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
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
    public static String phoneParent = "^(01[2689]|02[0-9]|09|08)[0-9]{8}";
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
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        return date;
    }
    private static final float BLUR_RADIUS = 25f;
    public static void askForPermission(Activity activity, String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Common.context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(Common.context, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
    public static void GetServer(Context ct,String link,Response.Listener<String> listener){
        GetCL get = new GetCL(link,listener);
        RequestQueue que = Volley.newRequestQueue(ct);
        que.add(get);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) Common.context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void PostServer(Context ct, String link, Map<String, String> map,
                                  Response.Listener<String> listener){

            PostCL post = new PostCL(link,map,listener);

            RequestQueue que = Volley.newRequestQueue(ct);
            que.add(post);


    }

    public static boolean isKeyboardShow(View activityRootView,Context ct){
        final boolean[] isShow = {false};
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(ct, 200)) {
                    isShow[0] = true;

                }else{
                    isShow[0] = false;
                }
            }
        });
        return isShow[0];
    };
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
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
        Picasso.get().load(link).placeholder(R.drawable.no_image_small).into(v);
    }

    public static void loadImagePicasso(int res, ImageView v){
        Picasso.get().load(res).placeholder(R.drawable.no_image_small).into(v);
    }

    public static void loadTransimagePicasso(String link, ImageView v){
        Picasso.get().load(link).placeholder(R.drawable.no_image_small).transform(new TransImage()).into(v);
    }

    public static void hideKeyboard(Activity ct){

            View view = ct.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)ct.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
    }

    public static boolean isPhoneAccep(String phone){
        if(phone.length()<10){
            return false;
        }else{
            String phoneSplit = phone.substring(0,2);
            String phoneFirst = phone.substring(0,1);
            Log.d("SPLIT_STR",phoneSplit);
            if(phone.length()>11){
                return false;
            }
            if(!phone.matches(phoneParent)){
                return false;
            }

            return true;
//            if(phoneFirst.equals("0")){
//
//                if(phoneSplit.equals("01")){
//                    if(phone.length()!=11){
//                        return  false;
//                    }else{
//                        return true;
//                    }
//                } if(phoneSplit.equals("09")){
//                    if(phone.length()!=10){
//                        return  false;
//                    }else{
//                        return true;
//                    }
//                }
//                return true;
//            }else{
//                 if(phoneSplit.equals("84")){
//                    if(phone.length()<10){
//                        return  false;
//                    }else{
//                        return true;
//                    }
//                }
//            }

        }
       // return false;
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
        Picasso.get().load(res).placeholder(R.drawable.no_image_small).transform(new TransImage()).into(v);
    }


    public static void ShowNotifString(String mess, ShowDialogNotif.OnCloseDialogNotif onClose){
        ShowDialogNotif notif = new ShowDialogNotif(mess);
        notif.setOnCloseDialogNotif(onClose);
        notif.dialogNotif();
    }

    public static class ShowDialogNotif{
        String message = "";
        public interface OnCloseDialogNotif{
            void onClose(Dialog dialog);
        }
        public ShowDialogNotif(String mess){
            this.message = mess;
        }

        public OnCloseDialogNotif onCloseDialogNotif;

        public  void dialogNotif(){

            Dialog dialog = new Dialog(Common.context);
            Window view=dialog.getWindow();
            view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window
            view.setBackgroundDrawableResource(R.drawable.border_white);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_notif);
            dialog.setCanceledOnTouchOutside(false);
            TextView tvMess = (TextView)dialog.findViewById(R.id.tv_notif);
            TextView close = (TextView)dialog.findViewById(R.id.tv_close);
            tvMess.setText(message);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCloseDialogNotif.onClose(dialog);
              //      dialog.dismiss();

                }
            });
            dialog.show();
        }

        public OnCloseDialogNotif getOnCloseDialogNotif() {
            return onCloseDialogNotif;
        }

        public void setOnCloseDialogNotif(OnCloseDialogNotif onCloseDialogNotif) {
            this.onCloseDialogNotif = onCloseDialogNotif;
        }
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

    public static void setPass(String pass){
        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.USER_PASS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("pass",pass);
        editor.commit();
    }

    public static String getPass(){

        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.USER_PASS, Context.MODE_PRIVATE);

        return preferences.getString("pass","");
    }



    public static String getUserName(){

        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.USER_NAME, Context.MODE_PRIVATE);

        return preferences.getString("username","");
    }

    public static String getTerm(){

        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.TERM, Context.MODE_PRIVATE);

        return preferences.getString("linkterm","");
    }

    public static void setTerm(String userName){
        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.TERM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("linkterm",userName);
        editor.commit();
    }
    public static void setMin(int min){
        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.MIN_VALUE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putInt("min",min);
        editor.commit();
    }
    public static void setMax(int max){
        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.MAX_VALUE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putInt("max",max);
        editor.commit();
    }
    public static int getMin(){

        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.MIN_VALUE, Context.MODE_PRIVATE);

        return preferences.getInt("min",0);
    }
    public static int getMax(){

        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.MAX_VALUE, Context.MODE_PRIVATE);

        return preferences.getInt("max",0);
    }


    public static void setAlphalAnimation(View v){

        Animation anim1 = new AlphaAnimation(0.3f,1f);
        anim1.setDuration(500);
        v.startAnimation(anim1);

    }

    public static void setUserName(String userName){
        SharedPreferences preferences =
                Common.context.getSharedPreferences(Constant.USER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("username",userName);
        editor.commit();
    }
    public static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }





}
