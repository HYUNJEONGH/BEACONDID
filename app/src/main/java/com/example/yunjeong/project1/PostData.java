package com.example.yunjeong.project1;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by YunJeong on 2016-08-25.
 */
public class PostData {
    private OkHttpClient client = new OkHttpClient();

    String run(String url, RequestBody formbody) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .post(formbody)
                .build();

        Response response = client.newCall(request).execute();
        //RESPONSE.BODY().STRING() CALL ONCE!
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }
}


