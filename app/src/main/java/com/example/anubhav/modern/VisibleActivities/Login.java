package com.example.anubhav.modern.VisibleActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    private static final int OTP_REQUEST = 101;
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
                        if (isNetworkAvailable()) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mLoginButton.setText("Sending OTP ...");
                            touchDisabled();
                            verifyPhone();
                        } else {
                            Snackbar.make(mLoginButton, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        if (mLoginSharedPreferences.getBoolean(ApplicationConstants.firstLogin, true) == true) {
                            startActivity(new Intent(Login.this, GetDetailsActivity.class));
                        } else {
                            goToMainAcivity();
                        }
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
                finish();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = s;
                mResendToken = forceResendingToken;
                mLoginButton.setEnabled(false);
                new CountDownTimer(20000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mLoginButton.setText("Detecting OTP, you can enter manually in" + millisUntilFinished / 1000 + "s");

                    }

                    public void onFinish() {
                        if (mLoginSharedPreferences.getString(ApplicationConstants.phoneNumber, null) == null) {
                            Intent i = new Intent(Login.this, OtpManual.class);
                            startActivityForResult(i, OTP_REQUEST);
                        }
                    }

                }.start();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                try {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        mLoginButton.setText(R.string.tryagain);
                        mLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        Snackbar.make(mLoginButton, "Incorrect Number or Remove area code", Snackbar.LENGTH_SHORT).show();
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        mLoginButton.setText(R.string.tryagain);
                        mLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        Snackbar.make(mLoginButton, "Can't authenticate", Snackbar.LENGTH_LONG);
                    }
                } catch (Exception ex) {
                    Toast.makeText(Login.this, "Try Again", Toast.LENGTH_SHORT).show();

                }

                touchEnabled();
                mLoginButton.setText(R.string.tryagain);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        };
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber
                        ("+91" + mPhoneEditText.getText().toString()
                                , 1
                                , TimeUnit.MINUTES
                                , this
                                , mCallbacks);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OTP_REQUEST && RESULT_OK == resultCode) {
            PhoneAuthCredential credential = new PhoneAuthCredential(mVerificationId, data.getStringExtra(IntentConstants.otpRequest));
            signInWithPhoneAuthCredential(credential);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        showProgress(false);
        touchEnabled();
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
        mPhoneEditText.setEnabled(false);
        mLoginButton.setEnabled(false);
    }

    public void touchEnabled() {
        mPhoneEditText.setEnabled(true);
        mLoginButton.setEnabled(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress(false);
        mLoginButton.setEnabled(true);
        mLoginButton.setText(R.string.login_text);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this, GetDetailsActivity.class));
                            mLoginSharedPreferences.edit().putString(ApplicationConstants.phoneNumber, mPhoneEditText.getText().toString()).apply();

//                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            touchEnabled();
                            mLoginButton.setText("Try Again");
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                touchEnabled();
                                Snackbar.make(mLoginButton, "Invalid otp entered", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
