package com.syntaxerror.tourmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.syntaxerror.tourmate.adapters.GalleryAdapter;
import com.syntaxerror.tourmate.database.DatabaseManager;
import com.syntaxerror.tourmate.pojos.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class MemoriesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private static List<Uri> imageLinks;
    private static List<String> eventIdList;
    private static List<Uri> imageDataList;

    private DatabaseReference dbMemoryNode;

    private ListView mGalleryView;
    private GalleryAdapter mGalleryAdapter;

    private Button savePhoto;

    private static Uri filePath;
    private StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 7;

    private static List<Events> eventsList;

    private DatabaseManager dbManager;

    private String eventId;

    private Spinner mSpinner;

    public MemoriesFragment() {
        // Required empty public constructor
    }

    public static MemoriesFragment newInstance(String param1, String param2) {
        MemoriesFragment fragment = new MemoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        eventIdList = new ArrayList<>();
        imageLinks = new ArrayList<>();
        imageDataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memories, container, false);

        dbMemoryNode = FirebaseDatabase.getInstance().getReference().child(UpdatedMainMenuActivity.userId).child("Memories");
        storageReference = FirebaseStorage.getInstance().getReference().child(UpdatedMainMenuActivity.userId);

        mSpinner = view.findViewById(R.id.memoriesSpinner);

        eventsList = dbManager.getAllEventsData();
        initSpinner();

        imageLinks = dbManager.getAllMemories();

        mGalleryView = view.findViewById(R.id.galleryView);
        mGalleryAdapter = new GalleryAdapter(mContext, R.layout.memories_model, imageLinks);
        mGalleryView.setAdapter(mGalleryAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                eventId = eventIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        savePhoto = view.findViewById(R.id.savePhoto);
        savePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                chooseImage();
                uploadImage(eventId);
                imageLinks = dbManager.getAllMemories();
                mGalleryAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void initSpinner() {

        if (eventsList != null) {

            Log.d("MemoriesFragment EvList", String.valueOf(eventsList.size()));

            List<String> nameList = new ArrayList<>();

            for (Events i : eventsList) {

                String data = i.getTravelDescription() + "(" + i.getFromDate() + ")";
                nameList.add(data);
                eventIdList.add(i.getEventId());
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, nameList);
            mSpinner.setAdapter(spinnerAdapter);
        }
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(final String eventId) {

        if(imageDataList != null)  {

            for (final Uri localData : imageDataList) {

                final String uid = UUID.randomUUID().toString();
                final StorageReference ref = storageReference.child("events/" + eventId + "/" + uid);

                ref.putFile(localData)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(mContext, "Added", Toast.LENGTH_SHORT).show();

                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                    @Override
                                    public void onSuccess(Uri uri) {

                                        dbManager.insertSingleMemory(eventId, uri.toString());
                                        Log.d("Memories", uri.toString());
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(mContext, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null ) {

            filePath = data.getData();
            imageDataList.add(data.getData());
        }
    }

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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        mContext = context;
        dbManager = new DatabaseManager(context);

        getActivity().setTitle("Memories");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
