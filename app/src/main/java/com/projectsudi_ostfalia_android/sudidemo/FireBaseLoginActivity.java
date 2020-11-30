package com.projectsudi_ostfalia_android.sudidemo;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.projectsudi_ostfalia_android.sudidemo.R.id.buttonLoginAusloggen;

public class FireBaseLoginActivity extends AppCompatActivity implements View.OnClickListener {

    //fields
    private EditText mEditTextMail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private Button mButtonLogout;
    private Button mButtonRegistrieren;
    private TextView mTextViewMail;
    private TextView mTextViewUID;
    private TextView mTextViewPasswordvergessen;
    private FirebaseAuth mAuth = null;
    private FirebaseUser mUser;
    private ProgressBar mProgressBarLogin;
    private String TAG = "SudiDEMO FireBaseDemo";

    public Activity getActivity() {
        return this;
    }

    //On Create Methode
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check ob man schon angemeldet ist
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            setContentView(R.layout.activity_fire_base_login);
        }

        mEditTextMail = findViewById(R.id.editTextLoginMail);
        mEditTextPassword = findViewById(R.id.editTextLoginPassword);
        mButtonLogin = findViewById(R.id.buttonLoginEinloggen);
        mButtonLogout = findViewById(R.id.buttonLoginAusloggen);
        mButtonRegistrieren = findViewById(R.id.buttonLoginRegistrieren);
        mTextViewMail = findViewById(R.id.TextViewFirebaseLoginMail);
        mTextViewUID = findViewById(R.id.TextViewFirebaseLoginUID);
        mTextViewPasswordvergessen = findViewById(R.id.textViewFirebasePasswortVergessen);
        mProgressBarLogin = findViewById(R.id.progressBarLogin);


        //OnClickListener
        mButtonRegistrieren.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        mButtonLogout.setOnClickListener(this);

        //OnClickListener für Password vergessen
        mTextViewPasswordvergessen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                FireBaseLoginActivity.PasswortVergessen dialogFragment = new FireBaseLoginActivity.PasswortVergessen();
                dialogFragment.show(fm, "Pfad Dialogbox");
            }
        });
    }

    //Login Methode
    private void LoginUser() {
        // EditText auslesen
        String mail = mEditTextMail.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();

        //Password & EMail Validierung
        if (mail.isEmpty()) {
            mEditTextMail.setError("Darf nicht leer sein");
            mEditTextMail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            mEditTextMail.setError("Bitte geben sie eine Gültige E-Mail Adresse an");
            mEditTextMail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mEditTextPassword.setError("Darf nicht leer sein");
            mEditTextPassword.requestFocus();
            return;
        }
        if (mEditTextPassword.length() < 6) {
            mEditTextPassword.setError("Bitte geben sie mehr als 6 Zeichen ein");
            mEditTextPassword.requestFocus();
            return;
        }

        mProgressBarLogin.setVisibility(View.VISIBLE);
        // Login mit Mail & Password
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Wenn erfolgreich
                        if (task.isSuccessful()) {
                            // Progressbar wird ausgeblendet
                            mProgressBarLogin.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            mUser = mAuth.getCurrentUser();
                            // Rückmeldung für den User
                            Toast.makeText(getApplicationContext(), "Angemeldet.",
                                    Toast.LENGTH_SHORT).show();
                            // LoginButton werden ausgeblendet
                            LoginButtonAusblenden();
                            Log.d(TAG, "Einloggen");
                            // Userdaten werden geladen
                            GetUserProfil();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // OnClick Methode
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLoginRegistrieren:
                startActivity(new Intent(this, FireBaseRegistryActivity.class));
                break;
            case R.id.buttonLoginEinloggen:
                LoginUser();
                break;
            case buttonLoginAusloggen:
                LogoutUser();
                break;

        }
    }

    private void LoginButtonAusblenden() {
        //Button ausblenden
        mButtonLogin.setVisibility(View.GONE);
        mEditTextMail.setVisibility(View.GONE);
        mEditTextPassword.setVisibility(View.GONE);
        mTextViewPasswordvergessen.setVisibility(View.GONE);

        //Button einblenden
        mTextViewMail.setVisibility(View.VISIBLE);
        mTextViewUID.setVisibility(View.VISIBLE);
        mButtonLogout.setVisibility(View.VISIBLE);
    }

    private void LoginButtonEinblenden() {
        //Button einblenden
        mButtonLogin.setVisibility(View.VISIBLE);
        mEditTextMail.setVisibility(View.VISIBLE);
        mEditTextPassword.setVisibility(View.VISIBLE);
        mTextViewPasswordvergessen.setVisibility(View.VISIBLE);

        //Button ausblenden
        mTextViewMail.setVisibility(View.GONE);
        mTextViewUID.setVisibility(View.GONE);
        mButtonLogout.setVisibility(View.GONE);


    }

    //User Logout
    private void LogoutUser() {
        // User wird ausgeloggt
        mAuth.signOut();
        // Rückmeldung für den User
        Toast.makeText(getApplicationContext(), "Abgemeldet.",
                Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Ausloggen");
        LoginButtonEinblenden();
    }

    //Einblenden der Userinformationen
    private void GetUserProfil() {
        //Aktueller User wird ermittelt
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {

            //Userdaten werden geladen
            String email = mUser.getEmail();
            String uid = mUser.getUid();


            mTextViewMail.setText(email);
            mTextViewUID.setText(uid);

            // Check if user's email is verified
            boolean emailVerified = mUser.isEmailVerified();


        }
    }

    // Password vergessen
    public static class PasswortVergessen extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_password, null))
                    //Positiver Button
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Password vergessen
                            //Auslesen der EditText Feld
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            EditText editTextMail = getDialog().findViewById(R.id.editTextPasswordMail);
                            String mail = editTextMail.getText().toString();

                            // Senden der ResetMail durch Firebase
                            auth.sendPasswordResetEmail(mail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Bei Erfolg Dialog wird geschlossen
                                                dismiss();
                                            }
                                        }
                                    });
                        }
                    })
                    //Abbruch
                    .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            return builder.create();
        }
    }


}
