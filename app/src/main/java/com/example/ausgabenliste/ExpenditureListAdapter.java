package com.example.ausgabenliste;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenditureListAdapter extends RecyclerView.Adapter<ExpenditureListAdapter.ExpenditureListViewHolder> {
    private ArrayList<ExpenditureList> expenditureListsOverview;

    public static class ExpenditureListViewHolder extends RecyclerView.ViewHolder {
        public TextView tvExpenditureListName;

        public ExpenditureListViewHolder(View itemView) {
            super(itemView);
            tvExpenditureListName = itemView.findViewById(R.id.tvExpenditureListName);
        }
    }

    public ExpenditureListAdapter(ArrayList<ExpenditureList> expenditureListsOverview) {
        this.expenditureListsOverview = expenditureListsOverview;
    }

    @Override
    public ExpenditureListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_overview_item, parent, false);
        ExpenditureListViewHolder viewHolder = new ExpenditureListViewHolder(v);
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
