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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.ui.main.viewmodel.BookViewModel;

public class BookDetailFragment extends Fragment {

    private BookViewModel mViewModel;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView descriptionTextView;
    private TextView yearTextView;
    private TextView genreTextView;
    private TextView isbnTextView;

    private ImageView coverImageView;

    public static BookDetailFragment newInstance() {
        return new BookDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        mViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        if (getArguments() != null) {
            String bookId = getArguments().getString("id");

            mViewModel.loadBookDetails(bookId);

            // Initialize views
            titleTextView = view.findViewById(R.id.titleTextView);
            authorTextView = view.findViewById(R.id.authorTextView);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
            coverImageView = view.findViewById(R.id.coverImageView);
            yearTextView = view.findViewById(R.id.publicationYearTextView);
            genreTextView = view.findViewById(R.id.genreTextView);
            isbnTextView = view.findViewById(R.id.isbnTextView);


            // Observe book details
            mViewModel.getBook().observe(getViewLifecycleOwner(), book -> {
                if (book == null) {
                    return;
                }

                titleTextView.setText(book.getTitle());
                authorTextView.setText(book.getAuthorsAsString());
                descriptionTextView.setText(book.getDescription());
                yearTextView.setText(book.getPublicationYear());

                if (book.getCategories() != null && !book.getCategories().isEmpty()) {
                    genreTextView.setText(TextUtils.join(", ", book.getCategories()));
                } else {
                    genreTextView.setText("-");
                }

                String isbn = book.getPrimaryIsbn();
                if (isbn != null) {
                    isbnTextView.setText(getString(R.string.isbn_format, isbn));
                } else {
                    isbnTextView.setText("-");
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
                        .into(coverImageView);
            });
        }
    }
}