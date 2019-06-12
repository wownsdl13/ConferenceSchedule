package server;

import android.app.Activity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class TestServer extends ServerRoot {

    public TestServer(Activity activity, String url) {
        super(activity, url);
    }

    @Override
    void afterResponse(String result) throws UnsupportedEncodingException, JSONException {

    }
}
