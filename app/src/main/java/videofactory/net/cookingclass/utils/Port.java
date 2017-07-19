package videofactory.net.cookingclass.utils;

import android.content.Context;


public class Port {

    public static boolean useSingleVideo(Context context) {

        // 버전기준
        if (OS.getApiLevel() < 16) {
            // 안드로이드 4.1 미만은
            // 두개의 MediaPlayer로 Switch 하는 기능을 할 수 없음.
            // Switching 하는 중간광과는 서비스 할 수 없음.
            return true;
        }

        String model = DeviceInfo.getModel(context);

        if (model == null || "".equals(model)) {
            return false;
        }

        // 모델명 기중

        if (model.startsWith("IM-A7")) {

            return true;

        }

        return false;
    }

//    public static boolean useNewPlayer(Context context) {
//        boolean rtn = false;
//
//        String model = DeviceInfo.getModel(context);
//
//        if (model == null || "".equals(model)) {
//            return rtn;
//        }
//
//        // || model.startsWith("SHV-E300") || model.startsWith("SHV-E330") || model.startsWith("LG-F240")
//        if (model.startsWith("IM-A780")) {
//
//            rtn = true;
//
//        }
//
//        return rtn;
//    }
}
