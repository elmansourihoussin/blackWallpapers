package com.islamiat.blackwallpapers.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.admob.BannerHelper;
import com.islamiat.blackwallpapers.fragment.FavoriteFragment;
import com.islamiat.blackwallpapers.fragment.HistoryFragment;
import com.islamiat.blackwallpapers.fragment.NewFragment;
import com.islamiat.blackwallpapers.helper.Utils;
import com.ornach.andutils.java.StringUtils;
import com.ornach.magicicon.IconButton;

public class MainActivity extends AppCompatActivity{


    private SectionsPagerAdapter mSectionsPagerAdapter;

    EditText edtSearch;
    IconButton btnSearch;

    private ViewPager mViewPager;

    //CategoryFragment categoryFragment;
    NewFragment newFragment;
    FavoriteFragment favoriteFragment;
    HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //categoryFragment = CategoryFragment.newInstance();
        newFragment = NewFragment.newInstance();
        favoriteFragment = FavoriteFragment.newInstance();
        historyFragment = HistoryFragment.newInstance();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        btnSearch = (IconButton) findViewById(R.id.btn_search);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

       // mViewPager.setCurrentItem(1);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtSearch.getText().toString();
                if (!StringUtils.isEmpty(text)) textSearch(text);
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = edtSearch.getText().toString();
                    if (!StringUtils.isEmpty(text)) textSearch(text);
                    handled = true;
                }
                return handled;
            }
        });

        // load admob banner ad
        BannerHelper.newInstance(this).loadBannerAdView((FrameLayout) findViewById(R.id.layout_ad));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_history) {
            Utils.clearHistory(this);
            return true;
        }else if (id == R.id.action_share) {
            Utils.shareApps(this);
            return true;
        }else if (id == R.id.action_more_app) {
            Utils.goMoreApps(this);
            return true;
        }else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
               /* case 0:
                    return categoryFragment;*/
                case 1:
                    return favoriteFragment;
                case 2:
                    return historyFragment;
                case 0:
                default:
                    return newFragment;

            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void textSearch(String text) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Utils.EXTRA_TEXT, text);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        Log.e("TAG", "onConfigurationChanged");

        /*// Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }*/


    }
}
