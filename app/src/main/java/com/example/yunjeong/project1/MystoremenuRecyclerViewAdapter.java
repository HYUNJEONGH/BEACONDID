package com.example.yunjeong.project1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yunjeong.project1.dummy.DummyContent.DummyItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link /OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MystoremenuRecyclerViewAdapter extends RecyclerView.Adapter<MystoremenuRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<ProductData> mValues;
    private final StoreMenuFragment.OnListFragmentInteractionListener mListener = null;
    View view;
    private LayoutInflater layoutInflater;
    String itemtogson;
    String userid;
    HashMap<String, String> cartItem;
    Gson gson;

    public MystoremenuRecyclerViewAdapter(ArrayList<ProductData> productitems) {
        mValues = productitems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        view  = layoutInflater.inflate(R.layout.fragment_storemenu, parent, false);
        //List initialize
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mIdView.setText(mValues.get(position).getProductName());
            holder.mContentView.setText(Integer.toString(mValues.get(position).getPrice()));
            Glide.with(holder.mView.getContext()).load(mValues.get(position).getImageurl()).into(holder.mImgView);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("item click:", "clicked" + position);
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(holder.mView.getContext());
                    View parentView = layoutInflater.inflate(R.layout.cart_footer, null);
                    bottomSheetDialog.setContentView(parentView);
                    bottomSheetDialog.show();
                    //cart_footer views
                    Button btnMinus = (Button)parentView.findViewById(R.id.btnMinus);
                    Button btnPlus = (Button)parentView.findViewById(R.id.btnPlus);
                    Button btnAddcart = (Button)parentView.findViewById(R.id.btnAddcart);
                    Button btnOrder = (Button)parentView.findViewById(R.id.btnOrder);
                    final TextView amount = (TextView)parentView.findViewById(R.id.tvAmount);
                    SharedPreferences preferences = holder.mView.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    userid = preferences.getString(Config.USERID_SHARED_PREF, "no ID");
                    btnMinus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("clicked","btnMinus");
                            int a = Integer.parseInt(amount.getText().toString());
                            if(a != 1){
                                a--;
                                amount.setText(String.valueOf(a));
                            }
                        }
                    });
                    btnPlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int Total = mValues.get(position).getAmount();
                            int a = Integer.parseInt(amount.getText().toString());
                            a++;
                            if(a > Total) {
                                Toast.makeText(holder.mView.getContext(), "재고가 부족 합니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                amount.setText(String.valueOf(a));
                            }
                        }
                    });
                    btnAddcart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cartItem = new HashMap<String, String>();
                            cartItem.put("id", mValues.get(position).getProductID());
                            cartItem.put("name", mValues.get(position).getProductName());
                            cartItem.put("amount", amount.getText().toString());
                            cartItem.put("userid", userid);
                            cartItem.put("price", Integer.toString(mValues.get(position).getPrice()));
                            gson = new Gson();
                            itemtogson = gson.toJson(cartItem);
                            // MAKE HTTPCONNECTION
                            Log.i("addcartb:", itemtogson.toString());
                            new AddCartExcute().execute(itemtogson);
                            bottomSheetDialog.dismiss();
                        }
                    });
                    btnOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cartItem = new HashMap<String, String>();
                            cartItem.put("id", mValues.get(position).getProductID());
                            cartItem.put("name", mValues.get(position).getProductName());
                            cartItem.put("amount", amount.getText().toString());
                            cartItem.put("userid", userid);
                            cartItem.put("price", Integer.toString(mValues.get(position).getPrice()));
                            gson = new Gson();
                            itemtogson = gson.toJson(cartItem);
                            Log.i("order btn:", itemtogson.toString());
                            new AddCartExcute().execute(itemtogson);
                            bottomSheetDialog.dismiss();
                            Intent intent = new Intent(holder.mView.getContext(), CartActivity.class);
                            holder.mView.getContext().startActivity(intent);
                        }
                    });

//                if (null != mListener) {
//
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
////                    mListener.onListFragmentInteraction(holder.getItemId());
//                }
                }
            });



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgView;
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImgView = (ImageView)view.findViewById(R.id.imgItem);
            mIdView = (TextView) view.findViewById(R.id.tvPname);
            mContentView = (TextView) view.findViewById(R.id.tvPprice);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
