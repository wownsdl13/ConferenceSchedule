package server;

import android.app.Activity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class GetTimeTableServer extends ServerRoot {

    public GetTimeTableServer(Activity activity, String url) {
        super(activity, url);
    }


    public GetTimeTableServer setInfo(int time){
        mainJSONMap.put("time", time);
        return this;
    }

    @Override
    void afterResponse(String result) throws UnsupportedEncodingException, JSONException {

    }
}
