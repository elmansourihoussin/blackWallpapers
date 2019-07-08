package com.islamiat.blackwallpapers.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.activity.TagsActivity;
import com.islamiat.blackwallpapers.adapter.CategoryAdapter;
import com.islamiat.blackwallpapers.admob.InterstitialHelper;
import com.islamiat.blackwallpapers.helper.DisplayHelper;
import com.islamiat.blackwallpapers.helper.Utils;
import com.islamiat.blackwallpapers.model.Tag;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Tag> tagList = new ArrayList<>();
    private CategoryAdapter adapter;

    private int column;
    private GridLayoutManager mLayoutManager;

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        column = getActivity().getResources().getInteger(R.integer.grid_column_number);
        mLayoutManager = new GridLayoutManager(getActivity(), column);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CategoryAdapter(getActivity(), tagList);
        recyclerView.setAdapter(adapter);


        final InterstitialHelper interstitialHelper = InterstitialHelper.newInstance((AppCompatActivity) getActivity());

        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {

                interstitialHelper.takeAction(new InterstitialHelper.ActionListener() {
                    @Override
                    public void onDone() {
                        Tag tag = tagList.get(position);
                        Intent intent = new Intent(getActivity(), TagsActivity.class);
                        intent.putExtra(Utils.EXTRA_TAG, tag.alias);
                        intent.putExtra(Utils.EXTRA_NAME, tag.name);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter.getItemCount() <= 0) {
            updateTagList(); // update tag adapter
        }

        DisplayHelper.updateSpan(getActivity(),mLayoutManager,column);
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Log.e("TAG", "CategoryFragment onConfigurationChanged");
        DisplayHelper.updateSpan(newConfig.orientation, mLayoutManager,column);
    }
	

    private void updateTagList() {
        tagList = Tag.getAllTags();
        adapter.newItems(tagList);
    }


}