package com.balkarov.yamusic.ViewControllers;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.balkarov.yamusic.Model.Artist;
import com.balkarov.yamusic.Services.ArtistCacheService;
import com.balkarov.yamusic.Views.AlertView;
import com.balkarov.yamusic.Model.ArtistsModel;
import com.balkarov.yamusic.Services.ArtistDataService;
import com.balkarov.yamusic.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class PopularArtistsActivity extends AppCompatActivity {

    View emptyView;
    View progressView;
    ArtistsModel artistsModel;
    ArtistCacheService artistCacheService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular);

        artistCacheService = new ArtistCacheService(this);
        artistsModel = new ArtistsModel();

        setToolbar();
        setUI();
        setImageLoaderOption();

        artistsModel.setArtists(artistCacheService.getArtists());
        if (artistsModel.getArtists() != null) setPager();

        loadData();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Топ исполнителей");
        }
    }

    private void setUI() {
        // Set up emtpy view when data null
        emptyView = findViewById(R.id.emptyView);
        showEmptyView(false); // deafult hide emptyView

        // Set up progress wheel when load data
        progressView = findViewById(R.id.progress_wheel);
        showProgressWheel(false); // default hide progress wheel
    }

    private void setPager() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        if (viewPager != null)
            viewPager.setAdapter(sectionsPagerAdapter);

        // Set up the Tab section
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null)
            tabLayout.setupWithViewPager(viewPager);
    }

    private void showEmptyView(boolean isShow) {
        if (emptyView != null)
            emptyView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void showProgressWheel(boolean isShow) {
        if (progressView != null)
            progressView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }




    // Set settings image load and cache
    private void setImageLoaderOption() {
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_placeholder)
                .showImageForEmptyUri(R.drawable.default_placeholder)
                .showImageOnFail(R.drawable.default_placeholder)
                .resetViewBeforeLoading(true) // if image does not loaded than reload it.
                .cacheOnDisk(true) // save cache in Disk
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(imageOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void loadData() {
        showProgressWheel(true);
        ArtistDataService.getArtists(new ArtistDataService.ArtistsResponseCallback() {
            @Override
            public void result(int statusCode, List<Artist> artists) {
                if (statusCode == 200) {
                    if (artists != null) {
                        artistsModel.setArtists(artists);
                        artistCacheService.saveArtists(artists); // caching artists
                        setPager(); // reload ViewPager with new data
                    } else {
                        showEmptyView(true);
                    }
                }
                else {
                    AlertView.internetConnectionError(PopularArtistsActivity.this);
                }

                showProgressWheel(false);
            }
        });
    }


    /**
     * A {@link PopularArtistsFragment} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            String genre = artistsModel.getGenres().get(position);
            return PopularArtistsFragment.newInstance(position + 1, artistsModel.getArtistsByGenre(genre));
        }

        @Override
        public int getCount() {
            return artistsModel.getGenres().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return artistsModel.getGenres().get(position);
        }
    }

}
