package com.insitutosanjuandelacruz.vetpet.view.ui.me;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentMeBinding;
import com.insitutosanjuandelacruz.vetpet.view.LoginActivity;
import com.insitutosanjuandelacruz.vetpet.view.MainActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MeFragment extends Fragment {

    private FragmentMeBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProfile;
    private ImageView imageViewEdit;

    TextView textViewAgeMe;
    StorageReference storageRef;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MeViewModel meViewModel = new ViewModelProvider(this).get(MeViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("user/profile/profileImages");
        textViewAgeMe = binding.textViewAge;
        meViewModel.getImageUrl().observe(getViewLifecycleOwner(), imageUrl -> {
            // Carga la imagen utilizando Glide
            Glide.with(requireContext()).load(imageUrl).into(binding.imageViewPro);
        });
        CollectionReference userRef = FirebaseFirestore.getInstance().collection("user");
        userRef.whereEqualTo("id",FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Timestamp date = document.getTimestamp("birthdate");
                    textViewAgeMe.setText(calculateAge(date));
                }
            }
        });
        imageViewEdit = binding.imageViewEdit;
        imageViewProfile = binding.imageViewPro;
        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código para abrir la galería y seleccionar una foto
                openGallery();
            }
        });

        return root;
    }

    private String calculateAge(Timestamp date) {
        Date birthdateDate = date.toDate();
        Calendar birthdateCalendar = Calendar.getInstance();
        birthdateCalendar.setTime(birthdateDate);

        Calendar currentCalendar = Calendar.getInstance();

        int years = currentCalendar.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR);
        int months = currentCalendar.get(Calendar.MONTH) - birthdateCalendar.get(Calendar.MONTH);
        int days = currentCalendar.get(Calendar.DAY_OF_MONTH) - birthdateCalendar.get(Calendar.DAY_OF_MONTH);

        if (months < 0 || (months == 0 && days < 0)) {
            years--;
        }

        return years+" years";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            deleteActualPhoto();
            String filename = UUID.randomUUID().toString();
            Uri selectedImageUri = data.getData();
            imageViewProfile.setImageURI(selectedImageUri);
            StorageReference imageRef = storageRef.child(filename);
            InputStream inputStream;
            try {
                inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            UploadTask uploadTask = imageRef.putStream(inputStream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    CollectionReference userCollectionRef = firestore.collection("user");
                    Query query = userCollectionRef.whereEqualTo("id", uid);
                    query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            DocumentReference userRef = userCollectionRef.document(documentId);
                            String newPhotoUrl = "profileImages/" + filename;
                            userRef.update("profileImage", newPhotoUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(requireContext(), "Update userPhoto", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(requireContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    requireActivity().finish();
                                    storageRef = storage.getReference().child("user/profile/");

                                    storageRef.delete();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(requireContext(), "Not found document", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        }
    }

    private void deleteActualPhoto() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        CollectionReference userCollectionRef = firestore.collection("user");
        Query query = userCollectionRef.whereEqualTo("id", uid);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                String documentId = documentSnapshot.getId();
                DocumentReference userRef = userCollectionRef.document(documentId);
                userRef.get().addOnSuccessListener(documentSnapshot2 -> {
                    if (documentSnapshot2.exists()) {
                        String fieldValue = documentSnapshot2.getString("profileImage");
                        if (!fieldValue.equals("default_profile_image.jpg")) {
                            storageRef = storage.getReference().child("user/profile/" + fieldValue);
                            storageRef.delete();
                        }
                    } else {
                    }
                }).addOnFailureListener(e -> {
                });
            }
        }).addOnFailureListener(e -> {
        });
    }
}
