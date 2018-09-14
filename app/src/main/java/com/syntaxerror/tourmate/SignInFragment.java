package com.syntaxerror.tourmate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Context mContext;

    private OnFragmentInteractionListener mListener;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private EditText userEmail;
    private EditText userPassword;

    private TextView createAccount;

    private Button signInButton;

    private OnFragmentInteractionListener data;

    private RegisterFragment registerFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.login_title);

        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        userEmail = view.findViewById(R.id.signInUserName);
        userPassword = view.findViewById(R.id.signInPassword);
        createAccount = view.findViewById(R.id.createAccount);

        signInButton = view.findViewById(R.id.signInButton);

        registerFragment = new RegisterFragment();

        fragmentManager = getActivity().getSupportFragmentManager();

        signInButton.setOnClickListener(this);
        createAccount.setOnClickListener(this);

        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(mContext);

        loginButton = view.findViewById(R.id.fbLoginButton);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                data.isLoggedIn(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                Toast.makeText(mContext, "Operation Canceled By User!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {

                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

        if (mListener != null) {

            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {

            mListener = (OnFragmentInteractionListener) context;
        }

        else {

            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        mContext = context;
        data = (OnFragmentInteractionListener) mContext;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        if (v == signInButton) {

            String userEmailString = userEmail.getText().toString().trim();
            String userPasswordString = userPassword.getText().toString().trim();

            if (TextUtils.isEmpty(userEmailString))

                userEmail.setError("Email Can't Be Empty!");

            else if (TextUtils.isEmpty(userPasswordString))

                userPassword.setError("Password Can't Br Empty!");

            else

                data.userLogIn(userEmailString, userPasswordString);
        }

        else if (v == createAccount) {

            fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.fragmentLayout, registerFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void isLoggedIn(AccessToken accessToken);
        void userLogIn(String userEmailOrName, String userPassword);
    }
}
