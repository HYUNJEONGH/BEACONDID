package com.example.yunjeong.project1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PasswdActivity extends AppCompatActivity {
    private EditText pwd1;
    private EditText pwd2;
    private EditText pwd3;
    private EditText pwd4;
    private EditText pwd5;
    private EditText pwd6;
    private EditText pwd7;
    private EditText pwd8;
    private EditText pwd9;
    private EditText pwd10;
    private EditText pwd11;
    private Button btnPassCheck;
    private Dialog dialog;
    private Dialog completeDiglog;
    private String userid;
    private int orderid;
    private int cardId;
    HashMap<String, String> orderitem;
    HashMap<String, String> payitem;
    HashMap<String, String> beaconInfo;
    PostData postData;
    String itemtoGson;
    private WebSocketClient mWebSocketClient;
    private static String URL_COMPAREPASS = "http://14.63.196.137/app/get_Card_Passwd.php";
    private static String URL_INSERTORDER = "http://14.63.196.137/app/insert_order_List.php";
    private static String URL_INSERTPAYINFO = "http://14.63.196.137/app/insert_pay_info.php";
    private final String TAG = "CheckOutActivity";
    public final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private ArrayList<HashMap<String, String>> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_passwd);
        setEditText();
        btnPassCheck = (Button)findViewById(R.id.btnPassCheck);
        final Intent intent = getIntent();
        cartList = (ArrayList<HashMap<String, String>>)intent.getSerializableExtra("map");
        userid = intent.getStringExtra("userid");
        beaconInfo = (HashMap<String, String>) intent.getSerializableExtra("beaconInfo");
        //TODO:결제비밀번호 누르기 성공시 그다음, 실패시 종료
        btnPassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardId = intent.getIntExtra("cardId", 0);
                new comparePasswd().execute(Integer.toString(cardId));
            }
        });
        setDialog();
    }

    private void setDialog() {
        dialog = new Dialog(PasswdActivity.this);
        dialog.setContentView(R.layout.invalid_passwd_dialog);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) dialog.findViewById(R.id.btnok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        completeDiglog = new Dialog(PasswdActivity.this);
        completeDiglog.setContentView(R.layout.order_complete_dialog);
        completeDiglog.setCanceledOnTouchOutside(false);
        Button btnOk1 = (Button) completeDiglog.findViewById(R.id.btnok);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("completely oreder:", "PasswdActivity");
                completeDiglog.dismiss();
                finish();
            }
        });
    }

    private void setEditText() {
        pwd1 = (EditText)findViewById(R.id.pwd1);
        pwd2 = (EditText)findViewById(R.id.pwd2);
        pwd3 = (EditText)findViewById(R.id.pwd3);
        pwd4 = (EditText)findViewById(R.id.pwd4);
        pwd5 = (EditText)findViewById(R.id.pwd5);
        pwd6 = (EditText)findViewById(R.id.pwd6);
        pwd7 = (EditText)findViewById(R.id.pwd7);
        pwd8 = (EditText)findViewById(R.id.pwd8);
        pwd9 = (EditText)findViewById(R.id.pwd9);
        pwd10 = (EditText)findViewById(R.id.pwd10);
        pwd11 = (EditText)findViewById(R.id.pwd11);
        pwd1.setNextFocusDownId(pwd2.getId());
        pwd2.setNextFocusDownId(pwd3.getId());
        pwd3.setNextFocusDownId(pwd4.getId());
        pwd4.setNextFocusDownId(pwd5.getId());
        pwd5.setNextFocusDownId(pwd6.getId());
        pwd6.setNextFocusDownId(pwd7.getId());
        pwd7.setNextFocusDownId(pwd8.getId());
        pwd8.setNextFocusDownId(pwd9.getId());
        pwd9.setNextFocusDownId(pwd10.getId());
        pwd10.setNextFocusDownId(pwd11.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.textView3).post(
                new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(pwd1.getId(), InputMethodManager.SHOW_IMPLICIT);
                        pwd1.requestFocus();
                    }
                }
        );
        connectWebSocket();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebSocketClient.close();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private class comparePasswd extends AsyncTask<String, Void, String> {
        String passwd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            passwd = "";
            passwd += pwd1.getText().toString();
            passwd += pwd2.getText().toString();
            passwd += pwd3.getText().toString();
            passwd += pwd4.getText().toString();
            passwd += pwd5.getText().toString();
            passwd += pwd6.getText().toString();
            passwd += pwd7.getText().toString();
            passwd += pwd8.getText().toString();
            passwd += pwd9.getText().toString();
            passwd += pwd10.getText().toString();
            passwd += pwd11.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            postData = new PostData();
            RequestBody body = new FormBody.Builder()
                    .add("cardId", id)
                    .build();
            try {
                String responce = postData.run(URL_COMPAREPASS, body);
                JSONObject json = new JSONObject(responce);
                int success = json.getInt("success");
                String compare = json.getString("passwd");
                if(success == 1) {
                    if(compare.equals(passwd)) {
                        boolean result = insertOrderList();
                        Log.i("result insert:", result +"");
                        if(result) {
                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    completeDiglog.show();
                                }
                            }, 0);
                        }
                    } else {
                       return "0";
                    }
                } else {
                    Toast.makeText(PasswdActivity.this,"Data access fail", Toast.LENGTH_SHORT).show();
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
            super.onPostExecute(s);
            String result = s;
            if(result.equals("0")) {
                dialog.show();
            }
        }
    }

    private boolean insertOrderList() {
        //productID,productAmount,userID,payConfirm
        for(int i = 0; i < cartList.size(); i++) {
            Gson gson = new Gson();
            orderitem = new HashMap<>();
            orderitem.put("productID", cartList.get(i).get("itemId"));
            orderitem.put("productAmount", cartList.get(i).get("itemAmount"));
            orderitem.put("userID", userid);
            itemtoGson = gson.toJson(orderitem);
            Log.i("PasswdActivity", itemtoGson.toString());
            RequestBody jsonbody = RequestBody.create(JSON, itemtoGson);
            String response = null;
            try {
                response = postData.run(URL_INSERTORDER, jsonbody);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = null;
            int success1 = 0;
            orderid = 0;
            try {
                jsonObject = new JSONObject(response);
                success1 = jsonObject.getInt("success");
                orderid = jsonObject.getInt("insertid");
                Log.i("response", jsonObject.toString());
                //TODO:결제리스트에 Insert
                insetPayInfo(orderid, cartList.get(i).get("itemPrice"), orderitem.get("userID"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success1 != 1) {
                return false;
            }
                mWebSocketClient.send(Integer.toString(orderid));
        }
        return true;
    }

    private void insetPayInfo(int orderID, String payAmount, String userid) {
        //cardID | payDate | payAmount | orderID | userID | payCatagoryID | beaconNum |
        Gson gson = new Gson();
        String paytogson;
        TimeZone jst = TimeZone.getTimeZone ("JST");
        Calendar cal = Calendar.getInstance ( jst );
        String date = cal.get ( Calendar.YEAR ) + "년 " + ( cal.get ( Calendar.MONTH ) + 1 ) + "월 " + cal.get ( Calendar.DATE ) + "일 ";
        payitem = new HashMap<>();
        payitem.put("cardID", Integer.toString(cardId));
        payitem.put("payDate", date);
        payitem.put("payAmount", payAmount);
        payitem.put("orderID", Integer.toString(orderID));
        payitem.put("userID", userid);
        payitem.put("payCatagoryID","PC0002");
        payitem.put("uuid", beaconInfo.get("uuid"));
        payitem.put("major", beaconInfo.get("major"));
        payitem.put("minor", beaconInfo.get("minor"));
        paytogson = gson.toJson(payitem);
        Log.i("Passwd paytogson", paytogson.toString());
        RequestBody jsonbody = RequestBody.create(JSON, paytogson);
        String response = null;
        try {
            response = postData.run(URL_INSERTPAYINFO, jsonbody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        int success1 = 0;
        try {
            jsonObject = new JSONObject(response);
            success1 = jsonObject.getInt("success");
            Log.i("response paylist", jsonObject.toString());
            if(success1 != 1) {
                //TODO:ERROR
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class MessageManagementTask extends AsyncTask<String, Void, Void> {

        private StringBuilder messageBuilder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            messageBuilder = new StringBuilder();
        }

        @Override
        protected Void doInBackground(String... strings) {
            messageBuilder.append(strings[0] + "\n");
            return null;
        }

    }

    private void connectWebSocket() {

        URI uri;

        try {
            uri = new URI("ws://172.30.32.24:8080/portal/websocket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i(TAG, "Opened");
            }

            @Override
            public void onMessage(String s) {
                new MessageManagementTask().execute(s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i(TAG, "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i(TAG, "Error " + e.getMessage());
            }

        };

        mWebSocketClient.connect();

    }
}
