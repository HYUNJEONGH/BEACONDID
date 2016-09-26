package com.example.yunjeong.project1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderListFragment./OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String URLGETORDERLIST = "http://14.63.196.137/app/get_Order_List.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_PAYINFOID = "payInfoID";
    private static final String TAG_CARDID = "cardID";
    private static final String TAG_PAYDATE = "payDate";
    private static final String TAG_PAYAMOUNT = "payAmount";
    private static final String TAG_PRODUCTNAME = "productName";

    private ListView lvOrder;
    private String userid;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<HashMap<String, String>> orderList;
//    private OnFragmentInteractionListener mListener;

    public OrderListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderListFragment newInstance(String param1, String param2) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userid = preferences.getString(Config.USERID_SHARED_PREF, "no ID");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        lvOrder = (ListView)view.findViewById(R.id.lvOrder);
        new LoadOrderInfo().execute(userid);
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    class LoadOrderInfo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orderList = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected String doInBackground(String... params) {
            PostData postData = new PostData();
            String id = params[0];
            String responce = null;
            try {
                RequestBody formbody = new FormBody.Builder()
                        .add("userid", id)
                        .build();

                responce = postData.run(URLGETORDERLIST, formbody);
                JSONObject json = new JSONObject(responce);
                Log.d("data", json.toString());

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    JSONArray items = json.getJSONArray(TAG_ITEMS);
//                    | payInfoID | cardID | payDate             | payAmount | productName            |
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);
                        String payInfoID = c.getString(TAG_PAYINFOID);
                        String cardID = Integer.toString(c.getInt(TAG_CARDID));
                        String payDate = c.getString(TAG_PAYDATE);
                        String payAmount = Integer.toString(c.getInt(TAG_PAYAMOUNT));
                        String productName = c.getString(TAG_PRODUCTNAME);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("cardid", cardID);
                        map.put("payinfo", payInfoID);
                        map.put("paydate", payDate);
                        map.put("payamount", payAmount);
                        map.put("productName", productName);
                        orderList.add(map);
                    }
                } else {
                    Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(), orderList,
                            R.layout.orderinfo_item, new String[] {"paydate", "productName", "payamount"},
                            new int[] { R.id.tvPayDate, R.id.tvPayContent, R.id.tvPayAmount});
                    // updating listview
                    lvOrder.setAdapter(adapter);
                }
            });

        }
    }
}
