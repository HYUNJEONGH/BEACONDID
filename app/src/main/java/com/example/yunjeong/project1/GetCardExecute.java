package com.example.yunjeong.project1;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.JarOutputStream;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by YunJeong on 2016-09-16.
 */
public class GetCardExecute extends AsyncTask<String, Void, String> {
    private static String URL_GETCARD = "http://14.63.196.137/app/get_Card_List.php";
    private ArrayList<CardData> cards;
    @Override
    protected String doInBackground(String... params) {
        String mUserid = params[0];
        PostData postData = new PostData();
        RequestBody body = new FormBody.Builder()
                .add("userid", mUserid)
                .build();
        try {
            String response =  postData.run(URL_GETCARD, body);
            JSONObject json = new JSONObject(response);
            String success = json.getString("success");
            if(success.equals("1")) {
                JSONArray items = json.getJSONArray("items");
                cards = new ArrayList<CardData>();
                for(int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    cards.add(new CardData(item.getInt("cardID"), item.getString("cardNum"), item.getString("cardName"), item.getString("userID"), item.getString("cardExpiration"),
                    item.getString("cardPassword"), item.getInt("cardCVCNum"), item.getString("cardType")));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
