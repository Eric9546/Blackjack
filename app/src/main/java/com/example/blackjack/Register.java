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

public class Register extends AppCompatActivity
{

    EditText mEmail, mPassword;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.loginBtn);
        fAuth = FirebaseAuth.getInstance();
        mProgress = findViewById(R.id.progress);

        // Check if the user is already logged in //
        if (fAuth.getCurrentUser() != null)
        {

            startActivity (new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener()
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

                if (password.length() < 6)
                {

                    mPassword.setError("Password Must Be More Then 6 Characters!");

                }

                // Show progress //
                mProgress.setVisibility(View.VISIBLE);

                //Register the user to firebase //
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if (task.isSuccessful())
                        {

                            Toast.makeText(Register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity (new Intent(getApplicationContext(), Login.class));

                        }

                        else
                        {

                            Toast.makeText(Register.this, "Error! Please Register Again!", Toast.LENGTH_SHORT).show();
                            mProgress.setVisibility(View.INVISIBLE);

                        }

                    }

                });

            }

        });

    }

}