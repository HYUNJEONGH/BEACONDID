package com.example.yunjeong.project1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CartActivity extends AppCompatActivity {
    private static final String URL_GETCARTLIST = "http://14.63.196.137/app/get_Cart_List.php";
    private ArrayList<CartItemData> cartList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btnCOrder;
    private boolean check;
    public static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_cart);
        mRecyclerView = (RecyclerView)findViewById(R.id.cart_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_cart_toolbar);
         /* center */
        toolbar.inflateMenu(R.menu.setting_menu);
        toolbar.setTitle("장바구니");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });
        check = true;
        //getID
        SharedPreferences preferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userid = preferences.getString(Config.USERID_SHARED_PREF, "no ID");
        new getCartList().execute(userid);

        textView = (TextView)findViewById(R.id.totalPrice);
        btnCOrder = (Button)findViewById(R.id.btn_order);
        btnCOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartList == null || mAdapter.getItemCount() == 0) {
                    Log.i("cartlistis", "null");
                    final Dialog dialog = new Dialog(CartActivity.this);
                    dialog.setContentView(R.layout.cart_empty_dialog);
                    Button btnOk = (Button) dialog.findViewById(R.id.btnok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return;
                }
                Intent intent = new Intent(getApplication(), CheckOutActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void updateUserInterface() {
        mAdapter = new MycartAdapter(cartList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class getCartList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String userid = params[0];
            Log.i("params:", userid);
            PostData postData = new PostData();
            RequestBody formbody = new FormBody.Builder()
                    .add("userid", userid)
                    .build();

            try {
                String response = postData.run(URL_GETCARTLIST, formbody);
                JSONObject json = new JSONObject(response);
                Log.i("json in cartActivity:", json.toString());
                int success = json.getInt("success");
                if(success == 1) {
                    Log.i("success:", "1");
                    check = true;
                    cartList = new ArrayList<CartItemData>();
                    JSONArray items = json.getJSONArray("cartItems");
                    for(int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        //String cartId, String itemId, String itemName, int itemAmount, String userId
                        cartList.add(new CartItemData(item.getString("cartId"),item.getString("itemId"), item.getString("itemName"),
                                item.getInt("itemAmount"), item.getInt("itemPrice"), item.getString("userId")));
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.cartLayout), R.string.noCData ,Snackbar.LENGTH_LONG);
                    snackbar.show();
                    check = false;
                    return "0";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1")) {
                Log.i("1 execute:","start");
                updateUserInterface();
            }
        }
    }
}
