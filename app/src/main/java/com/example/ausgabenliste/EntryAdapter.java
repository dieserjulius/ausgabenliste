package com.example.ausgabenliste;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {
    private ArrayList<Entry> entryList;
    private OnItemClickListener listener;



    public EntryAdapter(ArrayList<Entry> entryList) {
        this.entryList = entryList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener tempListener) {
        listener = tempListener;
    }

    public static class EntryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEntryName;
        public TextView tvAmount;

        /**
         * ViewHolder für die ExpenditureListOverview
         * @param itemView
         * @param listener Listener aus dem Interface von weiter oben
         */

        public EntryViewHolder(View itemView, final EntryAdapter.OnItemClickListener listener) {
            super(itemView);
            tvEntryName = itemView.findViewById(R.id.tvEntryNameView);
            tvAmount = itemView.findViewById(R.id.tvAmountView);

            // Klick auf ein Item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("EntryAdapter", "In onClick itemView");
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        EntryViewHolder viewHolder = new EntryViewHolder(v, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EntryAdapter.EntryViewHolder holder, int position) {
        Entry current = entryList.get(position);
        holder.tvEntryName.setText(current.getEntryName());
        holder.tvAmount.setText(current.getAmountAsString(true));
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

}
