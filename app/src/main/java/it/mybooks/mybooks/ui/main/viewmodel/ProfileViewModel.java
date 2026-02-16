package it.mybooks.mybooks.ui.main.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {

    private final FirebaseAuth auth;

    public ProfileViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void logout() {
        auth.signOut();
    }

    public LiveData<Boolean> deleteAccount() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                result.setValue(task.isSuccessful());
            });
        } else {
            result.setValue(false);
        }

        return result;
    }
}


