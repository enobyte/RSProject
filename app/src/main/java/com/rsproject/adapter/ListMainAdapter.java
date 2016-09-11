package com.rsproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ListItems listItems= itemsData.get(position);
        final ViewHolder hold = (ViewHolder) holder;
        hold.name.setText(listItems.getName());
        hold.date.setText(listItems.getDate());
        hold.description.setText(listItems.getDescription());


        Glide.with(activity)
                .load(listItems.getUrlImage())
                //.load("http://4.bp.blogspot.com/-y3l8eCjS9X0/VfXF4zrZLfI/AAAAAAAAFu4/fw0XWe6yy4Q/s640/Boneka%2Bsi%2BUnyil.jpg")
                /*.thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)*/
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        hold.img.setImageResource(R.drawable.no_image);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        hold.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(hold.img);
        hold.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetailActivity.class);
                //i.putExtra("name", listItems.getName());
                i.putExtra("date", listItems.getDate());
                i.putExtra("description", listItems.getDescription());
                i.putExtra("urlimage", listItems.getUrlImage());
                i.putExtra("title", listItems.getJudul());
                i.putExtra("id_laporan", listItems.getId());
                //i.putExtra("position", String.valueOf(position));
                i.putExtra("position", listItems.getId());
                activity.startActivity(i);
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
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.ndate);
            description = (TextView) itemView.findViewById(R.id.desc_items);
            img = (ImageView)itemView.findViewById(R.id.nphoto);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_image);

        }
    }
}
