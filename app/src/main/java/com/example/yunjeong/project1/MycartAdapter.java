package com.example.yunjeong.project1;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YunJeong on 2016-09-08.
 */
public class MycartAdapter extends RecyclerView.Adapter<MycartAdapter.ViewHolder> {
    private final ArrayList<CartItemData> mValues;
    private LayoutInflater layoutInflater;
    public static int total;

    public MycartAdapter(ArrayList<CartItemData> cartList) {
        Log.i("adapter cart;", cartList.size() + "s");
        mValues = cartList;
        total = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.activity_cart_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String cartId = mValues.get(position).getCartId();
        holder.mTvid.setText(cartId);
        holder.mTvname.setText(mValues.get(position).getItemName());
        holder.mTvAmount.setText(Integer.toString(mValues.get(position).getItemAmount()) + "개");
        final int price = (mValues.get(position).getItemPrice() * mValues.get(position).getItemAmount());
        holder.mTvPrice.setText(Integer.toString(price)  + "원");
        total += price;
        holder.mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteCartItem().execute(cartId);
                mValues.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mValues.size());
//                total -= price;
//                CartActivity.textView.setText("총 " + Integer.toString(total) + "원");
            }
        });
        Log.i("total price:", total+"원");
//        CartActivity.textView.setText("총 " + Integer.toString(total) + "원");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTvid;
        public final TextView mTvname;
        public final TextView mTvAmount;
        public final ImageButton mImgBtn;
        public final TextView mTvPrice;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvid = (TextView)view.findViewById(R.id.cart_id);
            mTvname = (TextView)view.findViewById(R.id.item_name);
            mTvAmount = (TextView)view.findViewById(R.id.item_amount);
            mImgBtn = (ImageButton)view.findViewById(R.id.imgbtn_Delete);
            mTvPrice = (TextView)view.findViewById(R.id.item_price);
        }

    }
}
