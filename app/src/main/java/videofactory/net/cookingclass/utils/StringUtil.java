package videofactory.net.cookingclass.utils;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Calendar;


public class StringUtil {
    public static boolean isInteger(String str) {
        if (str == null || "".equals(str))
            return false;
        return str.matches("^[0-9]+$");

    }

    public static int parseInt(String _string) {
        int value = 0;
        try {
            if (_string != null && !_string.equals("") && isInteger(_string))
                value = Integer.parseInt(_string);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static long parseLong(String _string) {
        // TODO Auto-generated method stub
        long value = 0;
        try {
            if (_string != null && !_string.equals("") && isInteger(_string))
                value = Long.parseLong(_string);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static String getDateString(int _diffFromToday)// returns YYYYMMDD
    {
        Calendar rightNow = Calendar.getInstance();

        if (_diffFromToday != 0) {
            rightNow.add(Calendar.DATE, _diffFromToday);
        }
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);//
        int date = rightNow.get(Calendar.DATE);//

        DecimalFormat decimalFormat = new DecimalFormat("00");
        String yesterday;
        yesterday = decimalFormat.format(year) + decimalFormat.format(month + 1) + decimalFormat.format(date);
        return yesterday;
    }

    public static String getNumericData(int _number)// returns 1234 to 1,234
    {
        if (_number == 0) {
            return "0";
        }
        String value = "";
        int nam = _number % 1000;
        int mok = _number / 1000;
        int temp = 0;
        if (mok > 0) {
            if (nam >= 100) {
                value = "" + nam;
            } else if (nam >= 10) {
                value = "0" + nam;
            } else {
                value = "00" + nam;
            }
        } else {
            value = "" + nam;
        }

        while (mok > 0) {

            temp = mok;
            nam = temp % 1000;
            mok = temp / 1000;
            if (mok > 0) {
                if (nam >= 100) {
                    value = "" + nam + "," + value;
                } else if (nam >= 10) {
                    value = "0" + nam + "," + value;
                } else {
                    value = "00" + nam + "," + value;
                }

            } else {
                value = "" + nam + "," + value;
            }
        }
        return value;
    }

    public static String getNumericData(String _param) // returns 1234 to 1,234
    {
        if (_param == null || _param.equals("")) {
            return "0";
        }
        if (_param.contains(".")) {
            return _param;
        }
        int number = 0;
        try {
            number = Integer.parseInt(_param);
        } catch (Exception e) {
            LOG.debug("getNumericData e=" + e.getMessage());
            return "0";
        }
        return getNumericData(number);
    }

    public static String getYesterday(int _theDayBefore)// 1占싱몌옙 30占싱몌옙 30占쏙옙 占쏙옙
    // YYYYMMDD 占쏙옙占승뤄옙 占쏙옙환
    {
        Calendar rightNow = Calendar.getInstance();
        int before = _theDayBefore * (-1);
        rightNow.add(Calendar.DATE, before);
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);//
        int date = rightNow.get(Calendar.DATE);//

        DecimalFormat decimalFormat = new DecimalFormat("00");
        String yesterday;
        yesterday = decimalFormat.format(year) + decimalFormat.format(month + 1) + decimalFormat.format(date);
        return yesterday;
    }

    public static boolean isFloat(String str) {
        if (str == null || "".equals(str))
            return false;
        return str.matches("([0-9]*)\\.([0-9]*)");
    }

    /**
     * 占싱뱄옙占쏙옙 url占쏙옙 占싼깍옙占�占쏙옙占쏙옙占싱몌옙占쏙옙 占쏙옙占쏙옙占쏙옙占쌔댐옙. http://116.84.245.148:11000/images/facility/20110921165747690.JPG => 20110921165747690.JPG
     * 
     * @param strUrl
     * @return
     */
    public static String getFileNameFromURL(String strUrl) {
        LOG.info(">> getFileNameFromURL()");
        String strFileName = "";

        int nIndex = strUrl.lastIndexOf("/");
        strFileName = strUrl.substring(nIndex + 1);
        LOG.info("strFileName : " + strFileName);

        return strFileName;
    }

    public static boolean isEmpty(String str) {
        if (null == str || 0 == str.trim().length())
            return true;

        return false;
    }

    public static String urlencode(String original) {
        try {
            return URLEncoder.encode(original, "UTF-8");
            // fixed: to comply with RFC-3986
            // return URLEncoder.encode(original, "euc-kr").replace("+",
            // "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (Exception e) {
        }
        return null;
    }

}
