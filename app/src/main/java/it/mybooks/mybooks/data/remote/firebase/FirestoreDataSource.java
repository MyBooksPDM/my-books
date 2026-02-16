package it.mybooks.mybooks.data.remote.firebase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.utils.Constants;

public class FirestoreDataSource {
    private static final String TAG = "FirestoreDataSource";
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public FirestoreDataSource() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    /**
     * Get the current user's saved books collection reference
     */
    private CollectionReference getSavedBooksCollection() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return null;

        return db.collection(Constants.FirestoreCollections.USERS_COLLECTION)
                .document(user.getUid())
                .collection(Constants.FirestoreCollections.BOOKS_COLLECTION);
    }

    /**
     * Save a book to the user's Firestore collection
     */
    public void saveBook(Book book, FirestoreChangeCallback callback) {
        CollectionReference collection = getSavedBooksCollection();
        if (collection == null) return;


        collection.document(book.getGid())
                .set(book)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Book saved successfully: " + book.getTitle());
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving book", e);
                    callback.onError(e.getMessage() != null ? e.getMessage() : "Unknown error");
                });
    }

    /**
     * Remove a book from the user's Firestore collection
     */
    public void deleteBook(String bookGid, FirestoreChangeCallback callback) {
        CollectionReference collection = getSavedBooksCollection();
        if (collection == null) return;

        collection.document(bookGid)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Book deleted successfully: " + bookGid);
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG,"Error deleting book", e);
                    callback.onError(e.getMessage() != null ? e.getMessage() : "Unknown error");
                });
    }


    /**
     * Get all saved books for the current user
     */
    public void getSavedBooks(FirestoreGetBooksCallback callback) {
        CollectionReference collection = getSavedBooksCollection();

        if (collection == null) {
            callback.onError("Utente non autenticato");
            return;
        }

        collection.orderBy("savedTimestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Book> books = queryDocumentSnapshots.toObjects(Book.class);
                    callback.onSuccess(books);
                })
                .addOnFailureListener(e -> {
                    callback.onError(e.getMessage() != null ? e.getMessage() : "Errore remoto");
                });
    }


    public interface FirestoreGetBooksCallback {
        void onSuccess(List<Book> books);
        void onError(String message);
    }


    public interface FirestoreChangeCallback {
        void onSuccess();

        void onError(String message);
    }
}
