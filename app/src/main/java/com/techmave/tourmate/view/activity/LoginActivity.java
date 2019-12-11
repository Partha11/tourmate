package com.techmave.tourmate.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.techmave.tourmate.R;
import com.techmave.tourmate.utils.SharedPrefs;
import com.techmave.tourmate.utils.Utility;
import com.techmave.tourmate.viewmodel.LoginViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_email)
    TextInputEditText loginEmail;
    @BindView(R.id.login_password)
    TextInputEditText loginPassword;
    @BindView(R.id.login_layout)
    LinearLayout loginLayout;
    @BindView(R.id.register_email)
    TextInputEditText registerEmail;
    @BindView(R.id.register_password)
    TextInputEditText registerPassword;
    @BindView(R.id.register_confirm_password)
    TextInputEditText registerConfirmPassword;
    @BindView(R.id.register_layout)
    LinearLayout registerLayout;
    @BindView(R.id.guide_to_register)
    LinearLayout guideToRegister;
    @BindView(R.id.guide_to_login)
    LinearLayout guideToLogin;

    private LoginViewModel viewModel;
    private SharedPrefs prefs;

    private boolean doubleBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        prefs = new SharedPrefs(this);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @OnClick({R.id.login_button, R.id.sign_up_button, R.id.register_button, R.id.sign_in_button})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.login_button:

                if (TextUtils.isEmpty(Objects.requireNonNull(loginEmail.getText()).toString())) {

                    loginEmail.setError(getResources().getString(R.string.email_error));

                } else if (!Utility.isValidEmail(loginEmail.getText().toString())) {

                    loginEmail.setError(getResources().getString(R.string.email_invalid));

                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginPassword.getText()).toString())) {

                    loginPassword.setError(getResources().getString(R.string.email_error));

                } else {

                    viewModel.signInUser(loginEmail.getText().toString(), loginPassword.getText().toString())
                            .observe(this, status -> {

                                if (status.isSuccessful()) {

                                    prefs.setUid(status.getUserId());
                                    prefs.setUserEmail(loginEmail.getText().toString().trim());
                                    prefs.setInstalled(true);
                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                    finish();

                                } else {

                                    Toast.makeText(this, status.getFailReason(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                break;

            case R.id.sign_up_button:

                loginPassword.setText("");
                loginLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);
                guideToLogin.setVisibility(View.VISIBLE);
                guideToRegister.setVisibility(View.GONE);
                break;

            case R.id.register_button:

                if (TextUtils.isEmpty(Objects.requireNonNull(registerEmail.getText()).toString())) {

                    registerEmail.setError(getResources().getString(R.string.email_error));

                } else if (!Utility.isValidEmail(registerEmail.getText().toString())) {

                    registerEmail.setError(getResources().getString(R.string.email_invalid));

                } else if (TextUtils.isEmpty(Objects.requireNonNull(registerPassword.getText()).toString())) {

                    registerPassword.setError(getResources().getString(R.string.email_error));

                } else if (TextUtils.isEmpty(Objects.requireNonNull(registerConfirmPassword.getText()).toString())) {

                    registerConfirmPassword.setError(getResources().getString(R.string.email_error));

                } else if (!TextUtils.equals(registerPassword.getText().toString(), registerConfirmPassword.getText().toString())) {

                    registerConfirmPassword.setError(getResources().getString(R.string.passwords_dont_match));

                } else if (registerPassword.getText().toString().length() < 6) {

                    registerPassword.setError(getResources().getString(R.string.password_length_error));

                } else {

                    viewModel.registerUser(registerEmail.getText().toString(), registerPassword.getText().toString())
                            .observe(this, status -> {

                                if (status.isSuccessful()) {

                                    prefs.setUid(status.getUserId());
                                    prefs.setUserEmail(registerEmail.getText().toString().trim());
                                    prefs.setInstalled(true);
                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                    finish();

                                } else {

                                    Toast.makeText(this, status.getFailReason(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                break;

            case R.id.sign_in_button:

                registerPassword.setText("");
                registerConfirmPassword.setText("");
                registerLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                guideToLogin.setVisibility(View.GONE);
                guideToRegister.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackPressed) {

            super.onBackPressed();
            return;
        }

        this.doubleBackPressed = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
    }
}
