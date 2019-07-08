package com.islamiat.blackwallpapers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.islamiat.blackwallpapers.R;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class OSLicenseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private Context mContext;
    private int resource = R.layout.row_os_license_item;

    //add an ExpansionLayoutCollection to your recycler adapter
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHeader;
        public TextView tvContent;
        ExpansionLayout expansionLayout;

        public MyViewHolder(View view) {
            super(view);
            tvHeader = view.findViewById(R.id.tv_header);
            tvContent = view.findViewById(R.id.tv_content);
            expansionLayout = view.findViewById(R.id.expansionLayout);
        }

        public void bind(Object object){
            expansionLayout.collapse(false);
        }

        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }
    }

    public List<String> getList() {
        return list;
    }

    public OSLicenseAdapter(Context context, List<String> list) {
        mContext = context;
        this.list = list;
        //Log.e("TAG", "list: "+list.size());
        expansionsCollection.openOnlyOne(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderView, int position) {

        MyViewHolder holder = (MyViewHolder) holderView;
        holder.bind(list.get(position));
        expansionsCollection.add(holder.getExpansionLayout());

        String file = list.get(position);
        String headerText = file.replace(".txt","");

        //Log.e("TAG", "text: "+headerText);

        holder.tvHeader.setText(headerText);
        holder.tvContent.setText(readText(file));

    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    private String readText(String file){
        StringBuilder text = new StringBuilder();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open("oslicense/"+file), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append(mLine);
                text.append("\n");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        return text.toString();
    }


}