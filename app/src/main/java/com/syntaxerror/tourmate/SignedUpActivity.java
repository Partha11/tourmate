package com.syntaxerror.tourmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignedUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_up);

        setTitle(R.string.signed_up_activity_title);
    }
}
