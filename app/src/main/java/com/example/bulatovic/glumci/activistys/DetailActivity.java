package com.example.bulatovic.glumci.activistys;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bulatovic.glumci.R;
import com.example.bulatovic.glumci.db.OrmLightHelper;
import com.example.bulatovic.glumci.db.model.Actor;
import com.example.bulatovic.glumci.db.model.Movie;
import com.example.bulatovic.glumci.prefernces.GlumciPreferences;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private  OrmLightHelper databaseHelper;
    private GlumciPreferences preferences;
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

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

       // preferences = (GlumciPreferences) PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(MainActivity.ACTOR_KEY);

        try {
            a = getDatabaseHelper().getActorDao().queryForId(key);

            name = (EditText) findViewById(R.id.actor_name);
            bio = (EditText) findViewById(R.id.actor_biography);
            birth = (EditText) findViewById(R.id.actor_birth);
            rating = (RatingBar) findViewById(R.id.acrtor_rating);

            name.setText(a.getName());
            bio.setText(a.getBio());
            birth.setText(a.getBirth());
            rating.setRating(a.getScore());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ListView listView = (ListView) findViewById(R.id.actor_movies);
        try {
            List<Movie> list = (List<Movie>) getDatabaseHelper().getMovieDao().queryBuilder()
                    .where()
                    .eq(Movie.FIELD_MOVIE_USER, a.getId())
                    .query();
            ListAdapter adapter = new  ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie m = (Movie) listView.getItemAtPosition(position);
                    Toast.makeText(DetailActivity.this, m.getName() + " " + m.getGenre() + " " + m.getYear(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void refresh(){
        ListView listView = (ListView) findViewById(R.id.actor_movies);
        if (listView != null){
            ArrayAdapter<Movie> adapter = (ArrayAdapter<Movie>)listView.getAdapter();
            if (adapter != null){
                try {
                    adapter.clear();
                    List<Movie>list = getDatabaseHelper().getMovieDao().queryBuilder()
                            .where()
                            .eq(Movie.FIELD_MOVIE_USER, a.getId())
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.priprema_add_movie:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_new_movie);

                Button add = (Button) dialog.findViewById(R.id.add_movie);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.movie_name);
                        EditText genre = (EditText) dialog.findViewById(R.id.movie_genre);
                        EditText year = (EditText) dialog.findViewById(R.id.movie_year);

                        Movie m = new Movie();
                        m.setName(name.getText().toString());
                        m.setGenre(genre.getText().toString());
                        m.setYear(year.getText().toString());
                        m.setUser(a);

                        try {
                            getDatabaseHelper().getMovieDao().create(m);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refresh();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            case R.id.priprema_edit:
                a.setName(name.getText().toString());
                a.setBirth(birth.getText().toString());
                a.setBio(bio.getText().toString());
                a.setScore(rating.getRating());

                try {
                    getDatabaseHelper().getActorDao().update(a);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.priprema_remove:
                try {
                    getDatabaseHelper().getActorDao().delete(a);
                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public OrmLightHelper getDatabaseHelper(){
        if (databaseHelper == null){
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
