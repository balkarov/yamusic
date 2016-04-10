package com.balkarov.yamusic.ViewControllers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.balkarov.yamusic.Model.Artist;
import com.balkarov.yamusic.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class PopularArtistsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ARTIST_LIST = "artist_list";

    PopularArtistsAdapter adapter;
    List<Artist> artists;


    public PopularArtistsFragment() {
    }


    public static PopularArtistsFragment newInstance(int sectionNumber, List<Artist> artistList) {
        PopularArtistsFragment fragment = new PopularArtistsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        Gson gson = new Gson();
        args.putString(ARG_ARTIST_LIST, gson.toJson(artistList));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.artistListView);

        Gson gson = new Gson();
        String st = getArguments().getString(ARG_ARTIST_LIST);
        artists = gson.fromJson(st, new TypeToken<List<Artist>>() {
        }.getType());

        adapter = new PopularArtistsAdapter(getActivity(), artists);
        if (listView != null) {
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startDetailIntent(view, position);
                }
            });
        }

        adapter.notifyDataSetChanged();

        return rootView;
    }

    private void startDetailIntent(View view, int position) {
        View coverImageView = view.findViewById(R.id.coverImageView);
        Gson gson = new Gson();

        Intent intent = new Intent(getActivity(), ArtistDetailActivity.class);
        Artist artist = artists.get(position);
        intent.putExtra("artist", gson.toJson(artist));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // if lolipop or higher when show Activity with animation
            Pair<View, String> pairCover = Pair.create(coverImageView, coverImageView.getTransitionName());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairCover);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}