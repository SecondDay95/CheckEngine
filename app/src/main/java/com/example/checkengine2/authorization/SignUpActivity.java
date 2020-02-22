package com.example.checkengine2.authorization;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private String email, password;

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private EditText etMail;
    private EditText etPass;
    private Button btnSign;

    //Tworzenie instancji dla autoryzacji uzytkownika w firebase:
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        etMail = (EditText) findViewById(R.id.et_mail_signUp);
        etPass = (EditText) findViewById(R.id.et_pass_signUp);
        btnSign = (Button) findViewById(R.id.btn_sign_up);

        toolbar.setTitle("Zarejestruj");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Inicjalizacja instacji autoryzacji uzytkownika:
        firebaseAuth = FirebaseAuth.getInstance();
        //Teraz juz jest ustalone połączenie z Firebase.

        //Dodanie obiektu nasluchujacego do przycisku:
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etMail.getText().toString();
                password = etPass.getText().toString();


                //Tworzenie konta nowego uzytkownika:
                //addOnCompleteListener determinuje czy operacja została wykonanan poprawnie czy nie
                //Poprawnosc operacji jest zawarta w obiekcie task
                if (!email.isEmpty() && !password.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email,
                            password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);

                                    //Sprawdzenie czy operacja rejestracji zostala zrealizowana pomyslnie:
                                    if (task.isSuccessful()) {
                                        //Jezeli rejestracja pomyslna to wyswietlamy komunikat:
                                        Toast toast = Toast.makeText(SignUpActivity.this,
                                                "Rejestracja przebiegla pomyslnie", Toast.LENGTH_SHORT);
                                        toast.show();

                                        //Usunięcie wartości z pól E-mail i Password po kliknieciu przycisku:
                                        etMail.setText("");
                                        etPass.setText("");
                                    }
                                    //Jezeli operacja rejestracji sie nie powiodla wyswietlamy komunikat otrzymany z firebase:
                                    else {
                                        Toast toast = Toast.makeText(SignUpActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG);
                                        toast.show();

                                        //Usunięcie wartości z pól E-mail i Password po kliknieciu przycisku:
                                        etMail.setText("");
                                        etPass.setText("");
                                    }
                                }
                            });
                }
                else {
                    Toast toast = Toast.makeText(SignUpActivity.this,
                            "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        etMail.setText("");
        etPass.setText("");
    }
}
