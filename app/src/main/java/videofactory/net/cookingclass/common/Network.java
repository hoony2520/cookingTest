package videofactory.net.cookingclass.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import net.videofactory.planb.R;
import net.videofactory.planb.ui.screens.IntroScreen;
import net.videofactory.planb.utils.LOG;
import net.videofactory.planb.utils.iNetwork;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by silentstorm on 15. 10. 15..
 */
public abstract class Network  {

    private Context context;
    public static Retrofit retrofit ;
    private static String HOST;
    private static String ostp;
    private static String lang;
    private static String appid;
    private static String key;

    private static String VERSION;
    private String USERNAME;
    private String API;

    private Map map;                        // post param
    private Map query;
    private SharedPreferences prefs;

    public Network(Context mContext, String s) {
        this.context = mContext;
    }

    public void setData(Map map, String API) {
        this.map = map;
        this.API = API;
    }

    protected void execute() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(this.context.getString(R.string.server_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        // URL
        if(HOST == null) HOST = context.getString(R.string.server_url);
        if(VERSION == null ) VERSION = context.getString(R.string.app_version);
        USERNAME = UserInfo.getNickname();
        key = UserInfo.getSessAuthKey();

        // URL QUERY
        if(ostp == null) ostp = context.getString(R.string.ostp);
        if(appid == null) appid = UserInfo.getUserAndroidId(context);
        lang = context.getString(R.string.lang);
        query = new HashMap();
        query.put("lang", lang);
        query.put("osTp", ostp);

        if(appid != null && !"".equals(appid)){
            query.put("appId", appid);
        }
        if(key != null && !"".equals(key)){
            query.put("sessAuthKey", key);
        }

        try {
            iNetwork inetwork = retrofit.create(iNetwork.class);
            String json = new ObjectMapper().writeValueAsString(this.map);
            Map param = new HashMap();
            LOG.debug(API+this.query);
            LOG.debug(json);
            param.put(json, "");
            Call<Map> call = null;
            if(USERNAME == null || key == null || "".equals(USERNAME) || "".equals(key)){
                call = inetwork.getNonAuthData(VERSION, API, this.query, param);
            }else{
                call = inetwork.getAuthData(VERSION, USERNAME, API, this.query, param);
            }
            call.enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if(response.isSuccessful()){
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body(), LinkedHashMap.class);
                        onPostExecute(json);
                    }
                }
                @Override
                public void onFailure(Call<Map> call, Throwable t) {
//                    goToIntro();
                    LOG.debug("ASDFASDF");
                    connectFail();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onPostExecute(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(json);
            String rtn = actualObj.get("RTN_VAL").asText();
            if(rtn != null){
                if("Y".equals(rtn)){
                    processFinish(actualObj);
                }else if("N".equals(rtn)){
                    onResultN(actualObj);
                }else{
                    String msg = actualObj.get("MSG") == null ? "" : actualObj.get("MSG").asText();
                    String errorType = actualObj.get("error_type") != null ? actualObj.get("error_type").asText() : "";
                    if("D".equals(rtn) &&
                            "version".equals(errorType)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(msg);
                        builder.setCancelable(true);
                        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Activity activty = (Activity) context;
                                activty.moveTaskToBack(true);
                                activty.finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        });
                        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
                                context.startActivity(intent);
                            }
                        });
                        builder.show();
                    }else{
                        Toast.makeText(context, "Session is expired.", Toast.LENGTH_LONG).show();
                        prefs = context.getSharedPreferences("loginInfo", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear().apply();
                        UserInfo.clearUserInfo();
                        goToIntro();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.debug("Network", "Network Exception : " + e.toString());
            connectFail();
        }
    }

    private void goToIntro(){
        Intent intent = new Intent(context, IntroScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected void onResultN(JsonNode result){
        if(result.get("MSG") != null && !"".equals(result.get("MSG").asText())){
            String msg = result.get("MSG").asText();


            final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText(context.getResources().getString(R.string.error));
            pDialog.setCancelable(true);
            pDialog.setContentText(msg);
            pDialog.setConfirmText(context.getResources().getString(R.string.ok));
            pDialog.show();

            pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    pDialog.dismissWithAnimation();
                }
            });

//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage(msg);
//            builder.setCancelable(true);
//            builder.setNegativeButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                }
//            });
//            builder.show();
        }
        processError(result);
    }

    protected void connectFail(){
        String errMsg = context.getResources().getString(R.string.network_error);
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(context.getResources().getString(R.string.error));
        pDialog.setContentText(errMsg);
        if(context != null){
            pDialog.show();
        }


        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ((Activity)context).moveTaskToBack(true);
                ((Activity)context).finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    protected abstract void processFinish(JsonNode result);

    protected abstract void processError(JsonNode result);

}
