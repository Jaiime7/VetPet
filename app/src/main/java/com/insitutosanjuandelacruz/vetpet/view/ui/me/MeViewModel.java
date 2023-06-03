package com.insitutosanjuandelacruz.vetpet.view.ui.me;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.insitutosanjuandelacruz.vetpet.view.MainActivity;

import java.io.File;
import java.io.IOException;

public class MeViewModel extends ViewModel {

    private MutableLiveData<String> imageUrl;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;

    public LiveData<String> getImageUrl() {
        if (imageUrl == null) {
            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();
            db = FirebaseFirestore.getInstance();
            imageUrl = new MutableLiveData<>();
            loadImageUrl();
        }
        return imageUrl;
    }

    private void loadImageUrl() {
        String userId = mCurrentUser.getUid();
        System.out.println(userId);
        usersCollection = db.collection("user");
        usersCollection.whereEqualTo("id", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String userPath = document.getString("profileImage");
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageReference.child("user/profile/" + userPath);
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String url = uri.toString();
                        imageUrl.setValue(url);
                    }).addOnFailureListener(e -> {
                        imageUrl.setValue(null);
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }
}