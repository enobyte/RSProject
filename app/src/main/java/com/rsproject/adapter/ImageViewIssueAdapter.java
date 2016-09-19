package com.rsproject.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rsproject.R;
import com.rsproject.encapsule.ListImageIssueArrays;

import java.util.List;

/**
 * Created by Eno on 9/19/2016.
 */
public class ImageViewIssueAdapter extends RecyclerView.Adapter<ImageViewIssueAdapter.ViewHolder> {
    private List<ListImageIssueArrays> itemsData;
    private Activity activity;
    private Dialog alertDialog;

    public ImageViewIssueAdapter(List<ListImageIssueArrays> itemsData, Activity activity) {
        this.itemsData = itemsData;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListImageIssueArrays listItems = itemsData.get(position);
        final ViewHolder hold = (ViewHolder) holder;
        hold.imageView.setImageBitmap(listItems.getImage());
        hold.zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                LayoutInflater inflater = activity.getLayoutInflater();
                final View root = (View) inflater.inflate(R.layout.image_preview, null);
                ImageView prev = (ImageView) root.findViewById(R.id.imagepreview);
                ImageView close_prev = (ImageView) root.findViewById(R.id.close_preview);
                close_prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                //alertDialog.setView(root);
                alertDialog.setContentView(root);
                prev.setImageBitmap(listItems.getImage());
                prev.setVisibility(View.VISIBLE);
                alertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, zoom;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            zoom = (ImageView) itemView.findViewById(R.id.zoomprev);
        }
    }

}
