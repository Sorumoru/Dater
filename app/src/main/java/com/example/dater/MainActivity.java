package com.example.dater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {

    public static final String LATITUDE = "com.example.dater_latitude";
    public static final String LONGITUDE = "com.example.dater_longitude";

    double latitude = 0;
    double longitude = 0;

    private static final String TAG = "MainActivity";
    TextView textViewUsername, textViewEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticateUser();

//        Button buttonLogout = findViewById(R.id.button_main_logoutButton);
//        buttonLogout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                logoutUser();
//            }
//        });

        Button addButton = findViewById(R.id.button_main_add);
        addButton.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, AddSpotActivity.class);
            Bundle bundle = new Bundle();

            bundle.putDouble(LATITUDE, latitude);
            bundle.putDouble(LONGITUDE, longitude);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        bottomNavigation();
    }

    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    private void bottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_2);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // https://www.youtube.com/watch?v=PiExmkR3aps
                FragmentManager fragmentManager = getSupportFragmentManager();

                if (item.getItemId() == R.id.page_1) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main_containerView, BookmarkFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.page_2) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main_containerView, MapsFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.page_3) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main_containerView, ProfileFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
    private void authenticateUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {

            DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                }
            });
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}