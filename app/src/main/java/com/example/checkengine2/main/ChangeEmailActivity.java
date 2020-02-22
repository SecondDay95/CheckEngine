package com.example.checkengine2.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.checkengine2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private EditText et_change_mail;
    private Button btn_change_mail;

    //Tworzenie instancji dla autoryzacji uzytkownika w firebase:
    private FirebaseAuth firebaseAuth;

    //Tworzenie instancji do zalogowanego uzytkownika:
    private FirebaseUser firebaseUser;

    private String mail;

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
                Intent intent1 = new Intent(ChangeEmailActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_change_email);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        et_change_mail = (EditText) findViewById(R.id.et_change_mail);
        btn_change_mail = (Button) findViewById(R.id.btn_change_mail);

        toolbar.setTitle("Zmień e-mail");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Inicjalizacja instacji autoryzacji uzytkownika:
        firebaseAuth = FirebaseAuth.getInstance();
        //Teraz juz jest ustalone połączenie z Firebase.

        //Pobranie referencji(instancji) dla zalogowanego uzytkownika:
        firebaseUser = firebaseAuth.getCurrentUser();

        btn_change_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = et_change_mail.getText().toString();
                if (!mail.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseUser.updateEmail(et_change_mail.getText().toString()).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Toast toast = Toast.makeText(ChangeEmailActivity.this,
                                                "Email został zmieniony", Toast.LENGTH_SHORT);
                                        toast.show();

                                        Intent intent = new Intent(ChangeEmailActivity.this, MainActivity.class);
                                        //Usuniecie ostatniego zadanie w backStack (aby nie bylo powrotu po wylogowaniu do aktywnosci
                                        //dostępnej po zalogowaniu).Po wywolaniu ponizszych linijek z backStack zniknie ostatnia aktywnosc
                                        //przed wylogowaniem, wiec nie bedzie do niej powrotu:
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast toast = Toast.makeText(ChangeEmailActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            });
                }
                else {
                    Toast toast = Toast.makeText(ChangeEmailActivity.this,
                            "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
