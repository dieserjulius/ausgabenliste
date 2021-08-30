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

    /**
     * Interface für die Klicks auf die jeweilige Liste und den "Löschen"-Text
     */

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    /**
     * Setzt einen Listener als "listener"-Variable
     * @param tempListener Listener, der gesetzt werden soll
     */

    public void setOnItemClickListener(OnItemClickListener tempListener) {
        listener = tempListener;
    }

    /**
     * ViewHolder für die RecyclerView
     */

    public static class ExpenditureListViewHolder extends RecyclerView.ViewHolder {
        public TextView tvExpenditureListName;
        public TextView tvDeleteList;

        /**
         * ViewHolder für die ExpenditureListOverview
         * @param itemView
         * @param listener Listener aus dem Interface von weiter oben
         */

        public ExpenditureListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvExpenditureListName = itemView.findViewById(R.id.tvEntryName);
            tvDeleteList = itemView.findViewById(R.id.tvEntryAmount);

            // Klick auf ein Item
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

            // Klick auf den "Löschen"-text
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

    /**
     * Konstruktor
     * @param expenditureListsOverview Overview für die verschiedenen ExpenditureLists
     */

    public ExpenditureListAdapter(ArrayList<ExpenditureList> expenditureListsOverview) {
        this.expenditureListsOverview = expenditureListsOverview;
    }

    /**
     * Vollendet die View, indem sie mit den einzelnen Items gefüllt wird
     * @param parent
     * @param viewType
     * @return ViewHolder
     */

    @Override
    public ExpenditureListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // list_overvire_items werden in den ViewHolder gefüllt
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_overview_item, parent, false);
        ExpenditureListViewHolder viewHolder = new ExpenditureListViewHolder(v, listener);
        return viewHolder;
    }

    /**
     * Überträgt die Daten der aktuellen List auf den ViewHolder
     * @param holder
     * @param position Position des Items
     */

    @Override
    public void onBindViewHolder(ExpenditureListAdapter.ExpenditureListViewHolder holder, int position) {
        ExpenditureList current = expenditureListsOverview.get(position);
        holder.tvExpenditureListName.setText(current.getListName());
    }

    /**
     * Gibt die Anzahl der Items zurück
     * @return Anzahl der Items
     */

    @Override
    public int getItemCount() {
        return expenditureListsOverview.size();
    }
}
