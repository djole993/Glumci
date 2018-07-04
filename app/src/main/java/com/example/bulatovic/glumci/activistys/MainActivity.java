package com.example.bulatovic.glumci.activistys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.bulatovic.glumci.R;
import com.example.bulatovic.glumci.db.OrmLightHelper;
import com.example.bulatovic.glumci.db.model.Actor;
import com.example.bulatovic.glumci.dialogs.AboutDialog;
import com.example.bulatovic.glumci.prefernces.GlumciPreferences;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    OrmLightHelper databaseHelper;
    private SharedPreferences prefs;

    public static String ACTOR_KEY = "ACTOR_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listView = (ListView)findViewById(R.id.lista_glumaca);

        try {
            List<Actor> list = getDatabaseHelper().getActorDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Actor p = (Actor) listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(ACTOR_KEY, p.getId());
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    private void refresh(){
        ListView listView = (ListView) findViewById(R.id.lista_glumaca);
        if (listView != null){
            ArrayAdapter<Actor> adapter = (ArrayAdapter<Actor>) listView.getAdapter();
            if (adapter!=null){
                try {
                    adapter.clear();
                    List<Actor>list = getDatabaseHelper().getActorDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Pripremni test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_black_24dp);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_new_actor);

                Button add = (Button) dialog.findViewById(R.id.add_actor);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.actor_name);
                        EditText bio = (EditText) dialog.findViewById(R.id.actor_bio);
                        EditText birth = (EditText) dialog.findViewById(R.id.actor_birth);
                        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.actor_rating);

                        Actor a = new Actor();
                        a.setName(name.getText().toString());
                        a.setBirth(birth.getText().toString());
                        a.setBio(bio.getText().toString());
                        a.setScore(ratingBar.getRating());

                        try {
                            getDatabaseHelper().getActorDao().create(a);

                           // boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                           // boolean status = prefs.getBoolean(NOTIF_STATUS, false);

                            //if (toast){
                              //  Toast.makeText(MainActivity.this, "Added new actor", Toast.LENGTH_SHORT).show();
                            //}

                            //if (status){
                             //   showStatusMesage("Added new actor");
                            //}
                            refresh();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.glumci_about:
                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;
            case R.id.glumci_preferences:
                startActivity(new Intent(MainActivity.this, GlumciPreferences.class));
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
