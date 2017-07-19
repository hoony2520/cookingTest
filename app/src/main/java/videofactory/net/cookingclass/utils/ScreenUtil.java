/**
 * 
 */
package videofactory.net.cookingclass.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * 
 * @author Jau_ya
 */
public class ScreenUtil {

    public static boolean IS_SVC_AVAIABLE = Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2;

    /**
     * 
     * @param context
     * @return
     */
    public static int getWidthOfLCDSize(Context context) {
        LOG.debug(">> computeScreenRate()");

        int nOSVersion = Build.VERSION.SDK_INT;
        LOG.debug("++ nOSVersion =" + nOSVersion);

        int nWidth = 0;

        // Display display = ((WindowManager)
        // context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (IS_SVC_AVAIABLE) {

            Point outSize = new Point();
            display.getSize(outSize);

            nWidth = outSize.x;
        } else {
            nWidth = display.getWidth();
        }

        // int nHeight = outSize.y;
        // LOG.debug("++ nWidth = [%d]", nWidth);

        return nWidth;
    }

    public static int getHeightOfLCDSize(Context context) {
        LOG.debug(">> computeScreenRate()");

        int nOSVersion = Build.VERSION.SDK_INT;
        LOG.debug("++ nOSVersion =" + nOSVersion);

        int nHeight = 0;

        // Display display = ((WindowManager)
        // context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (IS_SVC_AVAIABLE) {

            Point outSize = new Point();
            display.getSize(outSize);

            nHeight = outSize.y;
        } else {
            nHeight = display.getHeight();
        }

        // int nHeight = outSize.y;
        // LOG.debug("++ nWidth = [%d]", nWidth);

        return nHeight;
    }

    // status bar (at the top of the screen on a Nexus device)
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    // navigation bar (at the bottom of the screen on a Nexus device)
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
