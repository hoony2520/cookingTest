package videofactory.net.cookingclass.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PackageUtil {

    public static String getVersionName(Context _context) {
        try {
            PackageInfo oPackageInfo = _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0);
            return oPackageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getPackageName(Context _context) {
        try {
            PackageInfo oPackageInfo = _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0);
            return oPackageInfo.packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isInstalled(Context _context, String _PackageName) {
        // ApplicationInfo appInfo = null;
        try {
            ApplicationInfo appInfo = _context.getPackageManager().getApplicationInfo(_PackageName.trim(), PackageManager.GET_ACTIVITIES);

            Intent intent = _context.getPackageManager().getLaunchIntentForPackage(_PackageName);

            if (intent == null)
                return false;

        } catch (NameNotFoundException e) {
            LOG.debug(">> e " + e.getStackTrace());
            return false;
        }
        return true;
    }

    public static boolean goMarket(Context _context, String _Pid) {
        // http://market.android.com/sear\nch?q="+PackageName+
        // packageName�δ� ���� ���� ��찡 �־ pid ������ ���� 2010-12-06 jungchae
        try {
            Uri uri = Uri.parse("market://details?id=" + _Pid + "");
            LOG.debug("uri=" + "market://details?id=" + _Pid + "");
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            if (i != null) {
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean goTstore(Context _context, String _Pid) {
        //
        LOG.debug("goStore pid=" + _Pid);
        Intent intent = null;
        String param = "PRODUCT_VIEW/";
        param += _Pid;
        param += "/0";

        PackageManager pm = _context.getPackageManager();

        intent = pm.getLaunchIntentForPackage("com.skt.skaf.A000Z00040");
        if (intent != null) {
            LOG.debug("go market _Pid=" + _Pid);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setClassName("com.skt.skaf.A000Z00040", "com.skt.skaf.A000Z00040.A000Z00040");
            intent.setAction("COLLAB_ACTION");
            intent.putExtra("com.skt.skaf.COL.URI", param.getBytes());
            intent.putExtra("com.skt.skaf.COL.REQUESTER", "A000Z00040");
            try {
                _context.startActivity(intent);

            } catch (ActivityNotFoundException anfe) {
                anfe.printStackTrace();
                LOG.debug("executeTStoreShop() ActivityNotFoundException = " + anfe.getMessage());
                return false;
            }
        }
        return true;
    }

    public static boolean goOlleh(Context _context, String _pid, String _nid) {
        File f = new File("/data/data/com.kt.olleh.storefront");
        if (f != null && f.isDirectory()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("vnd.kt.olleh.storefront/detail.kt.olleh.storefront");
            intent.putExtra("CONTENT_TYPE", "APPLICATION");
            intent.putExtra("P_TYPE", "c");
            intent.putExtra("P_ID", _pid);
            intent.putExtra("N_ID", _nid);
            try {
                _context.startActivity(intent);
            } catch (Exception e) {
                LOG.error(e);
                return false;
            }
        } else
            return false;

        return true;
    }

    public static boolean goUplus(Context _context, String _pid) {
        boolean isNewVersion = false;
        PackageManager pm = _context.getPackageManager();

        String filename = "/data/data/android.lgt.appstore";

        try {
            ApplicationInfo info = pm.getApplicationInfo("com.lguplus.appstore", 0);
            if (info != null)
                isNewVersion = true;
        } catch (NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (isNewVersion)
            filename = "/data/data/com.lguplus.appstore";

        File f = new File(filename);
        if (f != null && f.isDirectory()) {

            // Intent intent = new Intent(Intent.ACTION_VIEW);
            Intent intent = new Intent();
            if (isNewVersion)
                intent.setClassName("com.lguplus.appstore", "com.lguplus.appstore.Store");
            else
                intent.setClassName("android.lgt.appstore", "android.lgt.appstore.Store");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("payload", "PID=" + _pid);
            try {
                _context.startActivity(intent);
            } catch (Exception e) {
                LOG.error(e);
                return false;
            }
        } else
            return false;

        return true;
    }

    // �������� ����
    // jungchae
    public static boolean goNaverStore(Context _context, String _pid) {
        // File f = new File("/data/data/com.kt.olleh.storefront");
        // if (f != null && f.isDirectory())
        {

            try {
                Uri uri = Uri.parse("appstore://?productNo=" + _pid + "&action=view");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setData(uri);
                _context.startActivity(intent);

            } catch (Exception e) {
                LOG.error(e);
                return false;
            }
        }

        // else
        // return false;

        return true;
    }

    public static void addShortcut(Context _context, String _appName, String _packageName, String _className, int _iconResId) {
        // ex
        // Util.addShortcut(IntroScreen.this,
        // getResources().getString(R.string.app_name), packageName,
        // IntroScreen.this.getClass().getName(), R.drawable.icon);
        try {
            Intent appIntent = new Intent(Intent.ACTION_MAIN);
            appIntent.addCategory("android.intent.category.LAUNCHER");

            appIntent.setClassName(_packageName, _className);
            // appIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, appIntent);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, _appName);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(_context, _iconResId));
            shortcutIntent.putExtra("duplicate", false);
            // shortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            _context.sendBroadcast(shortcutIntent);

        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public static void addSubShortcut(Context _context, String _appName, String _packageName, String _className, int _iconResId, String paramValue) {
        // ex
        // Util.addShortcut(IntroScreen.this,
        // getResources().getString(R.string.app_name), packageName,
        // IntroScreen.this.getClass().getName(), R.drawable.icon);
        try {
            Intent appIntent = new Intent("android.intent.action.VIEW", null);
            appIntent.addCategory("android.intent.category.DEFAULT");
            appIntent.setClassName(_packageName, _className);
            appIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (paramValue != null && !"".equals(paramValue))
                appIntent.putExtra("param", paramValue);

            Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, appIntent);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, _appName);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(_context, _iconResId));
            shortcutIntent.putExtra("duplicate", false);
            // shortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            _context.sendBroadcast(shortcutIntent);

        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public static void showChooser(Activity _activity, String _subject, String _text) {
        LOG.debug(">> doChooser");
        Intent i = new Intent(Intent.ACTION_SEND);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setType("text/plain");

        // i.putExtra(Intent.EXTRA_SUBJECT, _subject);
        i.putExtra(Intent.EXTRA_TEXT, _text);

        _activity.startActivity(Intent.createChooser(i, _subject));
        _activity.overridePendingTransition(0, 0);
    }

    public static void showChooser(Activity _activity, int _titleResId, String uri) {
        LOG.debug(">> doChooser");
        Intent i = new Intent(Intent.ACTION_SEND);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setType("text/plain");

        // i.putExtra(Intent.EXTRA_SUBJECT,
        // _activity.getResources().getString(_titleResId));
        i.putExtra(Intent.EXTRA_TEXT, uri);

        _activity.startActivity(Intent.createChooser(i, _activity.getResources().getString(_titleResId)));
        _activity.overridePendingTransition(0, 0);

    }

    public static String getApplicationInstalledTime(Context context) {

        long lInstalledTime = -1;

        PackageManager pm = context.getPackageManager();

        try {

            // 진저 브레드 이상
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                lInstalledTime = pm.getPackageInfo(getPackageName(context), 0).firstInstallTime;
            }
            // 진저 브레드 이하
            else {
                ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(context), 0);
                String strAppFile = appInfo.sourceDir;
                lInstalledTime = new File(strAppFile).lastModified();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        String strInstalledTime = getDate(lInstalledTime);

        return strInstalledTime;
    }

    private static String getDate(long datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        String strDate = formatter.format(calendar.getTime());

        return strDate;
    }

    public static void launchApplication(Context context, String packageName) {
        PackageManager oPackageManager = context.getPackageManager();
        Intent pInt = oPackageManager.getLaunchIntentForPackage(packageName);
        if (pInt == null) {

        } else {
            try {
                pInt.addCategory(Intent.CATEGORY_LAUNCHER);
                pInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(pInt);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
