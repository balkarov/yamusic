package com.balkarov.yamusic.ViewControllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balkarov.yamusic.Model.Artist;
import com.balkarov.yamusic.R;
import com.balkarov.yamusic.Utilities.Prefix;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

public class ArtistDetailActivity extends AppCompatActivity {

    Artist artist;
    ImageView coverImageView;
    FloatingActionButton shareActionButton;
    TextView descriptionTextView;
    TextView tracksTextView;
    TextView albumsTextView;
    TextView genresTextView;
    Boolean isBigCoverCacheExists;
    View descriptionContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setIntentData();
        if (artist != null) {
            setToolbar();
            setUI();
            setContentData();
            setTransiotionListener();
        }
    }

    private void setIntentData() {
        Gson gson = new Gson();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String objectString = bundle.getString("artist");
            artist = gson.fromJson(objectString, Artist.class);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(artist.getName());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUI() {
        shareActionButton = (FloatingActionButton) findViewById(R.id.shareActionButton);
        if (shareActionButton != null) {
            shareActionButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artist.getLink()));
                    startActivity(browserIntent);
                }
            });
        }
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        tracksTextView = (TextView) findViewById(R.id.tracksTextView);
        albumsTextView = (TextView) findViewById(R.id.albumsTextView);
        coverImageView = (ImageView) findViewById(R.id.coverImageView);
        genresTextView = (TextView) findViewById(R.id.genresTextView);
        descriptionContainer = findViewById(R.id.descriptionContainer);
    }

    private void setContentData() {
        setCoverImage();
        if (descriptionTextView != null)
            descriptionTextView.setText(artist.getDescription());
        if (albumsTextView != null)
            albumsTextView.setText(artist.getAlbums() + " " + "альбом" + Prefix.getStringPrefix(artist.getAlbums()));
        if (tracksTextView != null)
            tracksTextView.setText(artist.getTracks() + " " + "трек" + Prefix.getStringPrefix(artist.getTracks()));
        if (genresTextView != null)
            genresTextView.setText(TextUtils.join(", ", artist.getGenres()));
    }

    private void setCoverImage() {
        File bigCoverFile = ImageLoader.getInstance().getDiskCache().get(artist.cover.getBig());
        isBigCoverCacheExists = bigCoverFile.exists(); // // check big image cover in cache (if loaded earlier)
        if (coverImageView != null) {
            if (isBigCoverCacheExists) {
                coverImageView.setImageURI(Uri.parse(bigCoverFile.getAbsolutePath())); // set big image cover
            } else {
                ImageLoader.getInstance().displayImage(artist.cover.getSmall(), coverImageView); // temporarily set small image until loaded big
            }
        }
    }

    // Animation listener (for example: check when window animtation finished and start to animate other element)
    private void setTransiotionListener() {
        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                if (shareActionButton != null) {
                    shareActionButton.setScaleX(0);
                    shareActionButton.setScaleY(0);
                }
                if (descriptionContainer != null)
                    descriptionContainer.setTranslationY(descriptionContainer.getHeight());
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                if (!isBigCoverCacheExists) { // if cover imageview have small cover when load big cover
                    ImageLoader.getInstance().loadImage(artist.cover.getBig(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            coverImageView.setImageBitmap(loadedImage);
                        }
                    });
                }

                if (shareActionButton != null) {
                    shareActionButton.animate().scaleX(1);
                    shareActionButton.animate().scaleY(1);
                }
                if (descriptionContainer != null)
                    descriptionContainer.animate().translationY(0);

                getWindow().getEnterTransition().removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }


}
