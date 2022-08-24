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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView textViewUsername, textViewEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//                    textViewUsername = findViewById(R.id.textView_main_test);
//                    textViewEmail = findViewById(R.id.textView_main_test2);
//
//                    textViewUsername.setText(user.getUsername());
//                    textViewEmail.setText(user.getEmail());
                }
            });
        }

//        Button buttonLogout = findViewById(R.id.button_main_logoutButton);
//        buttonLogout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                logoutUser();
//            }
//        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_2);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // https://www.youtube.com/watch?v=PiExmkR3aps
                FragmentManager fragmentManager = getSupportFragmentManager();

                if (item.getItemId() == R.id.page_1) {
                    //dummytext.setText("WUBALUBADUBDUB");
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main_containerView, BookmarkFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name")
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.page_2) {
                    //dummytext.setText("I don't give a fuck");
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main_containerView, MapFragment.class, null)
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



    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}