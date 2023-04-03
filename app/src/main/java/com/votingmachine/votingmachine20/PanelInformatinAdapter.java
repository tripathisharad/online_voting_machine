package com.votingmachine.votingmachine20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PanelInformatinAdapter extends RecyclerView.Adapter<PanelInformatinAdapter.MviewHolder> {


    private ArrayList<String> currentList2;
    private Context context2;
    private final OnItemClickListener listener;

    public PanelInformatinAdapter(ArrayList<String> currentList2, Context context2, OnItemClickListener listener) {
        this.currentList2 = currentList2;
        this.context2 = context2;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context2).inflate(R.layout.panel_info_adap_xml,parent,false);
        return new MviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MviewHolder holder, int position) {

          holder.panelName.setText(currentList2.get(position));
          holder.panelName.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  listener.onItemClick(holder.getAdapterPosition(),holder.panelName.getText().toString());
              }
          });


    }

    @Override
    public int getItemCount() {
        return currentList2.size();
    }

    public class MviewHolder extends RecyclerView.ViewHolder{

        TextView panelName;

        public MviewHolder(@NonNull View itemView) {
            super(itemView);

            panelName = itemView.findViewById(R.id.textView5);
        }
    }
}
interface OnItemClickListener {
    void onItemClick(int item,String s);
}
