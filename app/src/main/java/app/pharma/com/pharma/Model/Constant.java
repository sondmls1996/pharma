package app.pharma.com.pharma.Model;

import android.content.res.Resources;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Vi on 3/19/2018.
 */

public class Constant {
    public static String SCROLL_LV = "scroll_lv";
    public static NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public static  int ACTION_DOWN = 1;
    public static int ACTION_UP = 2;
    public static Resources resources = Common.context.getResources();

    public static String LIST_CATALO_PILL = "product";
    public static String LIST_CATALO_PILL_INTRO = "ingredient";
    public static String LIST_CATALO_SICK = "disease";
    public static String LIST_CATALO_DR = "specialist";

    public static String USER_SHARE = "user_share";

}
