package com.balkarov.yamusic.Model;

import java.util.ArrayList;
import java.util.List;


public class ArtistsModel {


    private static final String ALL_KEY = "all";

    private List<Artist> artists;
    private List<String> genres;

    public ArtistsModel() {
        artists = new ArrayList<>();
        genres = new ArrayList<>();
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    // get Artists List by Genre
    public List<Artist> getArtistsByGenre(String genre) {
        if (genre.equals(ALL_KEY))
            return artists;

        List<Artist> artistsByGenre = new ArrayList<>();
        for (Artist artist : artists) {
            if (artist.getGenres().contains(genre)) {
                artistsByGenre.add(artist);
            }
        }

        return artistsByGenre;
    }

    // get Genres List
    public List<String> getGenres() {
        if (genres.size() == 0) {
            genres.add(ALL_KEY); // add genre "all artist"
            for (Artist artist : artists) {
                for (String genre : artist.getGenres()) {
                    if (!genres.contains(genre)) {
                        genres.add(genre);
                    }
                }
            }
        }

        return genres;
    }


    public void setGenres(List<String> genres) {
        this.genres = genres;
    }


}