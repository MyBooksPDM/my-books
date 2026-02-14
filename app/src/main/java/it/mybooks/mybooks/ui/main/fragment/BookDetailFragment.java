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
                Toast.makeText(getContext(), R.string.book_removed, Toast.LENGTH_SHORT).show();
            } else {
                // Save the book to database
                bookViewModel.saveBook(currentBook);
                isBookSaved = true;
                Toast.makeText(getContext(), R.string.book_saved, Toast.LENGTH_SHORT).show();
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
        // Load book cover image
        String imageUrl = book.getThumbnail();
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = book.getSmallThumbnail();
        }

        Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(bookCoverImage);

        // Set title (always visible)
        bookTitle.setText(book.getTitle());

        // Set subtitle (show only if exists)
        if (book.getSubtitle() != null && !book.getSubtitle().isEmpty()) {
            bookSubtitle.setText(book.getSubtitle());
            bookSubtitle.setVisibility(View.VISIBLE);
        } else {
            bookSubtitle.setVisibility(View.GONE);
        }

        // Set author
        bookAuthor.setText(book.getAuthorsAsString());

        // Set publisher and year
        bookPublisherYear.setText(getString(R.string.book_publisher_year, book.getPublisher(), book.getPublicationYear()));

        // Set rating (show only if rating exists)
        if (book.getAverageRating() > 0) {
            bookRating.setText(String.format("%.1f/5", book.getAverageRating()));
            bookRatingsCount.setText(getString(R.string.ratings, book.getRatingsCount()));
            ratingSection.setVisibility(View.VISIBLE);
        } else {
            ratingSection.setVisibility(View.GONE);
        }

        // Set ISBN
        String isbn = book.getPrimaryIsbn();
        if (isbn != null && !isbn.isEmpty()) {
            bookIsbn.setText(isbn);
        } else {
            bookIsbn.setText("-");
        }

        // Set pages (show only if page count exists)
        if (book.getPageCount() > 0) {
            bookPages.setText(getString(R.string.pages, book.getPageCount()));
            pagesSection.setVisibility(View.VISIBLE);
        } else {
            pagesSection.setVisibility(View.GONE);
        }

        // Set language (show only if exists)
        if (book.getLanguage() != null && !book.getLanguage().isEmpty()) {
            bookLanguage.setText(book.getLanguage());
            languageSection.setVisibility(View.VISIBLE);
        } else {
            languageSection.setVisibility(View.GONE);
        }

        // Set categories (show only if exists)
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            bookCategories.setText(TextUtils.join(", ", book.getCategories()));
            categoriesSection.setVisibility(View.VISIBLE);
        } else {
            categoriesSection.setVisibility(View.GONE);
        }

        // Set description (show only if exists)
        if (book.getDescription() != null && !book.getDescription().isEmpty()) {
            bookDescription.setText(book.getDescription());
            descriptionCard.setVisibility(View.VISIBLE);
        } else {
            descriptionCard.setVisibility(View.GONE);
        }
    }

    private void initializeViews(View view) {
        bookCoverImage = view.findViewById(R.id.book_cover_image);
        bookTitle = view.findViewById(R.id.book_title);
        bookSubtitle = view.findViewById(R.id.book_subtitle);
        bookAuthor = view.findViewById(R.id.book_author);
        bookPublisherYear = view.findViewById(R.id.book_publisher_year);
        bookRating = view.findViewById(R.id.book_rating);
        bookRatingsCount = view.findViewById(R.id.book_ratings_count);
        ratingSection = view.findViewById(R.id.rating_section);
        bookIsbn = view.findViewById(R.id.book_isbn);
        bookPages = view.findViewById(R.id.book_pages);
        pagesSection = view.findViewById(R.id.pages_section);
        bookLanguage = view.findViewById(R.id.book_language);
        languageSection = view.findViewById(R.id.language_section);
        bookCategories = view.findViewById(R.id.book_categories);
        categoriesSection = view.findViewById(R.id.categories_section);
        bookDescription = view.findViewById(R.id.book_description);
        descriptionCard = view.findViewById(R.id.description_card);
        fabSaveBook = view.findViewById(R.id.fab_save_book);
    }

    private void updateFabState() {
        if (isBookSaved) {
            fabSaveBook.setIconResource(R.drawable.round_remove_24);
            fabSaveBook.setText(R.string.save_book);
        } else {
            fabSaveBook.setIconResource(R.drawable.round_add_24);
            fabSaveBook.setText(R.string.remove_book);
        }
    }
}