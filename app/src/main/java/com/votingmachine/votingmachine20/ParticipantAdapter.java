package com.votingmachine.votingmachine20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.MyViewHolder> {

    private ArrayList<Participant> currentListPA;
    private Context context;
    private final OnParticipantClick onParticipantClick;

    public ParticipantAdapter(ArrayList<Participant> currentList, Context context,OnParticipantClick onParticipantClick) {
        this.currentListPA = currentList;
        this.context = context;
        this.onParticipantClick = onParticipantClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.participant_view,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.serialN.setText(String.valueOf(currentListPA.get(position).serialNo));
        holder.participantN.setText(currentListPA.get(position).nameOFP);
        holder.participantN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onParticipantClick.onPartiClick(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return currentListPA.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView serialN,participantN;
        ImageView symbolN;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            serialN = itemView.findViewById(R.id.serial_no);
            participantN = itemView.findViewById(R.id.textView3);
            symbolN = itemView.findViewById(R.id.imageView3);

        }
    }
}
interface OnParticipantClick{

    void onPartiClick(int item);

}
