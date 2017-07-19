package videofactory.net.cookingclass.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 업그레이드 해야 할 각 모듈들의 버전을 관리한다.
 * 
 * @author JW.LEE
 * 
 */
public class UpdateManager {

    public static final int UPDATE_NONE  = 0x00;
    public static final int MAJOR_UPDATE = 0x01;
    public static final int MINOR_UPDATE = 0x02;
    public static final int EXTRA_UPDATE = 0x03;

    /**
     * Constructor
     * 
     * @param context
     */
    /**
     * 현재 App의 버전을 Manifest에서 가져온다
     * 
     * @param context
     *            : applicatio ncontext
     * @return String
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            // versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            versionName = PackageUtil.getVersionName(context);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 현재 App의 버전코드를 Manifest에서 가져온다
     * 
     * @param context
     *            : application context
     * @return int
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 현재 단말의 버전과 서버에서 받아온 App 버전을 비교하여 업데이트 여부를 확인한다
     * 
     * @param checkVersion
     *            : "0.0.0" format 버전
     */
    public static int checkUpdate(Context context, String serverVersion) {
        LOG.debug(">> checkUpdate()");
        LOG.debug("++ serverVersion = [%s]", serverVersion);

        int nUpdateType = -1;

        // Server Version 없을 경우..
        if (serverVersion == null || serverVersion.length() == 0)
            return nUpdateType;

        // App Version
        String currentVersion = getAppVersionName(context);
        LOG.debug("++ currentVersion = [%s]", currentVersion);

        // App Version 없을 경우..
        if (currentVersion == null || currentVersion.length() == 0)
            return nUpdateType;

        String[] appVersion = currentVersion.split("\\.");
        LOG.debug("++ appVersion = [%s]", currentVersion);

        // App Version load error
        if (appVersion == null || serverVersion.length() == 0)
            return MAJOR_UPDATE;

        int nAppVersionMajor = 0;
        int nAppVersionMinor = 0;
        int nAppVersionExtra = 0;

        if (appVersion.length > 2)
            nAppVersionExtra = StringUtil.parseInt(appVersion[2]);

        if (appVersion.length > 1)
            nAppVersionMinor = StringUtil.parseInt(appVersion[1]);

        if (appVersion.length > 0)
            nAppVersionMajor = StringUtil.parseInt(appVersion[0]);

        // Server Version
        String[] arServerVersion = serverVersion.split("\\.");

        if (arServerVersion == null)
            return -1;

        nUpdateType = UPDATE_NONE;
        if (arServerVersion.length == 3) {

            int nServerMajor = StringUtil.parseInt(arServerVersion[0]);
            int nServerMinor = StringUtil.parseInt(arServerVersion[1]);
            int nServerExtra = StringUtil.parseInt(arServerVersion[2]);

            // Major Update!!
            if (nAppVersionMajor < nServerMajor) {
                nUpdateType = MAJOR_UPDATE;
            }
            // Minor Update!!
            else if (nAppVersionMajor == nServerMajor && nAppVersionMinor < nServerMinor) {
                nUpdateType = MINOR_UPDATE;
            }
            // Extra Update!!
            else if (nAppVersionMajor == nServerMajor && nAppVersionMinor == nServerMinor && nAppVersionExtra < nServerExtra) {
                nUpdateType = EXTRA_UPDATE;
            }

        } else if (arServerVersion.length == 2) {
            int nServerMajor = StringUtil.parseInt(arServerVersion[0]);
            int nServerMinor = StringUtil.parseInt(arServerVersion[1]);

            // Major Update!!
            if (nAppVersionMajor < nServerMajor) {
                nUpdateType = MAJOR_UPDATE;
            }
            // Minor Update!!
            else if (nAppVersionMajor == nServerMajor && nAppVersionMinor < nServerMinor) {
                nUpdateType = MINOR_UPDATE;
            }
        } else if (arServerVersion.length == 1) {
            int nServerMajor = StringUtil.parseInt(arServerVersion[0]);

            // Major Update!!
            if (nAppVersionMajor < nServerMajor) {
                nUpdateType = MAJOR_UPDATE;
            }
        } else if (arServerVersion.length == 0) {
            nUpdateType = -1;
        }

        return nUpdateType;
    }

}
