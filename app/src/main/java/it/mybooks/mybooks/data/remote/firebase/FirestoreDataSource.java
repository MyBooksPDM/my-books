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
    public void saveBook(Book book) {
        CollectionReference collection = getSavedBooksCollection();
        if (collection == null) return;


        collection.document(book.getGid())
                .set(book)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Book saved successfully: " + book.getTitle()))
                .addOnFailureListener(e -> Log.e(TAG, "Error saving book", e));
    }

    /**
     * Remove a book from the user's Firestore collection
     */
    public void deleteBook(String bookGid) {
        CollectionReference collection = getSavedBooksCollection();
        if (collection == null) return;

        collection.document(bookGid)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Book deleted successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error deleting book", e));
    }

    /**
     * Get all saved books for the current user
     */
    public LiveData<List<Book>> getSavedBooks() {
        MutableLiveData<List<Book>> savedBooks = new MutableLiveData<>();
        CollectionReference collection = getSavedBooksCollection();

        if (collection != null) {
            collection.orderBy("savedTimestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.e(TAG, "Listen failed.", error);
                            return;
                        }

                        if (value != null) {
                            List<Book> books = value.toObjects(Book.class);
                            savedBooks.setValue(books);
                        }
                    });
        }

        return savedBooks;
    }
}
