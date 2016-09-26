package com.example.yunjeong.project1;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link /UserFragment./OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements TabHost.OnTabChangeListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PAYINFO_SPEC = "payinfo";
    private static final String TICKETINFO_SPEC = "ticketinfo";
    private String mParam1;
    private String mParam2;
    private Toolbar toolbar;
    private String userid;
    private String username;
    private String useremail;
    private FragmentTabHost tabHost;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userid = "@";
        userid += preferences.getString(Config.USERID_SHARED_PREF, "no ID");
        username = preferences.getString(Config.USERNAME_SHARED_PREF, "no NAME");
        useremail = preferences.getString(Config.USEREMAIL_SHARED_PREF, "no EMAIL");

        Log.d("Userfrag id,name,email:", userid +"/"+username+"/"+useremail);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.imgUserImg);
        Glide.with(this).load("http://14.63.196.137/img/user.png").into(imageView);
        TextView tvname = (TextView)view.findViewById(R.id.tvUserfname);
        TextView tvid = (TextView)view.findViewById(R.id.tvUserfid);
        tvname.setText(username);
        tvid.setText(userid);

        toolbar = (Toolbar)view.findViewById(R.id.my_user_toolbar);
        toolbar.inflateMenu(R.menu.user_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e("e", "item click: intent from fragment to activity");
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                return true;
            }
        });

        tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(TICKETINFO_SPEC).setIndicator("티켓결제내역"),  PayListFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(PAYINFO_SPEC).setIndicator("음식점결제내역"), OrderListFragment.class, null);
        tabHost.setCurrentTab(0);
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {

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
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */
}
