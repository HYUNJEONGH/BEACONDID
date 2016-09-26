package com.example.yunjeong.project1;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by YunJeong on 2016-09-07.
 */
public class AddCartExcute extends AsyncTask<String, Void, String> {
    private static String URL_ADDCART = "http://14.63.196.137/app/add_item_Cart.php";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String item = params[0];
        Log.i("item in doinback:",item);
        PostData postData = new PostData();
        RequestBody jsonbody = RequestBody.create(JSON, item);
        String response = null;
        try {
            response = postData.run(URL_ADDCART, jsonbody);
            JSONObject jsonObject = new JSONObject(response);
            Log.i("jsonob in async:", jsonObject.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
