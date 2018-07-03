package com.example.bulatovic.glumci.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Actor.TABLE_ACTOR_NAME)
public class Actor {

    public static final String TABLE_ACTOR_NAME = "actor";
    public static final String FIELD_ACTOR_NAME = "name";
    public static final String FIELD_ACTOR_ID = "id";
    public static final String FIELD_ACTOR_BIOGRAPHY = "biography";
    public static final String FIELD_ACTOR_SCORE = "skore";
    public static final String FIELD_ACTOR_BIRTH = "birth";
    public static final String FIELD_NAMES_MOVIES = "movies";

    @DatabaseField(columnName = FIELD_ACTOR_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_ACTOR_NAME)
    private String name;

    @DatabaseField(columnName = FIELD_ACTOR_BIOGRAPHY)
    private String bio;

    @DatabaseField(columnName = FIELD_ACTOR_BIRTH)
    private String birth;

    @DatabaseField(columnName = FIELD_ACTOR_SCORE)
    private float score;

    @ForeignCollectionField(columnName = Actor.FIELD_NAMES_MOVIES, eager = true)
    private ForeignCollection<Movie> movies;

    public Actor(){

    }

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public  ForeignCollection<Movie> getMovies() {return movies;}

    public  void setMovies (ForeignCollection<Movie> movies) {this.movies = movies;}

    @Override
    public String toString() {
        return  name;
    }
}
