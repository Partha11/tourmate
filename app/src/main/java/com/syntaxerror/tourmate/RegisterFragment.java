package com.syntaxerror.tourmate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    Context mContext;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText fullName;
    private EditText userEmail;
    private EditText userPassword;

    private Button registerButton;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.register_title);

        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        fullName = view.findViewById(R.id.registerName);
        userEmail = view.findViewById(R.id.registerUserEmail);
        userPassword = view.findViewById(R.id.registerUserPassword);

        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        return view;
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
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        if (v == registerButton) {

            String userEmailString = userEmail.getText().toString().trim();
            String userPasswordString = userPassword.getText().toString().trim();

            if (TextUtils.isEmpty(userEmailString) || TextUtils.isEmpty(userPasswordString))

                Toast.makeText(mContext, "Empty!!", Toast.LENGTH_SHORT).show();

            else {

                OnFragmentInteractionListener data = (OnFragmentInteractionListener) mContext;
                data.onUserRegistered(userEmailString, userPasswordString);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onUserRegistered(String userEmail, String userPassword);
    }
}
