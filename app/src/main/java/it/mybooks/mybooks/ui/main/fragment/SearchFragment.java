package it.mybooks.mybooks.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.ui.main.adapter.BookAdapter;
import it.mybooks.mybooks.ui.main.viewmodel.BookViewModel;

public class SearchFragment extends Fragment {

    private BookViewModel mViewModel;
    private BookAdapter bookAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        setupRecyclerView(view);
        setupSearchInput(view);
        observeViewModel();
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewExplore);
        bookAdapter = new BookAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);

        bookAdapter.setOnBookClickListener(book -> {

        });
    }

    private void setupSearchInput(View view) {
        EditText editText = view.findViewById(R.id.search_edit_text);

        if (editText != null) {
            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(editText.getText().toString());
                    return true;
                }
                return false;
            });
        }
    }

    private void observeViewModel() {
        mViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
            }
        });
    }

    private void performSearch(String query) {
        mViewModel.search(query);
        View currentFocus = requireActivity().getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            currentFocus.clearFocus();
        }
    }
}
