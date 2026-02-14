package it.mybooks.mybooks.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.ui.main.adapter.BookAdapter;
import it.mybooks.mybooks.ui.main.viewmodel.BookViewModel;

public class SearchFragment extends Fragment {

    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;
    private TextInputEditText searchBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.results_recyclerview);
        searchBar = view.findViewById(R.id.search_edit_text);

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        setupRecyclerView();
        setupSearchInput();

        observeViewModel();
    }

    private void setupRecyclerView() {
        bookAdapter = new BookAdapter();

        bookAdapter.setOnBookClickListener(book -> {
            SearchFragmentDirections.ActionSearchFragmentToBookDetailFragment action =
                    SearchFragmentDirections.actionSearchFragmentToBookDetailFragment(book);
            NavHostFragment.findNavController(SearchFragment.this).navigate(action);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);
    }

    private void setupSearchInput() {
        if (bookViewModel.getCurrentQuery() != null && !bookViewModel.getCurrentQuery().isEmpty()) {
            searchBar.setText(bookViewModel.getCurrentQuery());
        }
        if (searchBar != null) {
            searchBar.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    CharSequence text = searchBar.getText();
                    String query = (text != null) ? text.toString() : "";
                    if (!query.trim().isEmpty()) {
                        bookViewModel.searchBooks(query);
                    }
                    View currentFocus = requireActivity().getCurrentFocus();
                    if (currentFocus != null) {
                        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                        currentFocus.clearFocus();
                    }
                    return true;
                }
                return false;
            });
        }
    }

    private void observeViewModel() {
        bookViewModel.getSearchResults().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
            }
        });
    }
}
