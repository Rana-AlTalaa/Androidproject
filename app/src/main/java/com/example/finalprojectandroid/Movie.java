package com.example.finalprojectandroid;

public class Movie {

    private String title;
    private String overview;
    private String releaseDate;
    private double rating;
    private String posterPath;

    public Movie(String title,
                 String overview,
                 String releaseDate,
                 double rating,
                 String posterPath) {

        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPosterUrl() {

        if (posterPath == null || posterPath.isEmpty()) {
            return "";
        }

        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
}