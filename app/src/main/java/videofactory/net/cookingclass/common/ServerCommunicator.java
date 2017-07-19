package videofactory.net.cookingclass.common;

import android.content.Context;

import net.videofactory.planb.utils.LOG;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hoony on 2017-01-23.
 */
public class ServerCommunicator {
    private Map dataMap;    // post param
    private Network network;
    private String API;
    public ServerCommunicator(Context context, Network network, String url) {
        try{
            this.network = network;
            this.dataMap = new HashMap();
            this.API = url;
        }catch(Exception e){
            LOG.debug(e);
        }
    }

    public void addData(String key, Object value){
        addData(0, key, value);
    }

    public void addData(int dataSetNum, String key, Object value){
        this.dataMap.put(key, value);
    }

    public void communicate(){
        try{
            network.setData(this.dataMap, this.API);
            network.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
