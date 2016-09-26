package com.example.yunjeong.project1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DeleteCardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mUserid;
    private static String URL_GETCARD = "http://14.63.196.137/app/get_Card_List.php";
    private ArrayList<CardData> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_delete_card);
        recyclerView = (RecyclerView)findViewById(R.id.delete_card_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_deleteC_toolbar);
         /* center */
        toolbar.inflateMenu(R.menu.setting_menu);
        toolbar.setTitle("DELETE CARD");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        mUserid =intent.getStringExtra("userid");
        new GetCardExecute().execute(mUserid);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    class GetCardExecute extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String Userid = params[0];
            PostData postData = new PostData();
            RequestBody body = new FormBody.Builder()
                    .add("userid", Userid)
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

        @Override
        protected void onPostExecute(String s) {
            mAdapter = new MycardAdapter(cards);
            recyclerView.setAdapter(mAdapter);
        }
    }

}
