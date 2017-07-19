package videofactory.net.cookingclass.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

public class PlayerUtil {

    public static boolean hasNavigationBar(Context context) {

        try {
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

            if (!hasMenuKey && !hasBackKey) {
                // Do whatever you need to do, this device has a navigation bar
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasBottomtNavigationBar(Activity activity){

        boolean hasMenuKey = hasNavigationBar(activity);
        if (!hasMenuKey && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

            if (dm.heightPixels%10!=0) {
                //navigation bar is on bottom
                return true;
            } else {
                //navigation bar is on right side
                return false;
            }

        }
        return false;
    }

    public static boolean hasRightNavigationBar(Activity activity){

        boolean hasMenuKey = hasNavigationBar(activity);
        if (!hasMenuKey && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

            if (dm.heightPixels%10!=0) {
                //navigation bar is on bottom
                return false;
            } else {
                //navigation bar is on right side
                return true;
            }

        }
        return false;
    }

    public static boolean hasNavBar (Resources resources)
    {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0)
            return resources.getBoolean(id);
        else
            return false;
    }

    public static int getNavigationBarHeight (Resources resources)
    {
        if (!hasNavBar(resources))
            return 0;

        int orientation = resources.getConfiguration().orientation;

        //Only phone between 0-599 has navigationbar can move
//        boolean isSmartphone = resources.getConfiguration().smallestScreenWidthDp < 600;
//        if (isSmartphone && Configuration.ORIENTATION_LANDSCAPE == orientation)
//            return 0;

        int id = resources
                .getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if (id > 0)
            return resources.getDimensionPixelSize(id);

        return 0;
    }

    public static int getNavigationBarWidth (Resources resources)
    {
        if (!hasNavBar(resources))
            return 0;

        int orientation = resources.getConfiguration().orientation;

        //Only phone between 0-599 has navigationbar can move
//        boolean isSmartphone = resources.getConfiguration().smallestScreenWidthDp < 600;
//        if (isSmartphone && Configuration.ORIENTATION_LANDSCAPE == orientation)
//            return 0;

        int id = resources.getIdentifier( "navigation_bar_width" , "dimen", "android");
        if (id > 0)
            return resources.getDimensionPixelSize(id);

        return 0;
    }

    public static boolean isSystemBarOnBottom(Context ctxt) {
        Resources res=ctxt.getResources();
        Configuration cfg=res.getConfiguration();
        DisplayMetrics dm=res.getDisplayMetrics();
//        boolean canMove=(dm.widthPixels != dm.heightPixels &&
//                cfg.smallestScreenWidthDp < 600);

        boolean canMove=(dm.widthPixels != dm.heightPixels );

        return(!canMove || dm.widthPixels < dm.heightPixels);
    }


}
