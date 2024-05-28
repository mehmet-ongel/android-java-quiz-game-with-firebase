package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_Up_Page extends AppCompatActivity {

    EditText mail;
    EditText password;
    Button signUp;
    ProgressBar progressBar;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__page);

        mail = findViewById(R.id.editTextSignUpMail);
        password = findViewById(R.id.editTextSignUpPassword);
        signUp = findViewById(R.id.buttonSignUpSign);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();

                if (userEmail.equals("") || userPassword.equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill the all field"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    signUp.setClickable(false);
                    signUpFirebase(userEmail,userPassword);
                }

            }
        });

    }

    public void signUpFirebase(String userEmail, String userPassword)
    {

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(Sign_Up_Page.this, "Your account is created"
                                    , Toast.LENGTH_LONG).show();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Toast.makeText(Sign_Up_Page.this
                                    , "There is a problem, please try again later"
                                    , Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

}