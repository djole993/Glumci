package com.example.bulatovic.glumci.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.bulatovic.glumci.db.model.Actor;
import com.example.bulatovic.glumci.db.model.Movie;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class OrmLightHelper extends OrmLiteSqliteOpenHelper{
    public static final String DATABASE_NAME = "glumci.db";
    public static final int DATABASE_VERSION = 1;

    private Dao<Actor, Integer> mActorDao = null;
    private Dao<Movie, Integer> mMovieDao = null;

    public OrmLightHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Actor.class);
            TableUtils.createTable(connectionSource, Movie.class);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Actor.class, true);
            TableUtils.dropTable(connectionSource, Movie.class, true);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Dao<Actor, Integer> getActorDao() {
        if (mActorDao == null){
            try {
                mActorDao = getDao(Actor.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mActorDao;
    }

    public Dao<Movie, Integer> getMovieDao() {
        if (mMovieDao == null){
            try {
                mMovieDao = getDao(Movie.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mMovieDao;
    }

    @Override
    public void close() {
        mActorDao = null;
        super.close();
    }
}
