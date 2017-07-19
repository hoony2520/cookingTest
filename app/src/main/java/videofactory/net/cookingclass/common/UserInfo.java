package videofactory.net.cookingclass.common;

import android.content.Context;
import android.provider.Settings;

import net.videofactory.planb.utils.LOG;

import java.util.HashMap;

/**
 * Created by Utae on 2015-11-06.
 */
public class UserInfo {

    static private HashMap<String, Object> userInfo = new HashMap<>();

    private static Object obj = new Object();

    private static boolean encodingFlag = false;


    public static void setEncodingFlag(boolean flag){
        synchronized (obj){
            encodingFlag = flag;
        }
    }

    public static boolean getEncodingFlag(){
        synchronized (obj){
            LOG.debug("22222222222222222222222:"+encodingFlag);
            return encodingFlag;
        }
    }

    static public String getUserAndroidId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    static public void setUserNum(String userNum){
        userInfo.put("userNum", userNum);
    }

    static public String getUserNum(){
        if(userInfo.get("userNum") == null){
            return null;
        }else{
            return userInfo.get("userNum").toString();
        }
    }

    static public void setNickname(String nickname){
        userInfo.put("nickname", nickname);
    }

    static public String getNickname(){
        if(userInfo.get("nickname") == null){
            return null;
        }else{
            return userInfo.get("nickname").toString();
        }
    }

    static public void setSessAuthKey(String sessAuthKey){
        userInfo.put("sessAuthKey", sessAuthKey);
    }

    static public String getSessAuthKey(){
        if(userInfo.get("sessAuthKey") == null){
            return null;
        }else{
            return userInfo.get("sessAuthKey").toString();
        }
    }

    static public boolean isNull(){
        return getSessAuthKey() == null || getUserNum() == null;
    }

    static public void clearUserInfo(){
        userInfo.clear();
    }
}
