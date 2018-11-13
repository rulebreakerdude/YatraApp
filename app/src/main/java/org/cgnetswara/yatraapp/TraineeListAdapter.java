package org.cgnetswara.yatraapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TraineeListAdapter extends RecyclerView.Adapter<TraineeListAdapter.TraineeViewHolder> {

    class TraineeViewHolder extends RecyclerView.ViewHolder {
        private final TextView traineeItemView;
        private final TextView traineeDateTime;
        private final CheckBox traineeSyncedView;

        private TraineeViewHolder(View itemView) {
            super(itemView);
            traineeItemView = itemView.findViewById(R.id.textView);
            traineeDateTime =itemView.findViewById(R.id.textView2);
            traineeSyncedView=itemView.findViewById(R.id.checkBox2);
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
            holder.traineeDateTime.setText(current.getDateTime());
            if(current.getIsSynced()==1){
                holder.traineeSyncedView.setChecked(true);
            }
            else{
                holder.traineeSyncedView.setChecked(false);
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.traineeItemView.setText("No Word");
            holder.traineeDateTime.setText("No Word");
            holder.traineeSyncedView.setChecked(false);

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
