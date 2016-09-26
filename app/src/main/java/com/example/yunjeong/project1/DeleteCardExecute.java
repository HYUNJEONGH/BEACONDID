package com.example.yunjeong.project1;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by YunJeong on 2016-09-18.
 */
public class DeleteCardExecute extends AsyncTask<String, Void, String> {
    private static final String URL_DELETECARD = "http://14.63.196.137/app/delete_card.php";
    PostData postData = new PostData();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        postData = new PostData();
    }

    @Override
    protected String doInBackground(String... params) {
        int success;
        String cardID = params[0];

        RequestBody formbody = new FormBody.Builder()
                .add("cardID", cardID)
                .build();
        try {
            String response = postData.run(URL_DELETECARD, formbody);
            JSONObject json = new JSONObject(response);
            success = json.getInt("success");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
