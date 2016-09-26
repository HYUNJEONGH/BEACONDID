package com.example.yunjeong.project1;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YunJeong on 2016-09-18.
 */
public class MycardAdapter extends RecyclerView.Adapter<MycardAdapter.ViewHolder> {
    private final ArrayList<CardData> mValues;
    private LayoutInflater layoutInflater;

    public MycardAdapter(ArrayList<CardData> datas) {
        mValues = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.activity_card_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i("holder:", mValues.size()+"");
        holder.mTvtype.setText(mValues.get(position).getCardType());
        holder.mTvname.setText(mValues.get(position).getCardName() +"님의 카드");
        holder.mTvnum.setText(mValues.get(position).getCardNum());

        holder.mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(holder.mView.getContext());
                dialog.setContentView(R.layout.card_delete_dialog);
                Button btnOk = (Button) dialog.findViewById(R.id.btnok);
                Button btnCancle = (Button) dialog.findViewById(R.id.btncancle);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete작업 수행
                        new DeleteCardExecute().execute(Integer.toString(mValues.get(position).getCardID()));
                        dialog.dismiss();
                        //startactivity
                        Intent intent = new Intent(holder.mView.getContext(), BottombarActivity.class);
                        holder.mView.getContext().startActivity(intent);
                    }
                });
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTvname;
        public final TextView mTvtype;
        public final TextView mTvnum;
        public final Button mBtn;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvname = (TextView) view.findViewById(R.id.tvCardName);
            mTvtype = (TextView) view.findViewById(R.id.tvCardType);
            mTvnum = (TextView) view.findViewById(R.id.tvCardNum);
            mBtn = (Button) view.findViewById(R.id.btn_delete_card);
        }

    }
}
