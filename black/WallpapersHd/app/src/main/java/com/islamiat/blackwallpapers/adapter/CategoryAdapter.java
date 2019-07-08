package com.islamiat.blackwallpapers.adapter;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.helper.GlideManager;
import com.islamiat.blackwallpapers.model.Tag;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Tag> list;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tvTitle;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.thumbnail);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
        }
    }


    public void addItems(List<Tag> list) {
        //int size = list.size();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void newItems(List<Tag> list) {
        //int size = list.size();
        this.list = list;
        notifyDataSetChanged();
    }


    public List<Tag> getItems() {
        return list;
    }

    public CategoryAdapter(Context context, List<Tag> list) {
        mContext = context;
        this.list = list;
        //Log.e("TAG", "List size: "+list.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Tag tag = list.get(position);

        MyViewHolder holder = (MyViewHolder) viewHolder;

        /*Glide.with(mContext)
                .load(tag.imageUrl)
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .crossFade(500)
                .into(holder.imageView);*/

        GlideManager.loadImage(mContext,tag.imageUrl,holder.imageView);


        if (!TextUtils.isEmpty(tag.name)) {
            holder.tvTitle.setText(tag.name);
        } else {
            holder.tvTitle.setText("");
        }

        final int i = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public void setOnItemClickListener(@Nullable OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}