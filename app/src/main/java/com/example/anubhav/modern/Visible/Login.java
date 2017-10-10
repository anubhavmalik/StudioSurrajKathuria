package com.example.anubhav.modern.Visible;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anubhav.modern.Constants.ApplicationConstants;
import com.example.anubhav.modern.Constants.IntentConstants;
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
    public SharedPreferences mLoginSharedPreferences;//, mPhoneNumberSharedPreferences, mFirstLoginSharedPreference;
    ProgressBar mProgressBar;
    String mVerificationId;
    EditText mPhoneEditText;
    Button mLoginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mPhoneEditText = findViewById(R.id.phone_edit_text);
        mLoginButton = findViewById(R.id.login_button);
        mProgressBar = findViewById(R.id.progress_bar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mLoginSharedPreferences = this.getSharedPreferences(ApplicationConstants.loginStatePreferencesName, MODE_PRIVATE);


//        if(mFirstLoginSharedPreference.getBoolean(ApplicationConstants.firstLogin,true)){
//            fetchUserIfExists();
//        }


        if (mLoginSharedPreferences.getBoolean(ApplicationConstants.loginState, false)) {
            goToMainAcivity();
            finish();
        } else {

            showProgress(false);
            mAuth = FirebaseAuth.getInstance();
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPhoneEditText.getText().toString().length() != 10) {
                        mPhoneEditText.setError("Invalid Number");
                    } else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mLoginButton.setText("Sending OTP ...");
                        touchDisabled();
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
                mLoginSharedPreferences.edit().putString(ApplicationConstants.phoneNumber, mPhoneEditText.getText().toString()).apply();
                touchEnabled();
                startActivity(new Intent(Login.this, GetDetailsActivity.class));
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
                try {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        mLoginButton.setText(R.string.login_text);
                        mLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        Snackbar.make(mLoginButton, "Incorrect Number or Remove area code", Snackbar.LENGTH_SHORT).show();
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        mLoginButton.setText(R.string.login_text);
                        mLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        Snackbar.make(mLoginButton, "Can't authenticate", Snackbar.LENGTH_LONG);
                    }
                } catch (Exception ex) {
                    Toast.makeText(Login.this, "Try Again", Toast.LENGTH_SHORT).show();
                }
                touchEnabled();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mPhoneEditText.getText().toString(), 2, TimeUnit.MINUTES, this, mCallbacks);
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

    public void showProgress(boolean status) {
        mProgressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    private void goToMainAcivity() {
        Snackbar.make(mLoginButton, "Welcome", Snackbar.LENGTH_SHORT).show();
        if (mProgressBar.isShown())
            mProgressBar.setVisibility(View.INVISIBLE);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(Login.this, MainActivity.class);
                i.putExtra(IntentConstants.phoneNumberText, mPhoneEditText.getText().toString());
                startActivity(i);

                finish();
            }
        }, 1200);

    }

    public void touchDisabled() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mPhoneEditText.setEnabled(false);
        mLoginButton.setEnabled(false);
    }

    public void touchEnabled() {
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mPhoneEditText.setEnabled(true);
        mLoginButton.setEnabled(true);
    }


}
