package com.votingmachine.votingmachine20;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class VotinBoothAdapter extends RecyclerView.Adapter<VotinBoothAdapter.MyViewHolder> {


    private ArrayList<Participant> currentListPA;
    private Context context;
    private final OnParticipantClickVB onParticipantClickVB;



    public VotinBoothAdapter(ArrayList<Participant> currentListPA, Context context, OnParticipantClickVB onParticipantClickVB) {
        this.currentListPA = currentListPA;
        this.context = context;
        this.onParticipantClickVB = onParticipantClickVB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.voting_booth_new_view,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce_button);
        holder.serialN.setText(String.valueOf(currentListPA.get(position).serialNo));
        holder.participantN.setText(currentListPA.get(position).nameOFP);
//        holder.vote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.vote.startAnimation(animation);
//                onParticipantClickVB.onPartiClickVB(holder.getAdapterPosition());
//            }
//        });

        holder.lottieAnimationViewVB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaPlayer mediaPlayer = MediaPlayer.create(context,R.raw.voting_beep);
                mediaPlayer.start();
                Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(500);
                holder.lottieAnimationViewVB.startAnimation(animation);
                onParticipantClickVB.onPartiClickVB(holder.getAdapterPosition());
            }
        });










    }

    @Override
    public int getItemCount() {
        return currentListPA.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView serialN,participantN;
        private LottieAnimationView lottieAnimationViewVB;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            serialN = itemView.findViewById(R.id.serial_no_voting_booth);
            participantN = itemView.findViewById(R.id.participant_name_voting_booth);
            lottieAnimationViewVB = itemView.findViewById(R.id.vote_button_with_anim);

        }
    }
}
interface OnParticipantClickVB{

    void onPartiClickVB(int item);

}
