package videofactory.net.cookingclass.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.compat.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.videofactory.planb.utils.LOG;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hoony on 2017-01-24.
 */

public class Utilities {

    public static void logD (String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static float dpToPx(Context context, float valueInDp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static long getCurrentTimeMinutes(){
        return System.currentTimeMillis()/60000;
    }

    public static long transMinutesToTimeMillis(long minutes){
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    public static String transTimeFormatFromTimeMillis(long timeMillis){
        return transTimeFormatFromTimeMillis(timeMillis, null);
    }

    public static String transTimeFormatFromTimeMillis(long timeMillis, String dateFormat){
        SimpleDateFormat simpleDateFormat;
        if(dateFormat == null){
            simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        }else{
            simpleDateFormat = new SimpleDateFormat(dateFormat);
        }
        String newTimeFormat = simpleDateFormat.format(new Date(timeMillis));
        return newTimeFormat;
    }

    public static float getDip(Context context, int value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPassword(CharSequence password){
        String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{6,20}$";
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static JsonNode jsonParse(String data){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory jsonFactory = mapper.getFactory();
            return mapper.readTree(jsonFactory.createParser(data));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String SHA256(String msg){
        StringBuffer sb = new StringBuffer();

        String data = "";
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(msg.getBytes());
            byte[] mdBytes = md.digest();

            data = byteAsString(mdBytes);

            return data;
        }catch (Exception localNoSuchAlgorithmException) {
            localNoSuchAlgorithmException.printStackTrace();
        }
        return data;
    }

    public static String byteAsString(byte[] dataBytes) throws Exception {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dataBytes.length; i++) {
            String hex = Integer.toHexString(0xFF & dataBytes[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void hideKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public static ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return  spans;
    }

    public static ArrayList<String> getSpanStrings(String body, char prefix) {
        ArrayList<String> spans = new ArrayList<>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            spans.add(matcher.group());
        }

        return spans;
    }

    static public BitmapFactory.Options getBitmapSize(File imageFile){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        return options;
    }

    public static boolean isStoragePermissionGranted(Context mContext, Activity activity){
        if(Build.VERSION.SDK_INT >=23){
            if(mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                LOG.debug("Write External Storage Permission is granted");
                return true;
            }else{
                LOG.debug("Write External Storage Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }else{
            LOG.debug("Write External Storage Permission is granted");
            return true;
        }
    }

    public static boolean isCameraPermissionGranted(Context mContext, Activity activity){
        if(Build.VERSION.SDK_INT >=23){
            if(mContext.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                LOG.debug("Camera Permission is granted");
                return true;
            }else{
                LOG.debug("Camera Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }else{
            LOG.debug("Write External Storage Permission is granted");
            return true;
        }
    }
}
