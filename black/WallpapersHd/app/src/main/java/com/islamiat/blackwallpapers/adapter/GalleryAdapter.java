package com.islamiat.blackwallpapers.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.helper.GlideManager;
import com.islamiat.blackwallpapers.model.Photo;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_VIEW_TYPE = 0;
    public static final int AD_VIEW_TYPE = 1;

    private List<Photo> list;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private int resource = R.layout.row_gallery_thumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img_thumbnail);
        }
    }


    public void addItems(List<Photo> list) {
        //int size = list.size();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void newItems(List<Photo> list) {
        //int size = list.size();
        this.list = list;
        notifyDataSetChanged();
    }


    public List<Photo> getList() {
        return list;
    }

    public GalleryAdapter(Context context, List<Photo> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderView, int position) {

        MyViewHolder holder = (MyViewHolder) holderView;
        Photo photo = list.get(position);
        //final String url = photo.thumb;

        /*Glide.with(mContext)
                .load(photo.thumb)
                .thumbnail(0.2f)
                //.transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);*/


        GlideManager.loadImage(mContext,photo.thumb,holder.imageView);



        final int i = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(i);
                    //Log.e("TAG", "onItemClick "+ i);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return ITEM_VIEW_TYPE;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}