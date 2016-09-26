package com.example.yunjeong.project1;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cooltechworks.creditcarddesign.CreditCardView;

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
 * {link HomeFragment. OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static String URL_GETCARD = "http://14.63.196.137/app/get_Card_List.php";
    private static String URL_GETBOARD = "http://14.63.196.137/app/get_board.php";
    private ArrayList<CardData> cards;
    private String mParam1;
    private ViewPager mPager;
    private ImageView cardPlus;
    private ImageView cardMinus;
    private RecyclerView mRecyclerView;
//    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        new GetCardExecute().execute(mParam1);
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        cardPlus = (ImageView)view.findViewById(R.id.card_plus);
        cardMinus = (ImageView)view.findViewById(R.id.card_minus);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new GetBoardExecute().execute();
        cardPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AddCardActivity.class);
                        startActivity(intent);
            }
        });
        cardMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteCardActivity.class);
                intent.putExtra("userid", mParam1);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPager = (ViewPager) view.findViewById(R.id.card_viewPager);
    }
    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
    public class CardpagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return cards.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CreditCardView creditCardView = null;
            creditCardView = new CreditCardView(getContext());
            creditCardView.setCVV(Integer.toString(cards.get(position).getCardCVCNum()));
            creditCardView.setCardHolderName(cards.get(position).getCardName());
            creditCardView.setCardExpiry(cards.get(position).getCardExpiration());
            String cardNum = cards.get(position).getCardNum();
            creditCardView.setCardNumber(cardNum);
            Log.i("card:", position + "번쨰" + cards.get(position).getCardNum());
            ((ViewPager)container).addView(creditCardView,0);
            return creditCardView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public class GetCardExecute extends AsyncTask<String, Void, String> {
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
                    cards = new ArrayList<CardData>();
                    for(int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        cards.add(new CardData(item.getInt("cardID"), item.getString("cardNum"), item.getString("cardName"), item.getString("userID"), item.getString("cardExpiration"),
                                item.getString("cardPassword"), item.getInt("cardCVCNum"), item.getString("cardType")));
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
            CardpagerAdapter cardpagerAdapter = new CardpagerAdapter();
            mPager.setAdapter(cardpagerAdapter);
        }
    }

    public class GetBoardExecute extends AsyncTask<String, Void, Void> {
        ArrayList<BoardData> boards = new ArrayList<>();
        @Override
        protected Void doInBackground(String... params) {
            GetData getData = new GetData();

            try {
                String response = getData.run(URL_GETBOARD);
                JSONObject json = new JSONObject(response);
                String success = json.getString("success");
                if(success.equals("1")) {
                    JSONArray items = json.getJSONArray("Boards");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        boards.add(new BoardData(item.getString("bId"), item.getString("imgUrl"), item.getString("Info")));
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setAdapter(new MyBoardAdapter(boards));
        }
    }
}
