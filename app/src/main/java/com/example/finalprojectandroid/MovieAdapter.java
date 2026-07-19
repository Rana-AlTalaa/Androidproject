package com.example.finalprojectandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movies;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(
            ArrayList<Movie> movies,
            OnMovieClickListener listener
    ) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_movie,
                        parent,
                        false
                );

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MovieViewHolder holder,
            int position
    ) {

        Movie movie = movies.get(position);

        holder.movieTitle.setText(
                movie.getTitle()
        );

        holder.movieDate.setText(
                "Release: " + movie.getReleaseDate()
        );

        holder.movieRating.setText(
                "Rating: " + movie.getRating()
        );

        if (!movie.getPosterUrl().isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load(movie.getPosterUrl())
                    .placeholder(
                            android.R.drawable.ic_menu_gallery
                    )
                    .into(holder.moviePoster);

        } else {

            holder.moviePoster.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        // Normal Click
        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {
                listener.onMovieClick(movie);
            }

        });

        // Long Click -> Popup Menu
        holder.itemView.setOnLongClickListener(v -> {

            PopupMenu popupMenu =
                    new PopupMenu(
                            v.getContext(),
                            v
                    );

            popupMenu
                    .getMenu()
                    .add("Show Details");

            popupMenu
                    .getMenu()
                    .add("Movie Info");

            popupMenu.setOnMenuItemClickListener(item -> {

                String title =
                        item.getTitle().toString();

                if (title.equals("Show Details")) {

                    if (listener != null) {
                        listener.onMovieClick(movie);
                    }

                } else if (title.equals("Movie Info")) {

                    Toast.makeText(
                            v.getContext(),
                            movie.getTitle()
                                    + "\nRating: "
                                    + movie.getRating(),
                            Toast.LENGTH_SHORT
                    ).show();

                }

                return true;
            });

            popupMenu.show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder
            extends RecyclerView.ViewHolder {

        ImageView moviePoster;

        TextView movieTitle;
        TextView movieDate;
        TextView movieRating;

        public MovieViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            moviePoster =
                    itemView.findViewById(
                            R.id.moviePoster
                    );

            movieTitle =
                    itemView.findViewById(
                            R.id.movieTitle
                    );

            movieDate =
                    itemView.findViewById(
                            R.id.movieDate
                    );

            movieRating =
                    itemView.findViewById(
                            R.id.movieRating
                    );
        }
    }
}