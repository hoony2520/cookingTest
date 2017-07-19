package videofactory.net.cookingclass.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;


public class DeviceInfo
{


    /**
     * 3G�� ���¸� üũ�Ѵ�.
     *
     * @param context
     * @return 3G�� ���� ����
     */
    public static boolean isMobileEnable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm.getActiveNetworkInfo()) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if( null != activeNetwork && activeNetwork.getType() ==  ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }

        //�Ǵ� ������ ���� ����� ���� �ִ�
        //boolean isMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        return false;
    }

    /**
     * Wi-Fi ���¸� üũ�Ѵ�.
     *
     * @param context
     * @return Wi-Fi ���� ����
     */
    public static boolean isWifiConnectedOrConnecting(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

//      if (null != cm.getActiveNetworkInfo()) {
//          NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//          if( null != activeNetwork && activeNetwork.getType() ==  ConnectivityManager.TYPE_WIFI) {
//              return true;
//          }
//      }
        //�Ǵ� ������ ���� ����� ���� �ִ�
        boolean isWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        return isWifi;
    }



    public static boolean isEnableNetwork(Context _context)
    {
        boolean networkWiFi = false, network3G = false, returnNetwork = false, networkWibro = false;
        NetworkInfo wifiInfo, mobileInfo, wibroInfo;

        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobileInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wibroInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

        if (wifiInfo != null)
            networkWiFi = wifiInfo.isConnectedOrConnecting();
        if (mobileInfo != null)
            network3G = mobileInfo.isConnectedOrConnecting();
        if (wibroInfo != null)
            networkWibro = wibroInfo.isConnectedOrConnecting();

        if (networkWiFi || network3G || networkWibro)
        {
            returnNetwork = true;
        }

        wifiInfo = mobileInfo = wibroInfo = null;
        connectivity = null;
        return returnNetwork;

    }

    public static boolean isWifiEnabled(Context _context)
    {
        LOG.debug(">> Network::isWifiEnabled()");
        WifiManager wifiManager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();

        return (wifiManager.isWifiEnabled() == true && wInfo.getSSID() != null);
    }

//    public static byte[] getMACAddress(Context _context)
//    {
//        LOG.debug("Network::getMACAddress()");
//        byte[] byteMacAddr = new byte[6];
//
//        WifiManager wifiManager = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
//        if (wifiManager.isWifiEnabled() == false)
//        {
//            return null;
//        } else
//        {
//            String strMac = wifiManager.getConnectionInfo().getMacAddress();
//            strMac = strMac.replace(":", ""); // remove ":"
//            strMac = strMac.replace(".", ""); // remove "."
//
//            if (strMac.length() < 12)
//            {
//                return null;
//            }
//
//            for (int i = 0; i < 6; i++)
//            {
//                int nStart = i * 2;
//                int nEnd = nStart + 2;
//                byteMacAddr[i] = (byte) Integer.parseInt(strMac.substring(nStart, nEnd), 16);
//            }
//        }
//        return byteMacAddr;
//    }

//    public static String getMACAddress(Context _context) {
//        LOG.debug("Network::getMACAddress()");
//
//        String strMacAddress = null;
//        boolean bIsWifiOff = false;
//
//        WifiManager wifiMgr = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
//
//        WifiInfo wifi = wifiMgr.getConnectionInfo();
//        strMacAddress = wifi.getMacAddress();
//
//
//        if( !wifiMgr.isWifiEnabled() && strMacAddress == null ) {
//            wifiMgr.setWifiEnabled(true);
//            bIsWifiOff = true;
//        }
//
//        wifi = wifiMgr.getConnectionInfo();
//        strMacAddress = wifi.getMacAddress();
//
//        if( bIsWifiOff ) {
//            wifiMgr.setWifiEnabled(false);
//            bIsWifiOff = false;
//        }
//
//        LOG.debug("++ strMacAddress = [%s]", strMacAddress);
//
//        return strMacAddress;
//    }



    /**
     *  Get Device MODEL
     *
     * @return OS Version
     */
    public static String getModel(Context _context)
    {
        LOG.debug("getModel() Name=" + Build.MODEL);

        String modelName = Build.MODEL;

        return modelName;
    }


    public static boolean isTablet ( Context context ) {

        LOG.info(">> isTablet()");

        int portrait_width_pixel = Math.min(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels);
        int dots_per_virtual_inch = context.getResources().getDisplayMetrics().densityDpi;
        float virutal_width_inch = portrait_width_pixel / dots_per_virtual_inch;

        return (virutal_width_inch > 2);

    }


    public static String getPhoneNumber(Context _context)
    {
        return ((TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }



    @SuppressWarnings("deprecation")
    public static boolean isAirplaneMode(Context _context)
    {
        return Settings.System.getInt(_context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

//    public static boolean isInsertedUsim(Context _context)
//    {
//        String mdn = getPhoneNumber(_context);
//
//        if (mdn == null || mdn.length() <= 0)
//        {
//            return false;
//        }
//
//        return true;
//    }

    public static boolean isRoaming(Context _context)
    {
        return ((TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE)).isNetworkRoaming();
    }

    public static boolean isTabletDevice(Context activityContext)
    {
        // Verifies if the Generalized Size of the device is XLARGE to be
        // considered a Tablet
        boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);

        // If XLarge, checks if the Generalized Density is at least MDPI
        // (160dpi)
        if (xlarge)
        {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
            // DENSITY_TV=213, DENSITY_XHIGH=320
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH)
            {

                // Yes, this is a tablet!
                return true;
            }
        }

        // No, this is not a tablet!
        return false;
    }


    public static boolean isInsertedUsim (Context _context) {

        TelephonyManager tm = (TelephonyManager)_context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT){

            return true;

        } else {

            return false;
        }

    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }




}
