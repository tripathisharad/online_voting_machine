package com.votingmachine.votingmachine20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResultDeclarationAdapter extends RecyclerView.Adapter<ResultDeclarationAdapter.MyRDClass> {


    private Context context;
    private ArrayList<ResultData> resultDataArrayList;
    private int preResult = 0;

    public ResultDeclarationAdapter(Context context, ArrayList<ResultData> resultDataArrayList) {
        this.context = context;
        this.resultDataArrayList = resultDataArrayList;
    }

    @NonNull
    @Override
    public MyRDClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.result_declaration_recycler_view,parent,false);
        return new MyRDClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRDClass holder, int position) {

          if(position == 0){
              preResult = Integer.parseInt(resultDataArrayList.get(position).getUserVotes());
              holder.tag.setVisibility(View.VISIBLE);
              holder.userName.setText(resultDataArrayList.get(position).getUserName());
              holder.userVotes.setText("VOTES : " + resultDataArrayList.get(position).getUserVotes());
          }else{

              if(Integer.parseInt(resultDataArrayList.get(position).getUserVotes()) == preResult){
                  holder.tag.setVisibility(View.VISIBLE);
                  holder.userName.setText(resultDataArrayList.get(position).getUserName());
                  holder.userVotes.setText("VOTES : " + resultDataArrayList.get(position).getUserVotes());
              }else{
                  holder.tag.setVisibility(View.INVISIBLE);
                  holder.userName.setText(resultDataArrayList.get(position).getUserName());
                  holder.userVotes.setText("VOTES : " + resultDataArrayList.get(position).getUserVotes());
              }

          }

    }

    @Override
    public int getItemCount() {
        return resultDataArrayList.size();
    }

    public class MyRDClass extends RecyclerView.ViewHolder{

        private TextView userName,userVotes;
        private CircleImageView tag;

        public MyRDClass(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.textView4ForNames);
            userVotes = itemView.findViewById(R.id.textViewVotes);
            tag = itemView.findViewById(R.id.tag_image_view);
            tag.setVisibility(View.INVISIBLE);
        }
    }
}
