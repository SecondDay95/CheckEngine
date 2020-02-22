package com.example.checkengine2.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.checkengine2.R;
import com.example.checkengine2.controller.AverageControllerActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private int userChoose = 0;
    private SharedPreferences sharedPreferencesChoose;
    private SharedPreferences.Editor editorChoose;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();

                //Powrót do MainActivity:
                Intent intent1 = new Intent(HomeActivity.this, MainActivity.class);
                //Usuniecie ostatniego zadanie w backStack (aby nie bylo powrotu po wylogowaniu do aktywnosci
                //dostępnej po zalogowaniu).Po wywolaniu ponizszych linijek z backStack zniknie ostatnia aktywnosc
                //przed wylogowaniem, wiec nie bedzie do niej powrotu:
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Check Engine");

        //Utworzenie tablic przechowujących dane, obiektu adaptera i przypisanie go do widoku RecyclerView:
        String[] menuNames = new String[HomeList.listnames.length];
        for (int i = 0; i < menuNames.length; i++)
        {
            menuNames[i] = HomeList.listnames[i].getName();
        }
        int[] menuImages = new int[HomeList.listnames.length];
        for (int i = 0; i < menuImages.length; i++)
        {
            menuImages[i] = HomeList.listnames[i].getImageResourceID();
        }
        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(menuNames, menuImages);
        recyclerView = findViewById(R.id.menu_recycler);
        recyclerView.setAdapter(adapter);
        //Deklaracja liniowego managera umożliwiającego pionowe rozmieszczanie elementów RecyclerView:
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Utworzenie klasy anonimowej na bazie interfejsu w klasie CaptionedImagesAdapter o nazwie Listener.
        //Ta klasa anonimowa umożliwia obsługę naciśnięć użytkownika na CardView:
        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            @Override
            public void onClick(int position) {
                if (position == 0)
                {
                    userChoose = 1;

                    Intent intent = new Intent(HomeActivity.this, AverageControllerActivity.class);
                    intent.putExtra("userChoose", userChoose);
                    startActivity(intent);
                }
                if (position == 1)
                {
                    userChoose = 2;
                    Intent intent = new Intent(HomeActivity.this, AverageControllerActivity.class);
                    intent.putExtra("userChoose", userChoose);
                    startActivity(intent);
                }
                if (position == 2)
                {
                    Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }
                if (position == 3)
                {
                    FirebaseAuth.getInstance().signOut();

                    //Powrót do MainActivity:
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}
