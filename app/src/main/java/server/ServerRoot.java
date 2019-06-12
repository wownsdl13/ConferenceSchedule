package server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LeeJaeJun on 2017-07-19.
 */

public abstract class ServerRoot extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    Context activity;

    HashMap<String, Object> mainJSONMap = new HashMap<String, Object>();

    private OkHttpClient client = new OkHttpClient();

    JSONObject requestJSON;
    protected String url = null;
    OnAfterLister lister;

    public interface OnAfterLister {
        void after(String result);
    }

    public ServerRoot(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.show();
    }

    public void setTimeout(int timeout) {
        client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();

    }

    @Override
    protected String doInBackground(String... params) {

        Response response;
        RequestBody requestBody = null;
        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("value", requestJSON.toString()).build();

        Request request = new Request.Builder().
                url(url).
                post(requestBody).
                build();
        try {
            response = client.newCall(request).execute();
            String answer = response.body().string();

            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("결과 : ", s + "<-");
        try {
            if (s == null)
                return;
            afterResponse(URLDecoder.decode(s, "utf-8"));
            if (lister != null) {
                try {
                    lister.after(URLDecoder.decode(s, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (progressDialog != null)
            progressDialog.dismiss();

    }

    abstract void afterResponse(String result) throws UnsupportedEncodingException, JSONException;

    public void setProgressDialog() {
        progressDialog = new ProgressDialog(activity);
    }

    public void run() throws JSONException {
        try {
            setRequestJSON(mainJSONMap);
            this.execute("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void setLister(OnAfterLister lister) {
        this.lister = lister;
    }

    private void setRequestJSON(HashMap<String, Object> mainJSONValue) throws JSONException, UnsupportedEncodingException {
        JSONObject mainJSON = setRequestJSONinside(mainJSONValue);
        requestJSON = mainJSON;
    }

    private JSONObject setRequestJSONinside(HashMap<String, Object> hashSet) throws JSONException, UnsupportedEncodingException {
        JSONObject ObjectJSON = new JSONObject();
        for (String key : hashSet.keySet()) {
            Object o = hashSet.get(key);
            if (o instanceof HashSet) {
                HashSet set = (HashSet) o;
                JSONArray tempArray = new JSONArray();
                for (Object i : set) {
                    tempArray.put(i);
                }
                ObjectJSON.put(key, tempArray);
            } else {
                ObjectJSON.put(key, o);
            }
        }
        return ObjectJSON;
    }

}
