package com.example.bulatovic.glumci.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Movie.TABLE_NAME_MOVIES)
public class Movie {
    public static final String TABLE_NAME_MOVIES = "movies";
    public static final String FIELD_NAME_MOVIE = "name";
    public static final String FIELD_ID_MOVIE = "id";
    public static final String FIELD_GENRE_MOVIE = "genre";
    public static final String FIELD_MOVIE_YEAR = "year";
    public static final String FIELD_MOVIE_USER = "user";

    @DatabaseField(columnName = FIELD_ID_MOVIE)
    private int id;
    @DatabaseField(columnName = FIELD_NAME_MOVIE)
    private String name;
    @DatabaseField(columnName = FIELD_GENRE_MOVIE)
    private String genre;
    @DatabaseField(columnName = FIELD_MOVIE_YEAR)
    private String year;
    @DatabaseField(columnName = FIELD_MOVIE_USER, foreign = true, foreignAutoRefresh = true)
    private Actor mUser;

    public Movie(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Actor getUser() {
        return mUser;
    }

    public void setUser(Actor mUser) {
        this.mUser = mUser;
    }

    @Override
    public String toString() {
        return name;
    }
}
