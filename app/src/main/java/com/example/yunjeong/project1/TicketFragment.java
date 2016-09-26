package com.example.yunjeong.project1;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_USERID = "userid";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TICKETS = "tickets";
    private static final String TAG_TICKETBOOKID = "ticketbookID";
    private static final String TAG_TICKETID = "ticketID";
    private static final String TAG_TICKETNAME = "ticketName";
    private static final String TAG_TICKETDATE = "ticketDate";
    private static final String TAG_SEATLOCATION = "seatlocation";
    private static final String TAG_STADIUMINFO = "stadiumInfo";

    private ArrayList<TicketData> ticketDatas;
    //array prepared
    private boolean arrayPrepare = false;

    private static String url_ticketList = "http://14.63.196.137/app/get_Ticket_List3.php";
    // TODO: Rename and change types of parameters
    private String mUserid;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;

    public TicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
  /*  public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
    public static TicketFragment newInstance(String mUserid) {
        TicketFragment fragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERID, mUserid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/koverwatch.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
        if (getArguments() != null) {
            mUserid = getArguments().getString(ARG_USERID);
            Log.v("mUserid in frag:", mUserid);
        }
        new TicketListCall().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        return view;
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    } */

    class TicketListCall extends AsyncTask<String, String, String> {
        TicketListAdapter adapter;
        ListView listView;
        @Override
        protected String doInBackground(String... params) {
            PostData postData = new PostData();
            RequestBody formbody = new FormBody.Builder()
                    .add("userid", mUserid)
                    .build();
            String response = null;
            try {
                response = postData.run(url_ticketList, formbody);
                JSONObject json = new JSONObject(response);
                Log.d("json ticket:", json.toString());
                int success = json.getInt(TAG_SUCCESS);
                if(success == 1) {
                    JSONArray tickets = json.getJSONArray(TAG_TICKETS);
                    ticketDatas = new ArrayList<TicketData>();
                    for(int i = 0; i < tickets.length(); i++) {
                        JSONObject t = tickets.getJSONObject(i);

                        ticketDatas.add(new TicketData(t.getString(TAG_TICKETBOOKID), t.getString(TAG_TICKETID),
                                t.getString(TAG_TICKETNAME), t.getString(TAG_TICKETDATE), t.getString(TAG_SEATLOCATION), t.getString(TAG_STADIUMINFO)));
                    }
                    Log.v("DATA SIZE:", ticketDatas.size() + "s");
                    adapter = new TicketListAdapter(getContext(), R.layout.fragment_ticket, ticketDatas);
                    listView = (ListView)getActivity().findViewById(R.id.lvTicket);
                    arrayPrepare = true;
                } else {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.ticketLayout), R.string.noTData ,Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return "0";
                }
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.equals("0")) {
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.ticket_detail_dialog);
                        TextView tvid = (TextView)dialog.findViewById(R.id.tvDTicketD);
                        TextView tvdate = (TextView)dialog.findViewById(R.id.tvDDate);
                        TextView tvname = (TextView)dialog.findViewById(R.id.tvDName);
                        TextView tvloc = (TextView)dialog.findViewById(R.id.tvDLocation);
                        TextView tvseat = (TextView)dialog.findViewById(R.id.tvDSeatInfo);
                        Button btnclose = (Button)dialog.findViewById(R.id.btn_close);
                        Button btnSearchSeat = (Button)dialog.findViewById(R.id.btn_search_seat);
                        btnSearchSeat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), SearchSeatActivity.class);
                                startActivity(intent);
                            }
                        });
                        btnclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        tvid.setText(ticketDatas.get(position).getTicketID());
                        tvname.setText(ticketDatas.get(position).getTicketName());
                        tvdate.setText(ticketDatas.get(position).getTicketDate());
                        tvloc.setText(ticketDatas.get(position).getStadiumInfo());
                        tvseat.setText(ticketDatas.get(position).getSeatlocation());
                        dialog.show();
                    }
                });
            }
        }

    }
}
