package app.pharma.com.pharma.Model;

import android.content.res.Resources;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Vi on 3/19/2018.
 */

public class Constant {
    public static String SEARCH_ACTION = "search";
    public static String CLOSE_SEARCH_ACTION = "closesearch";
    public static String inFragment = "";
    public static NumberFormat format = NumberFormat.getNumberInstance(Locale.UK);
    public static  int ACTION_DOWN = 1;
    public static String TERM = "term";
    public static int ACTION_UP = 2;
    public static String PILL_INTRO_TYPE = "introtype";
    public static Resources resources = Common.context.getResources();

    public static String LIST_CATALO_PILL = "product";
    public static String LIST_CATALO_PILL_INTRO = "ingredient";
    public static String LIST_CATALO_SICK = "disease";
    public static String LIST_CATALO_DR = "specialist";
    public static String MIN_VALUE = "min";
    public static String MAX_VALUE = "max";
    public static String USER_SHARE = "user_share";
    public static String USER_NAME = "user_name";

    public static final int PERMISSION_LOCATION = 1;
    public static final int PHONE = 2;

}
