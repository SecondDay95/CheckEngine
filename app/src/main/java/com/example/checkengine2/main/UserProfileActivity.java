package com.example.checkengine2.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.checkengine2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private Button btn_change_mail;
    private Button btn_delete_user;
    private TextView tv_user_mail;
    private ProgressBar pb_delete_user;

    //Tworzenie instancji dla autoryzacji uzytkownika w firebase:
    private FirebaseAuth firebaseAuth;

    //Tworzenie instancji do zalogowanego uzytkownika:
    private FirebaseUser firebaseUser;

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
                Intent intent1 = new Intent(UserProfileActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_change_mail = (Button) findViewById(R.id.btn_change_mail);
        btn_delete_user = (Button) findViewById(R.id.btn_delete_user);
        tv_user_mail = (TextView) findViewById(R.id.tv_user_mail);
        pb_delete_user = (ProgressBar) findViewById(R.id.progressBar);


        toolbar.setTitle("Profil użytkownika");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Inicjalizacja instacji autoryzacji uzytkownika:
        firebaseAuth = FirebaseAuth.getInstance();
        //Teraz juz jest ustalone połączenie z Firebase.

        //Pobranie referencji(instancji) dla zalogowanego uzytkownika:
        firebaseUser = firebaseAuth.getCurrentUser();

        //Wyswietlenie maila uzytkownika w textView:
        tv_user_mail.setText(firebaseUser.getEmail());

        btn_change_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, ChangeEmailActivity.class);
                startActivity(intent);
            }
        });

        //Usunięcie użytkownika:
        btn_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utworzenie okienka ostrzegawczego:
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfileActivity.this);
                dialog.setTitle("Czy jestes pewien ?");
                dialog.setMessage("Usunięcie konta spowoduje całkowite usunięcie Twojego konta z systemu" +
                        " i nie będziesz mieć dostępu do aplikacji.");
                dialog.setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pb_delete_user.setVisibility(View.VISIBLE);
                        //Usuniecie uzytkownika:
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pb_delete_user.setVisibility(View.GONE);
                                if (task.isSuccessful())
                                {
                                    Toast toast = Toast.makeText(UserProfileActivity.this,
                                            "Konto zostało usunięte", Toast.LENGTH_SHORT);
                                    toast.show();

                                    //Powrót do MainActivity:
                                    Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                                    //Usuniecie ostatniego zadanie w backStack (aby nie bylo powrotu po wylogowaniu do aktywnosci
                                    //dostępnej po zalogowaniu).Po wywolaniu ponizszych linijek z backStack zniknie ostatnia aktywnosc
                                    //przed wylogowaniem, wiec nie bedzie do niej powrotu:
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast toast = Toast.makeText(UserProfileActivity.this,
                                            task.getException().getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });
                    }
                });

                //Utworzenie przycisku w alert umozliwiajacego zrezygnowanie w usuwaniu konta:
                dialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //Wyswietlenie okna Alert:
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }
}
