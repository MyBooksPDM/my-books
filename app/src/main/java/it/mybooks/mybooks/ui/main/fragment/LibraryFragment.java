package it.mybooks.mybooks.ui.main.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import it.mybooks.mybooks.ui.onboarding.viewmodel.AuthViewModel;

public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private TextView savedBooksCountTextView;
    private BookViewModel bookViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.profile_recycler_view);
        savedBooksCountTextView = view.findViewById(R.id.profile_saved_books_count);

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        View profileCard = view.findViewById(R.id.profile_card);

        //logout on long click just for testing purposes, will be moved to settings in the future
        profileCard.setOnLongClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Sign out")
                    .setMessage("Do you want to sign out?")
                    .setPositiveButton("Sign out", (dialog, which) -> signOut(authViewModel))
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        setupRecyclerView();

        // Observe books data from ViewModel
        bookViewModel.getSavedBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
                updateSavedBooksCount(books.size());
            }
        });
    }

    private void signOut(AuthViewModel authViewModel) {
        authViewModel.signOut();
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
            LibraryFragmentDirections.ActionLibraryFragmentToBookDetailFragment action =
                    LibraryFragmentDirections.actionLibraryFragmentToBookDetailFragment(book);
            NavHostFragment.findNavController(LibraryFragment.this).navigate(action);
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