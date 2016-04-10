package com.balkarov.yamusic.Services;

import com.balkarov.yamusic.Model.Artist;
import com.balkarov.yamusic.Utilities.StringEncoding;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ArtistDataService {

    public ArtistDataService() {

    }

    public static void getArtists(final ArtistsResponseCallback callback) {
        final String url = "http://cache-default04f.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0, 20000); // set (number reconnection, timeout)
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String json = StringEncoding.getStringFromByte(response);
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // ignoring error if not found property
                List<Artist> artists = null;
                try {
                    artists = Arrays.asList(mapper.readValue(json, Artist[].class)); // mapping json to array objects
                } catch (IOException e) {
                    e.printStackTrace();
                }

                callback.result(statusCode, artists);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.result(statusCode, null);
            }
        });
    }

    // callback when finish get response
    public interface ArtistsResponseCallback {
        void result(int statusCode, List<Artist> artists);
    }

}
