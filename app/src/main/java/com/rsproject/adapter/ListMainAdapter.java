package com.rsproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rsproject.R;
import com.rsproject.encapsule.ListItems;
import com.rsproject.main.DetailActivity;

import java.util.List;

/**
 * Created by Eno on 8/23/2016.
 */
public class ListMainAdapter extends RecyclerView.Adapter<ListMainAdapter.ViewHolder> {
    private List<ListItems> itemsData;
    private Activity activity;

    public ListMainAdapter(Activity activity, List<ListItems> itemsData) {
        this.itemsData = itemsData;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItems listItems= itemsData.get(position);
        ViewHolder hold = (ViewHolder) holder;
        hold.name.setText(listItems.getName());
        hold.date.setText(listItems.getDate());
        hold.description.setText(listItems.getDescription());
        Glide.with(activity).load(listItems.getUrlImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(hold.img);
        hold.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, DetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, date, description;
        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.ndate);
            description = (TextView) itemView.findViewById(R.id.desc_items);
            img = (ImageView)itemView.findViewById(R.id.nphoto);

        }
    }
}
