/* Derived from the official Android tutorial on
https://developer.android.com/guide/topics/ui/layout/recyclerview
* and Suragch's answer on https://stackoverflow.com/a/40584425 */

package com.cmput301.assignment1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogsViewHolder> {

    private List<View> mDataset;
    private LayoutInflater mInflater;

    public LogsAdapter(Context context, List<View> data){
        this.mInflater = LayoutInflater.from(context);
        this.mDataset = data;
    }

    public class LogsViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public LogsViewHolder(View view){
            super(view);
            mView = itemView.findViewById(R.id.logTextView);
        }
    }

    @Override
    public LogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // create a new view
        View v = mInflater.inflate(R.layout.log, parent, false);
        return new LogsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LogsViewHolder holder, int position){
        // replace content of view
        holder.mView = this.mDataset.get(position);
    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}
