package com.example.lrgeneratorapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    public interface OnListEmptyListener {
        void onListEmpty();
    }
    Context context;
    ArrayList<LRHistory> list;

    OnListEmptyListener listener;


    public HistoryAdapter(Context context, ArrayList<LRHistory> list, OnListEmptyListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lrNo, parties, route, date;
        ImageButton deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            lrNo = itemView.findViewById(R.id.tv_lrno);
            deleteBtn = itemView.findViewById(R.id.btn_delete);
            parties = itemView.findViewById(R.id.tv_parties);
            route = itemView.findViewById(R.id.tv_route);
            date = itemView.findViewById(R.id.tv_date);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LRHistory lr = list.get(position);

        holder.lrNo.setText("LR " + lr.lrNo);
        holder.parties.setText(lr.consignor + " → " + lr.consignee);
        holder.route.setText(lr.fromCity + " → " + lr.toCity);
        holder.date.setText(lr.date);

        holder.itemView.setOnClickListener(v -> {

            File file = new File(lr.pdfPath);

            if (file.exists()) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }
        });

        holder.deleteBtn.setOnClickListener(v -> {

            int adapterPosition = holder.getBindingAdapterPosition();

            if(adapterPosition != RecyclerView.NO_POSITION){

                DatabaseHelper db = new DatabaseHelper(context);

                db.deleteLR(lr.lrNo);

                list.remove(adapterPosition);

                notifyItemRemoved(adapterPosition);

                if(list.isEmpty()){
                    listener.onListEmpty();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}