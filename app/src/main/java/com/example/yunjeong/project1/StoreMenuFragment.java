package com.example.yunjeong.project1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.yunjeong.project1.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StoreMenuFragment extends Fragment {

    private static final String ARG_FRAG = "fragment";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_PRODUCTID = "productID";
    private static final String TAG_STOREID = "storeID";
    private static final String TAG_PRODUCTNAME = "productName";
    private static final String TAG_PRICE = "price";
    private static final String TAG_IMAGEURL = "imageurl";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_BESTMENU = "BestMenu";
    private static final String URL_GETSTOREITEM = "http://14.63.196.137/app/get_Store_item.php";

    private int mColumnCount = 2;
    private Toolbar toolbar;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<ProductData> Productitems;
    private RecyclerView recyclerView;
    private MystoremenuRecyclerViewAdapter recyclerViewAdapter;
    private String mViewId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoreMenuFragment() {


    }

    public static StoreMenuFragment newInstance(String storeid) {
        StoreMenuFragment fragment = new StoreMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRAG, storeid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment onCreate","start");
        if (getArguments() != null) {
            Log.i("getargument:", "execute");
            mViewId = getArguments().getString(ARG_FRAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("fragment onCreateView","start");
        View view = inflater.inflate(R.layout.fragment_storemenu_list, container, false);

        toolbar = (Toolbar)view.findViewById(R.id.my_store_toolbar);
        toolbar.inflateMenu(R.menu.store_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cart:
                        Log.e("e", "cart click: intent from fragment to Cartactivity");
                        Intent intent = new Intent(getActivity(), CartActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.storemenu_list);

        Context context = view.getContext();
        //Set the Spinner
        final Spinner spinner = (Spinner)view.findViewById(R.id.cateSpinner);
        ArrayAdapter<CharSequence> Itemadapter = ArrayAdapter.createFromResource(context, R.array.store_array, android.R.layout.simple_spinner_item);
        Itemadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Itemadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        new getStoreItem().execute("0");
                        Log.i("position:", "0");
                        break;
                    case 2:
                        new getStoreItem().execute("S0001");
                        break;
                    case 3:
                        new getStoreItem().execute("S0002");
                        break;
                    case 4:
                        new getStoreItem().execute("S0003");
                        break;
                    case 5:
                        new getStoreItem().execute("S0004");
                        break;
                    case 6:
                        new getStoreItem().execute("S0005");
                        break;
                    case 7:
                        new getStoreItem().execute("S0006");
                        break;
                    case 8:
                        new getStoreItem().execute("S0007");
                        break;
                    case 9:
                        new getStoreItem().execute("S0008");
                        break;
                    case 10:
                        new getStoreItem().execute("S0009");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set the adapter
           if (mColumnCount <= 1) {
               recyclerView.setLayoutManager(new LinearLayoutManager(context));
           } else {
               Log.d("grid:", "grid view");
               recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
           }
        if(mViewId != null) {
            if(mViewId.equals("S0005")) {
                Log.i("fragment start;", "IN STOREFRAG");
                new getStoreItem().execute("S0005");
            }
        }

//        recyclerViewAdapter.notifyDataSetChanged();
        return view;
    }

    public void updateUserInterface() {
        recyclerViewAdapter = new MystoremenuRecyclerViewAdapter(Productitems);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
       void onListFragmentInteraction(DummyContent.DummyItem item);
    }

    public class getStoreItem extends AsyncTask<String, Void, String> {

        int success;

        @Override
        protected String doInBackground(String... params) {
            String cate = params[0];
            PostData postData = new PostData();
            RequestBody formbody = new FormBody.Builder()
                    .add("cate", cate)
                    .build();

            String responce = null;

            try {
                responce = postData.run(URL_GETSTOREITEM, formbody);
                JSONObject json = new JSONObject(responce);
                Log.d("get_store_item_json", json.toString());
                success = json.getInt(TAG_SUCCESS);
                Productitems = new ArrayList<ProductData>();
                if(success == 1) {
                    JSONArray items = json.getJSONArray(TAG_ITEMS);
                    for(int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Productitems.add(new ProductData(item.getString(TAG_PRODUCTID),item.getString(TAG_STOREID), item.getString(TAG_PRODUCTNAME),
                                item.getInt(TAG_PRICE), item.getString(TAG_IMAGEURL), item.getInt(TAG_AMOUNT), item.getInt(TAG_BESTMENU)));
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
        protected void onPostExecute(String result) {
            if (success == 0) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.storelinear), R.string.failed_get_item, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                updateUserInterface();
            }
        }
    }

}
