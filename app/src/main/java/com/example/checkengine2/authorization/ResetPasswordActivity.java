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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btn_reset_pass;
    private EditText et_forgot_pass;
    private ProgressBar progressBar;

    //Tworzenie instancji dla autoryzacji uzytkownika w firebase:
    private FirebaseAuth firebaseAuth;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Zapomniałeś hasła ?");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        btn_reset_pass = (Button) findViewById(R.id.btn_reset_pass);
        et_forgot_pass = (EditText) findViewById(R.id.et_forgot_pass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Inicjalizacja instacji autoryzacji uzytkownika:
        firebaseAuth = FirebaseAuth.getInstance();
        //Teraz juz jest ustalone połączenie z Firebase.

        btn_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_forgot_pass.getText().toString();

                if (!email.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        et_forgot_pass.setText("");
                                        Toast toast = Toast.makeText(ResetPasswordActivity.this,
                                                "Hasło zostalo wyslane", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        et_forgot_pass.setText("");
                                        Toast toast = Toast.makeText(ResetPasswordActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            });
                }
                else {
                    Toast toast = Toast.makeText(ResetPasswordActivity.this,
                            "Proszę uzupełnić wszystkie pola", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
