package com.rsproject.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rsproject.R;
import com.rsproject.encapsule.ListImageDetailArrays;

import java.util.List;

/**
 * Created by Eno on 9/19/2016.
 */
public class ImageViewDetailAdapter extends RecyclerView.Adapter<ImageViewDetailAdapter.ViewHolder> {
    private List<ListImageDetailArrays> itemsData;
    private Activity activity;
    private Dialog alertDialog;

    public ImageViewDetailAdapter(List<ListImageDetailArrays> itemsData, Activity activity) {
        this.itemsData = itemsData;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview_recycle_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListImageDetailArrays listItems = itemsData.get(position);
        final ViewHolder hold = (ViewHolder) holder;
        Glide.with(activity)
                .load(listItems.getImageUri())
                //.load("http://4.bp.blogspot.com/-y3l8eCjS9X0/VfXF4zrZLfI/AAAAAAAAFu4/fw0XWe6yy4Q/s640/Boneka%2Bsi%2BUnyil.jpg")
                /*.thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)*/
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        hold.imageView.setImageResource(R.drawable.no_image);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        hold.pr.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(hold.imageView);


    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, zoom;
        private ProgressBar pr;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            zoom = (ImageView) itemView.findViewById(R.id.zoomprev);
            pr = (ProgressBar) itemView.findViewById(R.id.progress_image);
        }
    }

}
