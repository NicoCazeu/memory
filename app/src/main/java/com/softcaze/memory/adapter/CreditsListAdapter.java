package com.softcaze.memory.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.CreditsActivity;
import com.softcaze.memory.model.Credit;

import java.util.List;

public class CreditsListAdapter extends RecyclerView.Adapter<CreditsListAdapter.ViewHolder> {
    protected List<Credit> listCredit;

    public CreditsListAdapter(List<Credit> list) {
        listCredit = list;
    }

    @Override
    public CreditsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_row, null);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);

        CreditsListAdapter.ViewHolder viewHolder = new CreditsListAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CreditsListAdapter.ViewHolder holder, int position) {
        holder.creditImg.setImageResource(listCredit.get(position).getResImg());
        holder.creditTxt.setText(CreditsActivity.getAttribution(listCredit.get(position).getCreator()));
    }

    @Override
    public int getItemCount() {
        return listCredit.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView creditTxt;
        protected ImageView creditImg;

        public ViewHolder(View itemView) {
            super(itemView);

            creditTxt = (TextView) itemView.findViewById(R.id.credit_txt);
            creditImg = (ImageView) itemView.findViewById(R.id.credit_img);
        }
    }
}
