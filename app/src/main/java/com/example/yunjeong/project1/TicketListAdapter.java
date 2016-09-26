package com.example.yunjeong.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YunJeong on 2016-08-31.
 */
public class TicketListAdapter extends BaseAdapter {
    private ArrayList<TicketData> ticketDatas;
    private Context context;
    private int layout;
    private LayoutInflater inflater;

    public TicketListAdapter(Context context, int layout, ArrayList<TicketData> ticketDatas) {
        super();
        this.context = context;
        this.layout = layout;
        this.ticketDatas = ticketDatas;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ticketDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.ticket_list_item, parent, false);
        }
        TextView Bookid = (TextView)convertView.findViewById(R.id.tvBookId);
        TextView Tid = (TextView)convertView.findViewById(R.id.tvTId);
        TextView name = (TextView)convertView.findViewById(R.id.tvTicketName);
        TextView date = (TextView)convertView.findViewById(R.id.tvTicketD);

        Bookid.setText(ticketDatas.get(pos).getTicketbookID());
        Tid.setText(ticketDatas.get(pos).getTicketID());
        name.setText(ticketDatas.get(pos).getTicketName());
        date.setText(ticketDatas.get(pos).getTicketDate());

        return convertView;
    }
}
