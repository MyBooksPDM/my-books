package it.mybooks.mybooks.ui.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.utils.SampleBookData;

public class BookDetailViewModel extends ViewModel {

    private final MutableLiveData<Book> book = new MutableLiveData<>();

    public MutableLiveData<Book> getBook() {
        return book;
    }

    public void loadBookDetails(String bookId){
        if (bookId == null || bookId.isEmpty()) {
            return;
        }
        // Simulate loading book details (in a real app, fetch from repository or database)
        List<Book> books = SampleBookData.getSampleBooks();
        for(Book b : books){
            if(bookId.equals(b.getId())){
                book.setValue(b);
                break;
            }
        }
    }

}