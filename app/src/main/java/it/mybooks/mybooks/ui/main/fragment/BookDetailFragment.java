package it.mybooks.mybooks.ui.main.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.ui.main.viewmodel.BookViewModel;

public class BookDetailFragment extends Fragment {

    private BookViewModel bookViewModel;
    private Book currentBook;
    private boolean isBookSaved = false;
    private ImageView bookCoverImage;
    private TextView bookTitle;
    private TextView bookSubtitle;
    private TextView bookAuthor;
    private TextView bookPublisherYear;
    private TextView bookRating;
    private TextView bookRatingsCount;
    private LinearLayout ratingSection;
    private TextView bookIsbn;
    private TextView bookPages;
    private TextView bookLanguage;
    private TextView bookCategories;
    private TextView bookDescription;
    private LinearLayout pagesSection;
    private LinearLayout languageSection;
    private LinearLayout categoriesSection;
    private MaterialCardView descriptionCard;
    private ExtendedFloatingActionButton fabSaveBook;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        initializeViews(view);

        if (getArguments() != null) {
            BookDetailFragmentArgs args = BookDetailFragmentArgs.fromBundle(getArguments());
            currentBook = args.getSelectedBook();
        }

        if (currentBook != null) {
            displayBookData(currentBook);
            checkIfBookIsSaved();
            setupFabListener();
        }
    }

    private void setupFabListener() {
        fabSaveBook.setOnClickListener(v -> {
            if (isBookSaved) {
                // Delete the book from database
                bookViewModel.deleteBook(currentBook);
                isBookSaved = false;
                Toast.makeText(getContext(), "Libro rimosso dalla libreria", Toast.LENGTH_SHORT).show();
            } else {
                // Save the book to database
                bookViewModel.saveBook(currentBook);
                isBookSaved = true;
                Toast.makeText(getContext(), "Libro salvato in libreria", Toast.LENGTH_SHORT).show();
            }
            updateFabState();
        });
    }

    private void checkIfBookIsSaved() {
        bookViewModel.getBookById(currentBook.getGid()).observe(getViewLifecycleOwner(), book -> {
            isBookSaved = (book != null);
            updateFabState();
        });
    }

    private void displayBookData(Book book) {
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthorsAsString());
        bookDescription.setText(book.getDescription());
        bookPublisherYear.setText(book.getPublicationYear());

        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            bookCategories.setText(TextUtils.join(", ", book.getCategories()));
        } else {
            bookCategories.setText("-");
        }

        String isbn = book.getPrimaryIsbn();
        if (isbn != null) {
            bookIsbn.setText(getString(R.string.isbn_format, isbn));
        } else {
            bookIsbn.setText("-");
        }

        // Load cover image using your preferred image loading library
        String imageUrl = book.getThumbnail();
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = book.getSmallThumbnail();
        }

        Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(bookCoverImage);
    }

    private void initializeViews(View view) {
        bookCoverImage = view.findViewById(R.id.coverImageView);
        bookTitle = view.findViewById(R.id.titleTextView);
        bookAuthor = view.findViewById(R.id.authorTextView);
        bookPublisherYear = view.findViewById(R.id.publicationYearTextView);
        bookIsbn = view.findViewById(R.id.isbnTextView);
        bookCategories = view.findViewById(R.id.genreTextView);
        bookDescription = view.findViewById(R.id.descriptionTextView);
//        bookSubtitle = view.findViewById(R.id.book_subtitle);
//        bookRating = view.findViewById(R.id.book_rating);
//        bookRatingsCount = view.findViewById(R.id.book_ratings_count);
//        ratingSection = view.findViewById(R.id.rating_section);
//        bookPages = view.findViewById(R.id.book_pages);
//        bookLanguage = view.findViewById(R.id.book_language);
//        pagesSection = view.findViewById(R.id.pages_section);
//        languageSection = view.findViewById(R.id.language_section);
//        categoriesSection = view.findViewById(R.id.categories_section);
//        descriptionCard = view.findViewById(R.id.description_card);
        fabSaveBook = view.findViewById(R.id.fab_save_book);
    }

    private void updateFabState() {
        if (isBookSaved) {
            // Book is saved - show filled/red icon
            fabSaveBook.setIconResource(android.R.drawable.ic_menu_close_clear_cancel);
//            fabSaveBook.setText(R.string.fab_remove_from_library);
        } else {
            // Book is not saved - show outline/plus icon
            fabSaveBook.setIconResource(android.R.drawable.ic_menu_add);
//            fabSaveBook.setText(R.string.fab_add_to_library);
        }
    }
}