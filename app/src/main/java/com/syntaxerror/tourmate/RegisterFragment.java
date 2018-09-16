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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.syntaxerror.tourmate.database.DatabaseManager;
import com.syntaxerror.tourmate.pojos.SingleUser;

public class RegisterFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    Context mContext;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText firstName;
    private EditText lastName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userName;

    private Button registerButton;

    private DatabaseManager dbManager;

    private ProgressBar progressBar;

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

        firstName = view.findViewById(R.id.registerFirstName);
        lastName = view.findViewById(R.id.registerLastName);
        userEmail = view.findViewById(R.id.registerUserEmail);
        userPassword = view.findViewById(R.id.registerUserPassword);
        userName = view.findViewById(R.id.registerUserName);

        progressBar = view.findViewById(R.id.registerProgressBar);

        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        userEmail.setOnFocusChangeListener(this);

        dbManager = new DatabaseManager(mContext);

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

        progressBar.setVisibility(View.VISIBLE);

        if (v == registerButton) {

            String userFullNameString = firstName.getText().toString().trim() + lastName.getText().toString().trim();
            String userEmailString = userEmail.getText().toString().trim();
            String userPasswordString = userPassword.getText().toString().trim();
            String userNameString = userName.getText().toString().trim();

            progressBar.setVisibility(View.GONE);

            if (TextUtils.isEmpty(userEmailString) || TextUtils.isEmpty(userPasswordString) ||
                    TextUtils.isEmpty(userFullNameString) || TextUtils.isEmpty(userNameString))

                Toast.makeText(mContext, "Fields Can Not Contain Empty Fields!", Toast.LENGTH_LONG).show();

            else {

                String testMail = dbManager.getUserMail(userNameString);

                if (TextUtils.isEmpty(testMail)) {

                    if (dbManager.insertData(new SingleUser(userEmailString, userNameString))) {

                        OnFragmentInteractionListener data = (OnFragmentInteractionListener) mContext;
                        data.onUserRegistered(userEmailString, userPasswordString);
                    }
                }

                else {

                    Toast.makeText(mContext, "Email or Username Already Exists!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v == userEmail) {

            String generatedUserName = SingleUser.emailToName(userEmail.getText().toString().trim());

            if (generatedUserName == null) {

                userEmail.setError("Invalid Email Address!");
                registerButton.setClickable(false);
            }

            else {

                userName.setText(generatedUserName);
                registerButton.setClickable(true);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onUserRegistered(String userEmail, String userPassword);
    }
}
