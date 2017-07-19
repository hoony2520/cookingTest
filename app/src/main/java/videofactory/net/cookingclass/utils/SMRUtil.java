package videofactory.net.cookingclass.utils;

import android.content.Context;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Enumeration;


public class SMRUtil {

    public static String getUUID(Context context) {
        String uuid = null;

        DecimalFormat df = new DecimalFormat("00");
        Calendar cal = Calendar.getInstance();
        String now = "" + cal.get(Calendar.YEAR) + df.format(cal.get(Calendar.MONDAY) + 1) + df.format(cal.get(Calendar.DAY_OF_MONTH))
                + df.format(cal.get(Calendar.HOUR_OF_DAY)) + df.format(cal.get(Calendar.MINUTE)) + df.format(cal.get(Calendar.SECOND));
        uuid = NetworkUtil.getMD5Hash("SMR_MEMBERS" + now + getIPAddress(true));

        LOG.debug("# getUUID() returns" + uuid);
        return uuid;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress=inetAddress.getHostAddress().toString();
                        LOG.debug("IP address"+ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        LOG.debug("# getIPAddress()=");
        return "";
    }

}
