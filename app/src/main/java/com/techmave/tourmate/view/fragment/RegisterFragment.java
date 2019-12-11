package com.techmave.tourmate.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.techmave.tourmate.R;
import com.techmave.tourmate.database.DatabaseManager;
import com.techmave.tourmate.pojo.FullName;
import com.techmave.tourmate.pojo.SingleUser;
import com.techmave.tourmate.utils.Utility;

public class RegisterFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private Context context;
    private OnFragmentInteractionListener listener;

    private EditText firstName;
    private EditText lastName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userReEnterPassword;
    private EditText userName;

    private Button registerButton;

    private DatabaseManager manager;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        firstName = view.findViewById(R.id.registerFirstName);
        lastName = view.findViewById(R.id.registerLastName);
        userEmail = view.findViewById(R.id.registerUserEmail);
        userPassword = view.findViewById(R.id.registerUserPassword);
        userReEnterPassword = view.findViewById(R.id.registerUserReEnterPassword);
        userName = view.findViewById(R.id.registerUserName);

        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        userEmail.setOnFocusChangeListener(this);

        manager = new DatabaseManager(context);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {

            listener = (OnFragmentInteractionListener) context;
        }

        else {

            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        this.context = context;
    }

    @Override
    public void onDetach() {

        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {

        if (v == registerButton) {

            String userFirstNameString = firstName.getText().toString().trim();
            String userLastNameString = lastName.getText().toString().trim();
            String userEmailString = userEmail.getText().toString().trim();
            String userPasswordString = userPassword.getText().toString().trim();
            String userReEnterPasswordString = userReEnterPassword.getText().toString().trim();
            String userNameString = userName.getText().toString().trim();

            if (TextUtils.isEmpty(userFirstNameString) || TextUtils.isEmpty(userLastNameString))

                Toast.makeText(context, "Please Enter Your Name", Toast.LENGTH_SHORT).show();

            else if (TextUtils.isEmpty(userEmailString))

                userEmail.setError("This field is required");

            else if (TextUtils.isEmpty(userPasswordString))

                userPassword.setError("Password can not be empty");

            else if (userPasswordString.length() < 6) {

                userPassword.setError("Password must contain at least 6 characters");
            }

            else if (!TextUtils.equals(userPasswordString, userReEnterPasswordString)) {

                userReEnterPassword.setError("Password doesn't match");
            }

            else {

                SingleUser singleUser = new SingleUser();
                listener.onUserRegistered(singleUser, userEmailString, userPasswordString);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v == userEmail) {

            String generatedUserName = Utility.getNameFromEmail(userEmail.getText().toString().trim());

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

        void onUserRegistered(SingleUser singleUser, String userEmail, String userPassword);
    }
}
