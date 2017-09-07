package com.example.anubhav.modern.Visible;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.anubhav.modern.MainActivity;
import com.example.anubhav.modern.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    ProgressBar mProgressBar;
    String mVerificationId;
    EditText mPhoneEditText;
    Button mLoginButton;
    SharedPreferences msharedPreferences;
    private String TAG = "Firebase Auth";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mPhoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        mLoginButton = (Button) findViewById(R.id.login_button);
        msharedPreferences = this.getSharedPreferences("loginState", MODE_PRIVATE);
        if (msharedPreferences.getBoolean("logged_in", false)) {
            goToMainAcivity();
            finish();
        } else {
            mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
            mProgressBar.setVisibility(View.GONE);
            mAuth = FirebaseAuth.getInstance();
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPhoneEditText.getText().toString().length() != 10) {
                        mPhoneEditText.setError("Invalid Number");
//                        Log.d(TAG, "onClick: " + mPhoneEditText.getText().toString().length());
                    } else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        verifyPhone();
                    }
                }
            });

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        goToMainAcivity();
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        Snackbar.make(mLoginButton, "Sign in to continue", Snackbar.LENGTH_LONG).show();
                    }

                }
            };

        }
    }

    private void verifyPhone() {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                msharedPreferences.edit().putBoolean("logged_in", true).apply();
                goToMainAcivity();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = s;
                mResendToken = forceResendingToken;
                mLoginButton.setBackgroundColor(getResources().getColor(R.color.logincolor));
                mLoginButton.setText("Waiting to detect OTP");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mLoginButton.setText("Login");
                    Snackbar.make(mLoginButton, "Invalid User Details", Snackbar.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    mLoginButton.setText("Login");
                    Snackbar.make(mLoginButton, "Can't authenticate", Snackbar.LENGTH_LONG);
                }

            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mPhoneEditText.getText().toString(), 2, TimeUnit.MINUTES, this, mCallbacks);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void goToMainAcivity() {
        Snackbar.make(mLoginButton, "Welcome", Snackbar.LENGTH_SHORT).show();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }
        }, 1200);

    }
}
