package it.mybooks.mybooks.ui;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import it.mybooks.mybooks.data.repository.UserRepository;

public class MainViewModel extends AndroidViewModel {
    private final String TAG = MainViewModel.class.getName();
    public LiveData<FirebaseUser> userSession;
    private final MutableLiveData<Boolean> isConnected;
    ConnectivityManager connectivityManager;

    public MainViewModel(@NonNull Application application) {
        super(application);
        UserRepository userRepo = UserRepository.getInstance();
        userSession = userRepo.getUser();
        connectivityManager = (ConnectivityManager) application.getSystemService(Application.CONNECTIVITY_SERVICE);
        isConnected = new MutableLiveData<>(false); // Will be updated by callback
        registerNetworkCallback();
    }

    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }

    public LiveData<FirebaseUser> getUserSession() {
        return userSession;
    }

    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Log.i(TAG, "onAvailable: Network is available");
            // Verify the network actually has internet capability
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            boolean hasInternet = capabilities != null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            isConnected.postValue(hasInternet);
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            // This is called when network capabilities change (e.g., validation completes)
            boolean hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            Log.i(TAG, "onCapabilitiesChanged: hasInternet=" + hasInternet);
            isConnected.postValue(hasInternet);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Log.i(TAG, "onLost: Network is lost");
            isConnected.postValue(false);
        }
    };

    private void registerNetworkCallback() {
        // registerDefaultNetworkCallback automatically calls onAvailable for current network
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Unregister the callback when ViewModel is cleared to avoid memory leaks
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }
}
