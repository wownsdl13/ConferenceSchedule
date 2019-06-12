package server;

import android.app.Activity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class SignInServer extends ServerRoot {

    public SignInServer(Activity activity, String url) {
        super(activity, url);
    }

    public SignInServer setInfo(String id, String pw){
        mainJSONMap.put("id", id);
        mainJSONMap.put("pw", pw);
        return this;
    }

    @Override
    void afterResponse(String result) throws UnsupportedEncodingException, JSONException {

    }
}
