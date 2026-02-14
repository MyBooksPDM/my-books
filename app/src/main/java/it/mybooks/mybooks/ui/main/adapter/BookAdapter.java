package it.mybooks.mybooks.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import it.mybooks.mybooks.R;
import it.mybooks.mybooks.data.model.Book;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ProfileViewHolder> {

    private List<Book> books = new ArrayList<>();
    private OnBookClickListener listener;

    // Click listener interface
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public interface OnBookLongClickListener{
        void onBookLongClick(Book book);
    }

    private OnBookLongClickListener longClickListener;

    public void setOnBookLongClickListener(OnBookLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView authorTextView;
        private final TextView yearTextView;
        private final TextView isbnTextView;
        private final ImageView coverImageView;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.book_card_title);
            authorTextView = itemView.findViewById(R.id.book_card_author);
            yearTextView = itemView.findViewById(R.id.book_card_year);
            isbnTextView = itemView.findViewById(R.id.book_card_isbn);
            coverImageView = itemView.findViewById(R.id.book_card_cover_image);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookClick(books.get(position));
                }
            });

            itemView.setOnLongClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && longClickListener != null){
                    longClickListener.onBookLongClick(books.get(position));
                }
                return true; // Indicate that the long click was handled
            });
        }

        public void bind(Book book) {
            titleTextView.setText(book.getTitle());
            authorTextView.setText(book.getAuthorsAsString());
            yearTextView.setText(book.getPublicationYear());

            String isbn = book.getPrimaryIsbn();
            if (isbn != null) {
                isbnTextView.setText(itemView.getContext().getString(R.string.isbn_format, isbn));
            } else {
                isbnTextView.setText("");
            }

            // Load book cover image using Glide
            String imageUrl = book.getThumbnail(); // Try main thumbnail first
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = book.getSmallThumbnail(); // Fallback to small thumbnail
            }

            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery) // Placeholder while loading
                    .error(android.R.drawable.ic_menu_gallery) // Error placeholder if load fails
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache images
                    .centerCrop() // Scale image to fit
                    .into(coverImageView);
        }
    }
}
