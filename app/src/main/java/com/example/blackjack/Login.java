package com.example.blackjack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity
{

    public final static int REGISTER_CODE = 1;

    EditText mEmail, mPassword;
    Button mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mProgress = findViewById(R.id.progress);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                // Retrieve inputs from user //
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // Check to make sure inputs are not empty //
                if (TextUtils.isEmpty(email))
                {

                    mEmail.setError("Email Is Required!");
                    return;

                }

                if (TextUtils.isEmpty(password))
                {

                    mPassword.setError("Password Is Required!");
                    return;

                }

                // Show progress //
                mProgress.setVisibility(View.VISIBLE);

                // Authenticate the login //
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if (task.isSuccessful())
                        {

                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity (new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }

                        else
                        {

                            Toast.makeText(Login.this, "Error! Please Login Again!", Toast.LENGTH_SHORT).show();
                            mProgress.setVisibility(View.INVISIBLE);

                        }

                    }

                });

            }

        });

    }

    public void createAcc_onClick (View view)
    {

        Intent register = new Intent(this, Register.class);

        startActivityForResult(register, REGISTER_CODE);

    }

    public void exit_onClick(View view)
    {

        finishAffinity();
        System.exit(0);

    }
}