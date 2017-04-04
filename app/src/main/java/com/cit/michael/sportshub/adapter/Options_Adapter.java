package com.cit.michael.sportshub.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cit.michael.sportshub.R;

import java.util.List;

/**
 * Created by micha on 04/04/2017.
 */

public class Options_Adapter extends RecyclerView.Adapter<Options_Adapter.OptionsViewHolder> {

    List<String> options;


    public class OptionsViewHolder extends RecyclerView.ViewHolder {
        TextView lblOption;
        public OptionsViewHolder(View v) {
            super(v);
            lblOption = (TextView) v.findViewById(R.id.lblOption);
        }
    }

    public Options_Adapter(List<String> options){
        this.options = options;
    }

    @Override
    public OptionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_options, parent, false);

        return new OptionsViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(Options_Adapter.OptionsViewHolder holder, int position) {
        holder.lblOption.setText(options.get(position).toString());

    }
    @Override
    public int getItemCount() {
        return options.size();
    }
}


