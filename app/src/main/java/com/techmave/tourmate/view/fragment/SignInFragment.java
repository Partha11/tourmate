package com.techmave.tourmate.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.techmave.tourmate.R;

import java.util.Arrays;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Context mContext;

    private OnFragmentInteractionListener mListener;

    private Button loginButton;
    private CallbackManager callbackManager;
    private LoginButton mFacebookLoginButton;

    private EditText userEmail;
    private EditText userPassword;

    private Button createAccount;

    private Button signInButton;

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
        loginButton = view.findViewById(R.id.fbLoginButton);
        mFacebookLoginButton = view.findViewById(R.id.hiddenFbButton);

        fragmentManager = getActivity().getSupportFragmentManager();

        signInButton.setOnClickListener(this);
        createAccount.setOnClickListener(this);
        loginButton.setOnClickListener(this);

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

//            mListener.onFragmentInteraction(uri);
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

                mListener.onUserLogin(userEmailString, userPasswordString);
        }

        else if (v == createAccount) {

            fragmentTransaction = fragmentManager.beginTransaction();

//            fragmentTransaction.replace(R.id.fragmentLayout, new RegisterFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        else if (v == loginButton) {

            callbackManager = CallbackManager.Factory.create();

            mFacebookLoginButton.performClick();
            mFacebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
            mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {

//                    mListener.facebookLogin(loginResult.getAccessToken());

                    Log.e("Permissions", loginResult.getRecentlyGrantedPermissions().toString());
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
        }
    }

    public interface OnFragmentInteractionListener {

        void onUserLogin(String credential, String password);
    }
}
