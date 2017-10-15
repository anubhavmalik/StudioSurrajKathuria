package com.example.anubhav.modern.Visible;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.anubhav.modern.Constants.IntentConstants;
import com.example.anubhav.modern.R;

public class OtpManual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_manual);
        final Button submiButton = findViewById(R.id.submit_otp_button);
        final EditText enterOtpEditText = findViewById(R.id.otp_manual_editText);

        final Intent i = getIntent();

        submiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterOtpEditText.getText().toString().isEmpty()) {
                    enterOtpEditText.setError("Enter a valid code");
                } else {
                    i.putExtra(IntentConstants.otpRequest, submiButton.getText().toString());
                    setResult(RESULT_OK, i);
                }
            }
        });


    }
}