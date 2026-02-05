package it.mybooks.mybooks.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.mybooks.mybooks.model.Book;
import it.mybooks.mybooks.utils.SampleBookData;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();
    private final MutableLiveData<Integer> bookCount = new MutableLiveData<>();

    public ProfileViewModel() {
        List<Book> list = SampleBookData.getSampleBooks();
        books.setValue(list);
        bookCount.setValue(list.size());
    }

    public LiveData<Integer> getBookCount() {
        return bookCount;
    }
    public LiveData<List<Book>> getBooks() {
        return books;
    }

    public void deleteBook(String bookId) {
        List<Book> currentBook = books.getValue();
        if(currentBook == null){
            return;
        }
        List<Book> updatedBooks = new ArrayList<>();
        for(Book book : currentBook){
            if(!book.getId().equals(bookId)){
                updatedBooks.add(book);
            }
        }
        books.setValue(updatedBooks);
    }
}
