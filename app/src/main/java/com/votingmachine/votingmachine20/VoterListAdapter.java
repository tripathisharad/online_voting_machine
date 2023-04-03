package com.votingmachine.votingmachine20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VoterListAdapter extends RecyclerView.Adapter<VoterListAdapter.VoterListClass> {

    private Context context;
    private ArrayList<String> voterEmail;

    public VoterListAdapter(Context context, ArrayList<String> voterEmail) {
        this.context = context;
        this.voterEmail = voterEmail;
    }

    @NonNull
    @Override
    public VoterListClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.voter_list_view,parent,false);
        return new VoterListClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VoterListClass holder, int position) {

        holder.voterEmail.setText(voterEmail.get(position));

    }

    @Override
    public int getItemCount() {
        return voterEmail.size();
    }

    public class VoterListClass extends RecyclerView.ViewHolder{

        TextView voterEmail;

        public VoterListClass(@NonNull View itemView) {
            super(itemView);
            voterEmail = itemView.findViewById(R.id.textView4);
        }
    }
}
