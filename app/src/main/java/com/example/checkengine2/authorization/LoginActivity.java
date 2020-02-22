package com.example.checkengine2.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.checkengine2.R;
import com.example.checkengine2.main.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //Utworzenie zmiennych referencyjnych do widoków z układu:
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private EditText userEmail;
    private EditText userPass;
    private Button userLogin;

    //Tworzenie instancji dla autoryzacji uzytkownika w firebase:
    private FirebaseAuth firebaseAuth;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Pobranie referencji do widoków z układu:
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.etUserPass);
        userLogin = findViewById(R.id.btnUserLogin);

        //Ustawienie tytulu dla ukladu:
        toolbar.setTitle("Logowanie");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Inicjalizacja instacji autoryzacji uzytkownika:
        firebaseAuth = FirebaseAuth.getInstance();
        //Teraz juz jest ustalone połączenie z Firebase.

        //Utworzenie obiektu nasłuchującego dla przycisku Zaloguj:
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userEmail.getText().toString();
                password = userPass.getText().toString();

                //Metoda umozliwiajaca zalogowanie sie uzytkownika:
                //OnCompleteListener to obiekt nasluchujacy poprawnosc wykonania operacji logowania.
                //W Task przechowywana jest true jest operacja odbyla sie poprawnie, a false
                //gdy operacja sie nie powiodla.
                if (!email.isEmpty() && !password.isEmpty()) {
                    //Dodanie progressBar po wcisnieciu przycisku SignUp:
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email,
                            password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Po zakonczeniu rejestracji (pomyslnie lub nie) usuniecie widoku ProgressBar:
                                    progressBar.setVisibility(View.GONE);

                                    //Jeżeli logowanie przebiegło pomyslnie:
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        //LoginActivity.this.finish();
                                    }
                                    //Jeżeli logowanie sie nie powiodlo:
                                    else {
                                        Toast toast = Toast.makeText(LoginActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG);
                                        toast.show();
                                        userEmail.setText("");
                                        userPass.setText("");
                                    }
                                }
                            });
                }
                else {
                    Toast toast = Toast.makeText(LoginActivity.this,
                            "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        userEmail.setText("");
        userPass.setText("");
    }
}
