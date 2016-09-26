package com.example.yunjeong.project1;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by YunJeong on 2016-09-09.
 */
public class DeleteCartItem extends AsyncTask<String, Void, String>{
    private static final String URL_DELETECARTITEM = "http://14.63.196.137/app/delete_cart_item.php";
    PostData postData;
    int success;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        postData = new PostData();
    }
    @Override
    protected String doInBackground(String... params) {
        String cartId = params[0];

        RequestBody formbody = new FormBody.Builder()
                .add("cartId", cartId)
                .build();
        try {
            String response = postData.run(URL_DELETECARTITEM, formbody);
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
