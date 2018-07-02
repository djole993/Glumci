package com.example.bulatovic.glumci.activistys;

import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bulatovic.glumci.R;
import com.example.bulatovic.glumci.db.OrmLightHelper;
import com.example.bulatovic.glumci.db.model.Actor;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private OrmLightHelper databaseHelper;
    private SharedPreferences prefs;
    private Actor a;

    private EditText name;
    private EditText bio;
    private EditText birth;
    private RatingBar rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(MainActivity.ACTOR_KEY);

        try {
            a = getDatabaseHelper().getActorDao().queryForId(key);

            name = (EditText)findViewById(R.id.actor_name);
            bio = (EditText)findViewById(R.id.actor_bio);
            birth = (EditText)findViewById(R.id.actor_birth);
            rating = (RatingBar) findViewById(R.id.actor_rating);

            name.setText(a.getName());
            bio.setText(a.getBio());
            birth.setText(a.getBirth());
            rating.setRating(a.getScore());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.actor_movies);
        try {
            List<com.example.bulatovic.glumci.db.model.Movie> list = getDatabaseHelper().getMovieDao().queryBuilder()
                    .where()
                    .eq(com.example.bulatovic.glumci.db.model.Movie.FIELD_MOVIE_USER, a.getId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    com.example.bulatovic.glumci.db.model.Movie m = (com.example.bulatovic.glumci.db.model.Movie) listView.getItemAtPosition(position);
                    Toast.makeText(DetailActivity.this, m.getName()+" "+m.getGenre()+" "+m.getYear(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void refresh(){
        ListView listView = (ListView) findViewById(R.id.actor_movies);
        if (listView != null) {
            ArrayAdapter<com.example.bulatovic.glumci.db.model.Movie>adapter = (ArrayAdapter<com.example.bulatovic.glumci.db.model.Movie>)listView.getAdapter();

            if (adapter != null){
                try {
                    adapter.clear();
                    List<com.example.bulatovic.glumci.db.model.Movie>list = getDatabaseHelper().getMovieDao().queryBuilder()
                            .where()
                            .eq(com.example.bulatovic.glumci.db.model.Movie.FIELD_MOVIE_USER, a.getId())
                            .query();
                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public OrmLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, OrmLightHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null){
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
