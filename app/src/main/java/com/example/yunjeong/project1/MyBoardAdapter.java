package com.example.yunjeong.project1;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by YunJeong on 2016-09-25.
 */
public class MyBoardAdapter extends RecyclerView.Adapter<MyBoardAdapter.ViewHolder>  {
    private LayoutInflater layoutInflater;
    private ArrayList<BoardData> boardDatas;

    public MyBoardAdapter(ArrayList<BoardData> boardDatas) {
        this.boardDatas = boardDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.board_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyBoardAdapter.ViewHolder holder, int position) {
        Glide.with(holder.mView.getContext()).load(boardDatas.get(position).getImgUrl()).into(holder.mImgboard);
        holder.mTvboard.setText(boardDatas.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return boardDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTvboard;
        public final ImageView mImgboard;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvboard = (TextView)view.findViewById(R.id.tvboard);
            mImgboard = (ImageView)view.findViewById(R.id.img_board);
        }
    }

}
