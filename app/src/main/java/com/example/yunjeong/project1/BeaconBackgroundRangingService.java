package com.example.yunjeong.project1;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECOProximity;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by YunJeong on 2016-09-11.
 */
public class BeaconBackgroundRangingService extends Service implements RECORangingListener, RECOMonitoringListener, RECOServiceConnectListener{

    private long mScanDuration = 1*1000L;
    private long mSleepDuration = 10*1000L;
    private long mRegionExpirationTime = 60*1000L;
    private int mNotificationID = 9999;
    public static final String RECO_UUID = "24DDF411-8CF1-440C-87CD-E368DAF9C933";
    private static final String URL_GETBESTMENU = "http://14.63.196.137/app/get_Best_Menu.php";
    private RECOBeaconManager mRecoManager;
    private ArrayList<RECOBeaconRegion> mRegions;
    private ArrayList<HashMap<String, String>> Bestitems;

        @Override
    public void onCreate() {
        Log.i("BackRangingService", "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BackRangingService", "onStartCommand");
        mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), false, true);
        mRecoManager.setDiscontinuousScan(true);
        this.bindRECOService();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("BackRangingService", "onDestroy()");
        this.tearDown();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("BackRangingService", "onTaskRemoved()");
        super.onTaskRemoved(rootIntent);
    }

    private void bindRECOService() {
        Log.i("BackRangingService", "bindRECOService()");

        mRegions = new ArrayList<RECOBeaconRegion>();
        this.generateBeaconRegion();

        mRecoManager.setMonitoringListener(this);
        mRecoManager.setRangingListener(this);
        mRecoManager.bind(this);
    }

    private void generateBeaconRegion() {
        Log.i("BackRangingService", "generateBeaconRegion()");

        RECOBeaconRegion recoRegion;

//        recoRegion = new RECOBeaconRegion(RECO_UUID, "RECO_01");
//        recoRegion.setRegionExpirationTimeMillis(this.mRegionExpirationTime);
//        mRegions.add(recoRegion);
        recoRegion = new RECOBeaconRegion("24DDF411-8CF1-440C-87CD-E368DAF9C93E", "RECO_02");
//        recoRegion.setRegionExpirationTimeMillis(this.mRegionExpirationTime);
        mRegions.add(recoRegion);
//        recoRegion = new RECOBeaconRegion("74278BDA-B644-4520-8F0C-720EAF059935", "MINIBEACON");
//


    }
    private void startMonitoring() {
        Log.i("BackRangingService", "startMonitoring()");

        mRecoManager.setScanPeriod(this.mScanDuration);
        mRecoManager.setSleepPeriod(this.mSleepDuration);

        for(RECOBeaconRegion region : mRegions) {
            try {
                mRecoManager.startMonitoringForRegion(region);
            } catch (RemoteException e) {
                Log.e("BackRangingService", "RemoteException has occured while executing RECOManager.startMonitoringForRegion()");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.e("BackRangingService", "NullPointerException has occured while executing RECOManager.startMonitoringForRegion()");
                e.printStackTrace();
            }
        }
    }

    private void stopMonitoring() {
        Log.i("BackRangingService", "stopMonitoring()");

        for(RECOBeaconRegion region : mRegions) {
            try {
                mRecoManager.stopMonitoringForRegion(region);
            } catch (RemoteException e) {
                Log.e("BackRangingService", "RemoteException has occured while executing RECOManager.stopMonitoringForRegion()");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.e("BackRangingService", "NullPointerException has occured while executing RECOManager.stopMonitoringForRegion()");
                e.printStackTrace();
            }
        }
    }

    private void startRangingWithRegion(RECOBeaconRegion region) {
        Log.i("BackRangingService", "startRangingWithRegion()");

        try {
            mRecoManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.e("BackRangingService", "RemoteException has occured while executing RECOManager.startRangingBeaconsInRegion()");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("BackRangingService", "NullPointerException has occured while executing RECOManager.startRangingBeaconsInRegion()");
            e.printStackTrace();
        }
    }

    private void stopRangingWithRegion(RECOBeaconRegion region) {
        Log.i("BackRangingService", "stopRangingWithRegion()");

        try {
            mRecoManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.e("BackRangingService", "RemoteException has occured while executing RECOManager.stopRangingBeaconsInRegion()");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("BackRangingService", "NullPointerException has occured while executing RECOManager.stopRangingBeaconsInRegion()");
            e.printStackTrace();
        }
    }

    private void tearDown() {
        Log.i("BackRangingService", "tearDown()");
        this.stopMonitoring();

        try {
            mRecoManager.unbind();
        } catch (RemoteException e) {
            Log.e("BackRangingService", "RemoteException has occured while executing unbind()");
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnect() {
        Log.i("BackRangingService", "onServiceConnect()");
        this.startMonitoring();
        //Write the code when RECOBeaconManager is bound to RECOBeaconService
    }

    @Override
    public void didDetermineStateForRegion(RECOBeaconRegionState state, RECOBeaconRegion region) {
        Log.i("BackRangingService", "didDetermineStateForRegion()" + region.getUniqueIdentifier() + ":" + state.toString());
        //Write the code when the state of the monitored region is changed
    }

    @Override
    public void didEnterRegion(RECOBeaconRegion region, Collection<RECOBeacon> beacons) {
        Log.i("BackRangingService", "didEnterRegion() - " + region.getUniqueIdentifier());
        //Get the region and found beacon list in the entered region
        for(RECOBeacon beacon : beacons) {
//            if(beacon.getProximity() == RECOProximity.RECOProximityImmediate) {
                if(beacon.getMinor() == 22004) {
                    Log.i("beacon is Near:", beacon.getProximityUuid());
                    new getBestMenu().execute("S0005", "삼겹살");
//                    this.popupNotification("Inside of 삼겹살" + region.getUniqueIdentifier(), "S0005");
                }
//            }
        }


        //Write the code when the device is enter the region

        this.startRangingWithRegion(region); //start ranging to get beacons inside of the region
        //from now, stop ranging after 10 seconds if the device is not exited
    }

    @Override
    public void didExitRegion(RECOBeaconRegion region) {
        Log.i("BackRangingService", "didExitRegion() - " + region.getUniqueIdentifier());
//        this.popupNotification("Outside of " + region.getUniqueIdentifier());
        //Write the code when the device is exit the region

        this.stopRangingWithRegion(region); //stop ranging because the device is outside of the region from now

    }

    @Override
    public void didStartMonitoringForRegion(RECOBeaconRegion region) {
        Log.i("BackRangingService", "didStartMonitoringForRegion() - " + region.getUniqueIdentifier());
        //Write the code when starting monitoring the region is started successfully
        Toast.makeText(this, "did start monitoring", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> beacons, RECOBeaconRegion region) {
        Log.i("BackRangingService", "didRangeBeaconsInRegion() - " + region.getUniqueIdentifier() + " with " + beacons.size() + " beacons");
        //Write the code when the beacons inside of the region is received
    }

    private void popupNotification(ArrayList<HashMap<String, String>> arrayList, String storeid, String storeName) {
        Log.i("BackRangingService", "popupNotification()");
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.KOREA).format(new Date());
        Intent intent = new Intent(this, BottombarActivity.class);
        intent.putExtra("storeid", storeid);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 ,intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setSmallIcon(R.drawable.shop25);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        builder.setStyle(inboxStyle.setBigContentTitle(storeName+ "의" + " Best Menu를 추천합니다." )
                .addLine(arrayList.get(0).get("name") + "," + arrayList.get(0).get("price") + "원")
                .addLine(arrayList.get(1).get("name") + "," + arrayList.get(1).get("price") + "원")
                .setSummaryText("클릭시 메뉴 화면으로 넘어 갑니다."))
                .setVibrate(new long[]{0,2000})
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        nm.notify(mNotificationID, builder.build());
        mNotificationID = (mNotificationID - 1) % 1000 + 9000;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //This method is not used
        return null;
    }

    @Override
    public void onServiceFail(RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed.
        //See the RECOErrorCode in the documents.
        Log.i("servicefail:", errorCode.toString());
        return;
    }

    @Override
    public void monitoringDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed to monitor the region.
        //See the RECOErrorCode in the documents.
        return;
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed to range beacons in the region.
        //See the RECOErrorCode in the documents.
        return;
    }

    public class getBestMenu extends AsyncTask<String, Void, String>{
        String storeid;
        String storeName;
        @Override
        protected String doInBackground(String... params) {
            storeid = params[0];
            storeName = params[1];
            PostData postData = new PostData();
            RequestBody formbody = new FormBody.Builder()
                    .add("storeid", storeid)
                    .build();
            String response = null;
            Bestitems = new ArrayList<>();
            try {
                response = postData.run(URL_GETBESTMENU, formbody);
                JSONObject json = new JSONObject(response);
                int success = json.getInt("success");
                if(success == 1) {
                    JSONArray array = json.getJSONArray("BestMenus");
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("name", item.getString("Bname"));
                        hashMap.put("price", Integer.toString(item.getInt("Bprice")));
                        Bestitems.add(hashMap);
                    }
                } else {
                    Log.i("no data:", "best menu is no exist");
                }
                Log.i("response:", response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            popupNotification(Bestitems , storeid, storeName);
        }
    }

}
