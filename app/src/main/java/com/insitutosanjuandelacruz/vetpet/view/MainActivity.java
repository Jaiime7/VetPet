package com.insitutosanjuandelacruz.vetpet.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore db;
    CollectionReference usersCollection;

    ImageView imageView;
    TextView textViewEmail;
    TextView textViewUserName;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();
        imageView = binding.navView.getHeaderView(0).findViewById(R.id.imageViewProfileHeader);
        textViewEmail = binding.navView.getHeaderView(0).findViewById(R.id.textViewEmailNav);
        textViewUserName = binding.navView.getHeaderView(0).findViewById(R.id.textViewUsernameNav);
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("user");
        setEmail();
        setUsername();
        setProfileImage();
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_pets, R.id.nav_appointments, R.id.nav_history_medical, R.id.nav_me).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setEmail() {
        textViewEmail.setText(mCurrentUser.getEmail());
    }

    private void setProfileImage() {
        String userId = mCurrentUser.getUid();
        usersCollection.whereEqualTo("id", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String userPath = document.getString("profileImage");
                    StorageReference imageRef = storageRef.child("user/profile/" + userPath);
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String url = uri.toString();
                        Glide.with(MainActivity.this)
                                .load(url)
                                .into(imageView);
                    }).addOnFailureListener(e -> {
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void setUsername() {
        String userId = mCurrentUser.getUid();
        setSupportActionBar(binding.appBarMain.toolbar);
        usersCollection.whereEqualTo("id", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String name = document.getString("name");
                    textViewUserName.setText(name);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}