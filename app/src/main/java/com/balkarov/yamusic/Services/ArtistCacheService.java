package com.balkarov.yamusic.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.balkarov.yamusic.Model.Artist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ArtistCacheService {

    private String PREFS_NAME = "artist";
    private String PREFS_ARTIST_KEY = "popular";
    private final Context context;
    private final SharedPreferences sharedPrefs;

    public ArtistCacheService() {
        context = null;
        sharedPrefs = null;
    }

    public ArtistCacheService(Context ctx) {
        context = ctx;
        sharedPrefs = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public void saveArtists(List<Artist> artists) {
        if (artists != null && artists.size() > 0) {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();
            editor.putString(PREFS_ARTIST_KEY, gson.toJson(artists));
            editor.apply();
        }
    }

    public List<Artist> getArtists() {
        Gson gson = new Gson();
        String json = sharedPrefs.getString(PREFS_ARTIST_KEY, null);
        Type listType = new TypeToken<List<Artist>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
}
