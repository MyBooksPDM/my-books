package it.mybooks.mybooks.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import it.mybooks.mybooks.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private BookAdapter bookAdapter;

    private TextView savedBooksCountTextView;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Initialize views
        RecyclerView recyclerView = view.findViewById(R.id.profile_recycler_view);
        savedBooksCountTextView = view.findViewById(R.id.saved_books_count);

        // Setup RecyclerView
        bookAdapter = new BookAdapter();
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set click listener for book items
        bookAdapter.setOnBookClickListener(book -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", book.getId());
            // Navigate to detail fragment when a book is clicked
            Navigation.findNavController(view)
                    .navigate(R.id.action_profileFragment_to_bookDetailFragment, bundle);
        });

        bookAdapter.setOnBookLongClickListener(book -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Remove book")
                    .setMessage("Do you want to remove \"" + book.getTitle() + "\" from your saved books?")
                    .setPositiveButton("Remove", (dialog, which) ->
                        mViewModel.deleteBook(book.getId())
                    )
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Observe books data from ViewModel
        mViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
                    bookAdapter.setBooks(books);
                    // Update saved books count
                    if (books != null) {
                        savedBooksCountTextView.setText(String.valueOf(books.size()));
                    } else {
                        savedBooksCountTextView.setText("0");
                    }
        });

        mViewModel.getBookCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null && savedBooksCountTextView != null) {
                String countText = count + " saved books";
                if (count == 1) {
                    countText = "1 saved book";
                }
                savedBooksCountTextView.setText(countText);
            }
        });

    }
}