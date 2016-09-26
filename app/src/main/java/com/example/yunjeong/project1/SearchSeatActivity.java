package com.example.yunjeong.project1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchSeatActivity extends AppCompatActivity implements RECOServiceConnectListener, RECORangingListener, RECOMonitoringListener {

    boolean mScanRecoOnly = false;
    boolean mEnableBackgroundTimeout = true;
    private static final String URL_GETBEACON = "http://14.63.196.137/app/get_beacon_list.php";
    RECOBeaconManager recoManager;
    private ImageView imgDirection;
    private Button btnStop;
    protected ArrayList<RECOBeaconRegion> mRegions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_search_seat);
        imgDirection = (ImageView)findViewById(R.id.imgDirection);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(mRegions);
                finish();
            }
        });
        mRegions = new ArrayList<>();
        recoManager = RECOBeaconManager.getInstance(this, mScanRecoOnly, mEnableBackgroundTimeout);
        recoManager.setDiscontinuousScan(true);
        recoManager.setRangingListener(this);
        recoManager.setMonitoringListener(this);
        recoManager.bind(this);
        new getBeaconList().execute();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void onResume() {
        super.onResume();
        this.start(mRegions);
    }

    protected void onPause() {
        super.onPause();
        this.stop(mRegions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stop(mRegions);
        this.unbind();
    }

    private void unbind() {
        try {
            recoManager.unbind();
        } catch (RemoteException e) {
            Log.i("RECORangingActivity", "Remote Exception");
            e.printStackTrace();
        }
    }

    protected void start(ArrayList<RECOBeaconRegion> regions) {

        for(RECOBeaconRegion region : regions) {
            try {
                recoManager.startRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    protected void stop(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                recoManager.stopRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    double getDistance(int rssi, int txPower) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }

    @Override
    public void onServiceConnect() {
        Toast.makeText(this, "Service Connect", Toast.LENGTH_SHORT).show();
        this.start(mRegions);
    }

    @Override
    public void onServiceFail(RECOErrorCode recoErrorCode) {
        Toast.makeText(this, "Service Fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> collection, RECOBeaconRegion recoRegion) {
        Log.i("RECORangingActivity", "didRangeBeaconsInRegion() region: " + recoRegion.getUniqueIdentifier() +
               "major"+ recoRegion.getMajor()+"minor"+recoRegion.getMinor() +", number of beacons ranged: " + collection.size());
        for(RECOBeacon beacon : collection) {
            double distance =getDistance(beacon.getRssi(), beacon.getTxPower());
            Log.i("distance:", beacon.getProximityUuid() + "minor:"+ beacon.getMinor() + "major:" +beacon.getMajor() +" distance:" + distance);
            if(beacon.getProximityUuid().equals("E2C56DB5DFFB48D2B060D0F5A71096E0")) {
                Log.i("search seat:", "enter the block1");
                if(beacon.getMajor() == 30001) {
                    if(beacon.getMinor() == 10077) {
                        double distan = getDistance(beacon.getRssi(), beacon.getTxPower());
                        if(distan < 6.0) {
                            Glide.with(this).load("http://14.63.196.137/img/direction.png").into(imgDirection);
                        }
                    } else if(beacon.getMinor() == 10076) {
                        double distan = getDistance(beacon.getRssi(), beacon.getTxPower());
                        if(distan < 1.0) {
                            Glide.with(this).load("http://14.63.196.137/img/direction.png").into(imgDirection);
                        }
                    } else if(beacon.getMinor() == 10075) {
                        double distan = getDistance(beacon.getRssi(), beacon.getTxPower());
                        if(distan < 1.0) {
                            Glide.with(this).load("http://14.63.196.137/img/arrival.png").into(imgDirection);
                            Glide.with(this).load("http://14.63.196.137/img/right.png").into(imgDirection);
                        }
                    } else if(beacon.getMinor() == 10074) {
                        double distan = getDistance(beacon.getRssi(), beacon.getTxPower());
                        if(distan < 2.0) {
                            Toast.makeText(this, "경로 안내를 종료합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {

    }

    @Override
    public void didEnterRegion(RECOBeaconRegion beaconRegion, Collection<RECOBeacon> beacons) {
        for(RECOBeacon beacon : beacons) {
            if(beacon.getMajor() == 30001 && beacon.getMinor() == 10077) {
                Toast.makeText(this, "block1-1 enter region", Toast.LENGTH_SHORT).show();
            } else if(beacon.getMajor() == 30001 && beacon.getMinor() == 10076) {
                Toast.makeText(this, "block1-2 enter region", Toast.LENGTH_SHORT).show();
            } else if(beacon.getMajor() == 30001 && beacon.getMinor() == 10075) {
                Toast.makeText(this, "block1-3 enter region", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void didExitRegion(RECOBeaconRegion recoBeaconRegion) {

    }

    @Override
    public void didStartMonitoringForRegion(RECOBeaconRegion recoBeaconRegion) {

    }

    @Override
    public void didDetermineStateForRegion(RECOBeaconRegionState recoBeaconRegionState, RECOBeaconRegion recoBeaconRegion) {

    }

    @Override
    public void monitoringDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {

    }

    private class getBeaconList extends AsyncTask<String, Void, String> {
        ArrayList<BeaconData> list;
        @Override
        protected String doInBackground(String... params) {
            GetData getData = new GetData();
            try {
                String response = getData.run(URL_GETBEACON);
                JSONObject json = new JSONObject(response);
                Log.i("json beacon:", json.toString());
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
//                    int major = Integer.parseInt(list.get(i).getBeaconMajor());
//                    int minor = Integer.parseInt(list.get(i).getBeaconMinor());
                    mRegions.add(new RECOBeaconRegion(list.get(i).getBeaconID(),
                            list.get(i).getBeaconName()));
                }
            }
        }
    }

}