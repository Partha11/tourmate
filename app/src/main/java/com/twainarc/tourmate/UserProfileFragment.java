package com.twainarc.tourmate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.twainarc.tourmate.model.CircleTransform;
import com.twainarc.tourmate.model.FullName;
import com.twainarc.tourmate.model.SingleUser;
import com.twainarc.tourmate.model.StaticData;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private EditText firstNameView;
    private EditText lastNameView;
    private TextView emailView;
    private TextView userNameView;
    private EditText passwordView;
    private EditText newPasswordView;
    private EditText userAgeView;
    private TextView totalEventsView;
    private TextView totalExpenseView;

    private ImageView profileImage;
    private ImageView addProfileImage;

    private String userPassword;
    private String userId;

    private RadioGroup genderGroup;
    private Button mUpdateButton;

    private SingleUser singleUser;

    private DatabaseReference dbReference;
    private static Uri filePath;
    private static Uri finalFilePath;
    private StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 7;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        storageReference = FirebaseStorage.getInstance().getReference().child("gg");

        singleUser =  mListener.getUserInfo();
        dbReference = FirebaseDatabase.getInstance().getReference();
        readPrefs();

        Log.d("UserProfile", singleUser.getProfileImage());

        firstNameView = view.findViewById(R.id.profileFirstName);
        lastNameView = view.findViewById(R.id.profileLastName);
        emailView = view.findViewById(R.id.profileEmail);
        userNameView = view.findViewById(R.id.profileUserName);
        userAgeView = view.findViewById(R.id.profileAge);
        passwordView = view.findViewById(R.id.profileCurrentPassword);
        newPasswordView = view.findViewById(R.id.profilePassword);
        totalEventsView = view.findViewById(R.id.profileTotalEvents);
        totalExpenseView = view.findViewById(R.id.profileTotalExpenses);
        mUpdateButton = view.findViewById(R.id.profileUpdate);
        genderGroup = view.findViewById(R.id.profileGender);

        addProfileImage = view.findViewById(R.id.profileAddIcon);
        profileImage = view.findViewById(R.id.profileUserPhoto);

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();

        addProfileImage.setOnClickListener(this);

        if (TextUtils.equals(singleUser.getUserGender(), "Male"))

            genderGroup.check(R.id.maleButton);

        else if (TextUtils.equals(singleUser.getUserGender(), "Female"))

            genderGroup.check(R.id.femaleButton);

        else

            genderGroup.check(R.id.maleButton);

        mUpdateButton.setOnClickListener(this);

        String event = String.valueOf(StaticData.getNumberOfEvents()) + " Event";
        String expense = String.valueOf(StaticData.getTotalExpenseAmount()) + " TK Used";

        firstNameView.setText("gg");
        lastNameView.setText("wp");
        emailView.setText(singleUser.getUserMail());
        userNameView.setText(singleUser.getUserName());
        userAgeView.setText(singleUser.getUserAge());
        totalEventsView.setText(event);
        totalExpenseView.setText(expense);

        if (singleUser.getProfileImage() != null) {

            StorageReference profilePhoto = FirebaseStorage.getInstance().getReference().child("gg");

            profilePhoto.child("profile/" + "gg").getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            progressDialog.dismiss();
                            loadImageFromUri(uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Log.d("UserProfile", e.getLocalizedMessage());
                }
            });
        }

        return view;
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

        if (v == mUpdateButton) {

            String firstName = firstNameView.getText().toString().trim();
            String lastName = lastNameView.getText().toString().trim();
            String currentPassword = passwordView.getText().toString().trim();
            String newPassword = newPasswordView.getText().toString().trim();
            String mUserAge = userAgeView.getText().toString().trim();
            String mGender;

            switch (genderGroup.getCheckedRadioButtonId()) {

                case R.id.maleButton:

                    mGender = "Male";
                    break;

                case R.id.femaleButton:

                    mGender = "Female";
                    break;

                default:

                    mGender = "Male";
                    break;
            }

            if (userPassword == null) {

                passwordView.setClickable(false);
                newPasswordView.setClickable(false);
            }

            else {

                passwordView.setClickable(true);
                newPasswordView.setClickable(true);
            }

            if (TextUtils.isEmpty(firstName))

                firstNameView.setError("Please Provide Your Name");

            else if (TextUtils.isEmpty(lastName))

                lastNameView.setError("Please Provide Your Name");

            else if (TextUtils.isEmpty(mUserAge))

                userAgeView.setError("Please Provide Your Age");

            else if (userPassword != null && !TextUtils.isEmpty(newPassword)) {

                if (TextUtils.isEmpty(currentPassword))

                    passwordView.setError("Please Enter Your Current Password");

                else {

                    if (!TextUtils.equals(userPassword, currentPassword)) {

                        passwordView.setError("Wrong Password");
                    }
                }
            }

            else {

                Log.d("UserProfile", mGender);

                SingleUser mSingleUser = new SingleUser();

                uploadImage();

                dbReference.child(userId).child("fullName").setValue(new FullName(firstName, lastName));
                dbReference.child(userId).child("userAge").setValue(mUserAge);
                dbReference.child(userId).child("userGender").setValue(mGender);
                dbReference.child(userId).child("profileImage").setValue(userId);
            }
        }

        else if (v == addProfileImage) {

            filePath = null;
            chooseImage();
        }
    }

    private void loadImageFromUri(Uri uri) {

        Picasso.get()
                .load(uri)
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(profileImage);
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if(finalFilePath != null)  {

            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();

            StorageReference ref = storageReference.child("profile/" + "gg");

            ref.putFile(finalFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null ) {

            filePath = data.getData();

            CropImage.activity(filePath)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setBorderLineColor(Color.GREEN)
                    .setMinCropResultSize(400,400)
                    .setMaxCropResultSize(2000,2000)
                    .start(getContext(), this);
        }

        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                finalFilePath = result.getUri();
                loadImageFromUri(finalFilePath);
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }

    private void readPrefs() {

/*        SharedPreferences prefs = mContext.getSharedPreferences(LoginActivity.USER_PREF_NAME, Context.MODE_PRIVATE);
        userPassword = prefs.getString(LoginActivity.PREF_USER_PASSWORD, null);
        userId = prefs.getString(LoginActivity.PREF_USER_ID, "1");*/

        Log.e("Message", userPassword);
    }

    public interface OnFragmentInteractionListener {

        SingleUser getUserInfo();
    }
}
