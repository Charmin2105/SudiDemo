package com.projectsudi_ostfalia_android.sudidemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class FireBaseRegistryActivity extends AppCompatActivity implements View.OnClickListener {

    //EditText Variablen
    private EditText mEditTextMail;
    private EditText mEditTextPassword;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBarRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_registry);
        mAuth = FirebaseAuth.getInstance();

        mEditTextMail = findViewById(R.id.editTextRegistryMail);
        mEditTextPassword = findViewById(R.id.editTextRegistryPassword);
        mProgressBarRegistry = findViewById(R.id.progressBarRegistry);

        findViewById(R.id.buttonRegistryLogin).setOnClickListener(this);
        findViewById(R.id.buttonRegistry).setOnClickListener(this);

    }


    private void registerUser() {
        String mail = mEditTextMail.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();

        if (mail.isEmpty()) {
            mEditTextMail.setText("Darf nicht leer sein");
            mEditTextMail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            mEditTextMail.setError("Bitte geben sie eine Gültige E-Mail Adresse an");
            mEditTextMail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mEditTextPassword.setText("Darf nicht leer sein");
            mEditTextPassword.requestFocus();
            return;
        }
        if (mEditTextPassword.length() < 6) {
            mEditTextPassword.setError("Bitte geben sie mehr als 6 Zeichen ein");
            mEditTextPassword.requestFocus();
            return;
        }
        mProgressBarRegistry.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mProgressBarRegistry.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Registrieren erfolgreich", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Es ist ein Fehler aufgetreten", Toast.LENGTH_LONG).show();
                }
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), "Du bist bereits Registriert", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegistryLogin:
                startActivity(new Intent(this, FireBaseLoginActivity.class));
                break;
            case R.id.buttonRegistry:
                registerUser();
                break;
        }
    }
}