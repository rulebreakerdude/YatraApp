package org.cgnetswara.yatraapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TraineeListAdapter extends RecyclerView.Adapter<TraineeListAdapter.TraineeViewHolder> {

    class TraineeViewHolder extends RecyclerView.ViewHolder {
        private final TextView traineeItemView;
        private final TextView traineeNameView;
        private final TextView trainerNumberView;
        private final TextView trainerNameView;
        private final TextView traineeDateTime;
        private final CheckBox traineeSyncedView;
        private final LinearLayout traineeLinearLayout;

        private TraineeViewHolder(View itemView) {
            super(itemView);
            traineeItemView = itemView.findViewById(R.id.textView);
            traineeNameView=itemView.findViewById(R.id.textView2);
            traineeDateTime =itemView.findViewById(R.id.textView3);
            traineeSyncedView=itemView.findViewById(R.id.checkBox2);
            trainerNumberView=itemView.findViewById(R.id.textView4);
            trainerNameView=itemView.findViewById(R.id.textView5);
            traineeLinearLayout=itemView.findViewById(R.id.linearLayoutContainer);
        }
    }

    private final LayoutInflater mInflater;
    private List<Trainee> mTrainees; // Cached copy of words

    TraineeListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public TraineeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TraineeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TraineeViewHolder holder, int position) {
        if (mTrainees != null) {
            Trainee current = mTrainees.get(position);
            holder.traineeItemView.setText(current.getTraineeNumber());
            holder.traineeNameView.setText(current.getTraineeName());
            holder.trainerNumberView.setText(current.getTrainerNumber());
            holder.trainerNameView.setText(current.getTrainerName());
            holder.traineeDateTime.setText(current.getDateTime());
            if(current.getIsSynced()==1){
                holder.traineeSyncedView.setChecked(true);
            }
            else{
                holder.traineeSyncedView.setChecked(false);
            }
            if(current.getIsAnswered()==1){
                holder.traineeLinearLayout.setBackgroundColor(Color.parseColor("#B8FAA6"));
            }
            else{
                holder.traineeLinearLayout.setBackgroundColor(0x00000000);
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.traineeItemView.setText("No Word");
            holder.traineeDateTime.setText("No Word");
            holder.traineeSyncedView.setChecked(false);
            Linkify.addLinks(holder.traineeItemView,Linkify.ALL);

        }
    }

    void setTrainees(List<Trainee> trainees){
        mTrainees = trainees;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTrainees != null)
            return mTrainees.size();
        else return 0;
    }
}
