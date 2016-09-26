package com.example.yunjeong.project1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UpdateListActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> appinfosList;
    ListView listView;
    // url to get all products list
    private static String url_updateinfo = "http://14.63.196.137/app/get_Update_info.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_APPINFOS = "appInfos";
    private static final String TAG_APPUDAY = "appUday";
    private static final String TAG_APPUVERSION = "appUVersion";
    private static final String TAG_APPUINFO = "appUInfo";
    private static final String TAG_DEVELOPERNAME = "developername";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_update_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_update_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Update Info");
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        new LoadAllProducts().execute();
        listView = (ListView)findViewById(R.id.list);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllProducts extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateListActivity.this);
            pDialog.setMessage("Loading Update Info. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            appinfosList = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected String doInBackground(String... params) {

            GetData getData = new GetData();
            String responce = null;
            try {
                responce = getData.run(url_updateinfo);
                JSONObject json = new JSONObject(responce);
                Log.d("data", json.toString());

                int success = json.getInt(TAG_SUCCESS);
                Log.d("success:", success + ",");
                if (success == 1) {
                    JSONArray appinfos = json.getJSONArray(TAG_APPINFOS);
//appInfoID | appUday    | appUVersion | appUInfo     | developername
                    for (int i = 0; i < appinfos.length(); i++) {
                        JSONObject c = appinfos.getJSONObject(i);
                        String info = c.getString(TAG_APPUDAY) + "\n";
                        info += c.getString(TAG_APPUINFO) + "\n";
                        info += c.getString(TAG_DEVELOPERNAME) + "\n";
                        String v = c.getString(TAG_APPUVERSION);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("version", v);
                        map.put("info", info);
                        appinfosList.add(map);
                    }
                } else {
                    Toast.makeText(UpdateListActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }



        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            UpdateListActivity.this, appinfosList,
                            R.layout.update_item, new String[] {"version", "info"},
                            new int[] { R.id.tvAppV, R.id.tvAppInfo});
                    // updating listview
                    listView.setAdapter(adapter);
                }
            });

        }
    }
}
