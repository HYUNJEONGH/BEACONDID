package com.example.yunjeong.project1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CheckOutActivity extends AppCompatActivity implements RECOServiceConnectListener, RECORangingListener {

    private static final String URL_GETCARTLIST = "http://14.63.196.137/app/get_Cart_List.php";
    private static final String URL_GETCARD = "http://14.63.196.137/app/get_Card_List.php";
    private static final String URL_GETBEACON = "http://14.63.196.137/app/get_beacon_list.php";
    private ListView listView;
    private Button btnCheckout;
    private Spinner spinner;
    private int totalPrice;
    private boolean checkRegion;
    private TextView tvTotalPrice;
    private ArrayList<Integer> cardId;
    private ArrayAdapter adapter;
    private String userid;
    RECOBeaconManager recoManager;
    ArrayList<RECOBeaconRegion> rangingRegions;
    private ArrayList<HashMap<String, String>> cartList;
    private HashMap<String, String> beaconInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_check_out);
        //getID
        SharedPreferences preferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userid = preferences.getString(Config.USERID_SHARED_PREF, "no ID");

        listView = (ListView) findViewById(R.id.Orderlist);
        btnCheckout = (Button) findViewById(R.id.Btncheckout);
        spinner = (Spinner) findViewById(R.id.card_spinner);
        tvTotalPrice = (TextView) findViewById(R.id.ckout_totalPrice);

        new GetCardExecute().execute(userid);
        new getCartList().execute(userid);

        checkRegion = false;

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBeaconRegion();
                Log.i("check:", checkRegion + "");
                if(checkRegion) {
                    Intent intent = new Intent(CheckOutActivity.this, PasswdActivity.class);
                    Log.i("cid:",cardId.get(spinner.getSelectedItemPosition())+"");
                    intent.putExtra("cardId",cardId.get(spinner.getSelectedItemPosition()));
                    intent.putExtra("map", cartList);
                    intent.putExtra("userid", userid);
                    intent.putExtra("beaconInfo", beaconInfo);
                    startActivity(intent);
                } else {
                   showDialog();
                }
            }
        });

    }
    private void showDialog() {
        final Dialog dialog = new Dialog(CheckOutActivity.this);
        dialog.setContentView(R.layout.invalid_region_dialog);
        Button btnOk = (Button) dialog.findViewById(R.id.btnok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void checkBeaconRegion() {
        //현재위치 비콘이 경기장 비콘이 맞는지 확인
        for(RECOBeaconRegion region : rangingRegions) {
            try {
                recoManager.startRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Beacon Service start
        recoManager = RECOBeaconManager.getInstance(CheckOutActivity.this, false, true);
        recoManager.bind(CheckOutActivity.this);
        recoManager.setRangingListener(CheckOutActivity.this);
        rangingRegions = new ArrayList<RECOBeaconRegion>();
        //get beaconInfo;
        new getBeaconList().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        for(RECOBeaconRegion region : rangingRegions) {
            try {
                recoManager.stopRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                //RemoteException 발생 시 작성 코드
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
            }
        }
//        try {
//            recoManager.unbind();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onServiceConnect() {

    }

    @Override
    public void onServiceFail(RECOErrorCode recoErrorCode) {

    }

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> collection, RECOBeaconRegion recoBeaconRegion) {
        for(RECOBeacon beacon : collection) {
            Log.i("beacon id:", beacon.getProximityUuid() + "  " +beacon.getMinor() + "  " + beacon.getMajor());
            checkRegion = true;
            //TODO:여기서 비콘 정보 가져와 저장해서 디비에 저장
            beaconInfo = new HashMap<>();
            beaconInfo.put("uuid", beacon.getProximityUuid());
            beaconInfo.put("major", Integer.toString(beacon.getMajor()));
            beaconInfo.put("minor", Integer.toString(beacon.getMinor()));
        }
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {
    }

    private class getBeaconList extends AsyncTask<String, Void, String> {
        ArrayList<BeaconData> list;
        @Override
        protected String doInBackground(String... params) {
            GetData getData = new GetData();
            try {
                String response = getData.run(URL_GETBEACON);
                JSONObject json = new JSONObject(response);
                int success = json.getInt("success");
                if(success == 1) {
                    JSONArray items = json.getJSONArray("items");
                    list = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject beacon = items.getJSONObject(i);
                        list.add(new BeaconData(beacon.getInt("beaconNum"), beacon.getString("beaconID"), beacon.getString("beaconName"), beacon.getString("beaconMajor")
                                , beacon.getString("beaconMinor"), beacon.getString("installID")));
                    }
                    return "1";
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
            super.onPostExecute(s);
            if(s.equals("1")) {
                for (int i = 0; i < list.size(); i++) {
                    int major = Integer.parseInt(list.get(i).getBeaconMajor());
                    int minor = Integer.parseInt(list.get(i).getBeaconMinor());
                    rangingRegions.add(new RECOBeaconRegion(list.get(i).getBeaconID(), major, minor,
                            list.get(i).getBeaconName()));
                }
            }
        }
    }

    public class getCartList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String userid = params[0];
            Log.i("params:", userid);
            totalPrice = 0;
            PostData postData = new PostData();
            RequestBody formbody = new FormBody.Builder()
                    .add("userid", userid)
                    .build();

            try {
                String response = postData.run(URL_GETCARTLIST, formbody);
                JSONObject json = new JSONObject(response);
                Log.i("json in cartActivity:", json.toString());
                int success = json.getInt("success");
                if (success == 1) {
                    Log.i("success:", "1");
                    cartList = new ArrayList<>();
                    JSONArray items = json.getJSONArray("cartItems");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        //String cartId, String itemId, String itemName, int itemAmount, String userId
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("cartId", item.getString("cartId"));
                        hashMap.put("itemId", item.getString("itemId"));
                        hashMap.put("itemName", item.getString("itemName"));
                        hashMap.put("itemAmount", Integer.toString(item.getInt("itemAmount")));
                        int total = item.getInt("itemAmount") * item.getInt("itemPrice");
                        hashMap.put("itemPrice", Integer.toString(total));
                        totalPrice += total;
                        hashMap.put("userId", item.getString("userId"));
                        cartList.add(hashMap);
                    }
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
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            CheckOutActivity.this, cartList,
                            R.layout.order_check_item, new String[] {"itemName", "itemAmount", "itemPrice"},
                            new int[] { R.id.tvName, R.id.tvAmount, R.id.tvPrice});
                    // updating listview
                    listView.setAdapter(adapter);
                    //updating totalprice
                    tvTotalPrice.setText(Integer.toString(totalPrice) + "WON");
                }
            });
        }

    }

    public class GetCardExecute extends AsyncTask<String, Void, String> {
        private ArrayList<String> cards;
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
                    cards = new ArrayList<String>();
                    cardId = new ArrayList<Integer>();
                    for(int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        cardId.add(item.getInt("cardID"));
                        String card_spinner_item = item.getString("cardType") +"  "+item.getString("cardNum") + ",  " + item.getString("cardName");
                        cards.add(card_spinner_item);
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
            adapter = new ArrayAdapter(CheckOutActivity.this, android.R.layout.simple_spinner_item, cards);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }
}
