package it.mybooks.mybooks.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.mybooks.mybooks.R;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getName();
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(findViewById(R.id.toolbar));

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);

        // 2. Ottieni il NavController
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.welcomeFragment,
                    R.id.libraryFragment,
                    R.id.searchFragment
            ).build();

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.welcomeFragment || destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment) {
                    findViewById(R.id.bottomNav).setVisibility(View.GONE);
                    findViewById(R.id.toolbar).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.bottomNav).setVisibility(View.VISIBLE);
                    findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
                }
            });

            // 3. Collega la ActionBar al NavController
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            // 4. Collega la BottomNavigationView al NavController
            BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
            NavigationUI.setupWithNavController(bottomNav, navController);
        }

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.userSession.observe(this, user -> {
            if (user != null) {
                Log.d(TAG, "onCreate: user logged in: " + user.getUid());
                // User is logged in!
//                String userId = user.getUid();

                // 1. Initialize your Repository with the new User ID
                // 2. Start the Firestore Sync for "users/{userId}/books"
                navigateToFragment(R.id.libraryFragment);
            } else {
                Log.d(TAG, "onCreate: user logged out");
                // User is logged out!
                // Clear local sensitive data or stop syncing
                navigateToFragment(R.id.welcomeFragment);
            }
        });
    }

    private void navigateToFragment(int destinationId) {
        navController.navigate(destinationId, null,
                new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true) // Clear the entire back stack
                        .build());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}