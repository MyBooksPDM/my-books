package it.mybooks.mybooks.ui.main.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.ui.main.adapter.BookAdapter;
import it.mybooks.mybooks.ui.main.viewmodel.BookViewModel;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private TextView savedBooksCountTextView;
    private BookViewModel bookViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.profile_recycler_view);
        savedBooksCountTextView = view.findViewById(R.id.saved_books_count);

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        setupRecyclerView();

        // Observe books data from ViewModel
        bookViewModel.getSavedBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
                updateSavedBooksCount(books.size());
            }
        });
    }

    private void updateSavedBooksCount(int count) {
        if (count == 0) {
            savedBooksCountTextView.setText("library is empty");
        } else {
            savedBooksCountTextView.setText(count + " saved books");
        }
    }

    private void setupRecyclerView() {
        bookAdapter = new BookAdapter();
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookAdapter.setOnBookClickListener(book -> {
            ProfileFragmentDirections.ActionProfileFragmentToBookDetailFragment action =
                    ProfileFragmentDirections.actionProfileFragmentToBookDetailFragment(book);
            NavHostFragment.findNavController(ProfileFragment.this).navigate(action);
        });

        bookAdapter.setOnBookLongClickListener(book -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Remove book")
                    .setMessage("Do you want to remove \"" + book.getTitle() + "\" from your saved books?")
                    .setPositiveButton("Remove", (dialog, which) ->
                            bookViewModel.deleteBook(book)
                    )
                    .setNegativeButton("Cancel", null)
                    .show();
        });

    }
}