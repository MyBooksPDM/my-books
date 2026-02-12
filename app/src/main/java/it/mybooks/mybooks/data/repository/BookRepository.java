package it.mybooks.mybooks.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.mybooks.mybooks.data.local.AppDatabase;
import it.mybooks.mybooks.data.local.BookDao;
import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.utils.AppExecutors;

public class BookRepository {
    private static final String TAG = BookRepository.class.getName();

    private final BookDao bookDao;

    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.bookDao = db.bookDao();
    }

    public LiveData<List<Book>> getSavedBooks() {
        return bookDao.getAllBooks();
    }

    public LiveData<Book> getBookById(String id) {
        return bookDao.getBookById(id);
    }

    public void saveBook(Book book) {
        AppExecutors.getInstance().diskIO().execute(() -> bookDao.insert(book));
    }

    public void deleteBook(Book book) {
        AppExecutors.getInstance().diskIO().execute(() -> bookDao.delete(book));
    }
}
