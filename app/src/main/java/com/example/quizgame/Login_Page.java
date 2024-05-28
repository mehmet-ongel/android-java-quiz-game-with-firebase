package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Page extends AppCompatActivity {

    EditText mail;
    EditText password;
    Button signIn;
    SignInButton signInGoogle;
    TextView signUp;
    TextView forgotPassword;
    ProgressBar progressBarSignIn;

    GoogleSignInClient googleSignInClient;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);

        mail = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        signIn = findViewById(R.id.buttonLoginSignin);
        signInGoogle = findViewById(R.id.buttonLoginGoogleSignin);
        signUp = findViewById(R.id.textViewLoginSignup);
        forgotPassword = findViewById(R.id.textViewLoginForgotPassword);
        progressBarSignIn = findViewById(R.id.progressBarSignin);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();

                signInWithFirebase(userEmail,userPassword);

            }
        });

        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInWithGoogle();

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login_Page.this, Sign_Up_Page.class);
                startActivity(i);

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login_Page.this,Forgot_Password.class);
                startActivity(i);

            }
        });

    }

    public void signInWithFirebase(String userEmail, String userPassword)
    {

        progressBarSignIn.setVisibility(View.VISIBLE);
        signIn.setClickable(false);

        auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            Intent i = new Intent(Login_Page.this,MainActivity.class);
                            startActivity(i);
                            finish();
                            progressBarSignIn.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login_Page.this, "Sign In is successful"
                                    , Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Login_Page.this, "Sign In is not successful"
                                    , Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null)
        {
            Intent i = new Intent(Login_Page.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    public void signInWithGoogle()
    {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        signin();

    }

    public void signin()
    {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            firebaseSignInWithGoogle(task);
        }

    }

    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task)
    {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "Sign in is successful", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login_Page.this,MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account.getIdToken());
        }
        catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseGoogleAccount(String idToken)
    {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    //FirebaseUser user = auth.getCurrentUser();
                }
                else
                {
                    Log.w("failure", task.getException());
                }

            }
        });

    }

}