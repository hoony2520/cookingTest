package videofactory.net.cookingclass.utils;

import android.os.Build;

import java.util.Locale;


public class OS
{
    public static int getApiLevel()
    {
        return Build.VERSION.SDK_INT;
    }

    public static int getSdkVersion()
    {
        int sdkVersion = Integer.parseInt(Build.VERSION.SDK);

        return sdkVersion;
    }


    /**
	 * Gets the OS Version
	 *
	 * @return OS Version
	 */
	public static String getOSVersion ( ) {
		LOG.info(">> getOSVersion()");

		String strOsVersion = Build.VERSION.RELEASE;
		LOG.info("++ strOsVersion = [%s]", strOsVersion);

		return strOsVersion;
	}



    public static String getVender()
    {
        return Build.MANUFACTURER.toUpperCase();
    }

    // os version check
    public static String getVersionType()
    {
        String OS_Version = Build.VERSION.RELEASE;
        return OS_Version;
    }
    
    
    /**
	 * 현재 단말에 설정된 로케일을 가져온다.
	 * 
	 * @return ISO-639 코드값 (ko_KR, en_US, en_UK, ...)
	 */
	public static String getLocale() {
		String locale = Locale.getDefault().toString();
		return locale;
	}
	
    /**
	 * 로케일 체크한다
	 * 
	 * @return 한국어 이면 true 
	 */
	public static boolean isKorean() {
		if( getLocale().equals("ko_KR") || getLocale().equals("ko")) {
			return true;
		}
		return false;
	}
}
