package com.example.ausgabenliste;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenditureListAdapter extends RecyclerView.Adapter<ExpenditureListAdapter.ExpenditureListViewHolder> {
    private ArrayList<ExpenditureList> expenditureListsOverview;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener tempListener) {
        listener = tempListener;
    }

    public static class ExpenditureListViewHolder extends RecyclerView.ViewHolder {
        public TextView tvExpenditureListName;
        public TextView tvDeleteList;

        public ExpenditureListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvExpenditureListName = itemView.findViewById(R.id.tvExpenditureListName);
            tvDeleteList = itemView.findViewById(R.id.tvDeleteList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ExpenditureListAdapter", "In onClick itemView");
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            tvDeleteList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ExpenditureListAdapter", "In onClick tvDeleteList");
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public ExpenditureListAdapter(ArrayList<ExpenditureList> expenditureListsOverview) {
        this.expenditureListsOverview = expenditureListsOverview;
    }

    @Override
    public ExpenditureListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_overview_item, parent, false);
        ExpenditureListViewHolder viewHolder = new ExpenditureListViewHolder(v, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenditureListAdapter.ExpenditureListViewHolder holder, int position) {
        ExpenditureList current = expenditureListsOverview.get(position);

        holder.tvExpenditureListName.setText(current.getListName());
    }

    @Override
    public int getItemCount() {
        return expenditureListsOverview.size();
    }
}
