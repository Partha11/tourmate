package com.twainarc.tourmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.twainarc.tourmate.adapters.GalleryAdapter;
import com.twainarc.tourmate.database.DatabaseManager;
import com.twainarc.tourmate.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class MemoriesFragment extends Fragment {

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

    private static List<Event> eventList;

    private DatabaseManager dbManager;

    private String eventId;

    private Spinner mSpinner;

    public MemoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memories, container, false);

        dbMemoryNode = FirebaseDatabase.getInstance().getReference().child("gg").child("Memories");
        storageReference = FirebaseStorage.getInstance().getReference().child("gg");

        mSpinner = view.findViewById(R.id.memoriesSpinner);

        eventList = dbManager.getAllEventsData();
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

        if (eventList != null) {

            Log.d("MemoriesFragment EvList", String.valueOf(eventList.size()));

            List<String> nameList = new ArrayList<>();

            for (Event i : eventList) {

                String data = i.getDescription() + "(" + i.getFromDate() + ")";
                nameList.add(data);
                eventIdList.add("gg");
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
}
