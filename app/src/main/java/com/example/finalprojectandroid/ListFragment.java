package com.example.finalprojectandroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String API_KEY = "6bb30dc04289665bfdb6207bf247049d";

    private EditText searchEditText;
    private Button searchButton;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MovieAdapter adapter;
    private final ArrayList<Movie> movies = new ArrayList<>();

    private String currentQuery = "Avengers";

    private OnMovieSelectedListener listener;

    public interface OnMovieSelectedListener {
        void onMovieSelected(Movie movie, boolean openDetails);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnMovieSelectedListener) {
            listener = (OnMovieSelectedListener) context;
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_list,
                container,
                false
        );

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        RecyclerView recyclerView =
                view.findViewById(R.id.moviesRecyclerView);

        adapter = new MovieAdapter(
                movies,
                movie -> {
                    if (listener != null) {
                        listener.onMovieSelected(
                                movie,
                                true
                        );
                    }
                }
        );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {

            String query = searchEditText
                    .getText()
                    .toString()
                    .trim();

            if (query.isEmpty()) {

                Toast.makeText(
                        requireContext(),
                        "Enter movie name",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            currentQuery = query;

            loadMovies(currentQuery);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadMovies(currentQuery);
        });

        loadMovies(currentQuery);

        return view;
    }

    private void loadMovies(String query) {

        if (isNetworkAvailable()) {

            new FetchMoviesTask().execute(query);

        } else {

            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }

            Toast.makeText(
                    requireContext(),
                    "No internet connection",
                    Toast.LENGTH_SHORT
            ).show();

            if (getActivity() instanceof MainActivity) {

                ((MainActivity) getActivity())
                        .showNotification(
                                "Error",
                                "No internet connection"
                        );
            }
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager =
                (ConnectivityManager)
                        requireActivity()
                                .getSystemService(
                                        Context.CONNECTIVITY_SERVICE
                                );

        if (manager == null) {
            return false;
        }

        NetworkCapabilities capabilities =
                manager.getNetworkCapabilities(
                        manager.getActiveNetwork()
                );

        if (capabilities == null) {
            return false;
        }

        return capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
        ) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
        ) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_ETHERNET
        );
    }

    private class FetchMoviesTask
            extends AsyncTask<
            String,
            Void,
            ArrayList<Movie>
            > {

        private boolean hasError = false;
        private String errorMessage = "";

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(
                    View.VISIBLE
            );

            searchButton.setEnabled(
                    false
            );
        }

        @Override
        protected ArrayList<Movie> doInBackground(
                String... params
        ) {

            ArrayList<Movie> result =
                    new ArrayList<>();

            HttpURLConnection connection =
                    null;

            BufferedReader reader =
                    null;

            try {

                String query = params[0];

                Uri uri = Uri.parse(
                                "https://api.themoviedb.org/3/search/movie"
                        )
                        .buildUpon()
                        .appendQueryParameter(
                                "api_key",
                                API_KEY
                        )
                        .appendQueryParameter(
                                "query",
                                query
                        )
                        .appendQueryParameter(
                                "language",
                                "en-US"
                        )
                        .build();

                URL url =
                        new URL(
                                uri.toString()
                        );

                connection =
                        (HttpURLConnection)
                                url.openConnection();

                connection.setRequestMethod(
                        "GET"
                );

                connection.setConnectTimeout(
                        15000
                );

                connection.setReadTimeout(
                        15000
                );

                connection.setDoInput(
                        true
                );

                connection.connect();

                int responseCode =
                        connection.getResponseCode();

                if (responseCode !=
                        HttpURLConnection.HTTP_OK) {

                    hasError = true;

                    errorMessage =
                            "Server Error: "
                                    + responseCode;

                    return result;
                }

                InputStream inputStream =
                        connection.getInputStream();

                reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        inputStream,
                                        "UTF-8"
                                )
                        );

                StringBuilder builder =
                        new StringBuilder();

                String line;

                while ((line =
                        reader.readLine())
                        != null) {

                    builder.append(line);
                }

                JSONObject root =
                        new JSONObject(
                                builder.toString()
                        );

                JSONArray results =
                        root.optJSONArray(
                                "results"
                        );

                if (results == null) {
                    return result;
                }

                for (int i = 0;
                     i < results.length();
                     i++) {

                    JSONObject movieObject =
                            results.optJSONObject(i);

                    if (movieObject == null) {
                        continue;
                    }

                    String title =
                            movieObject.optString(
                                    "title",
                                    "No Title"
                            );

                    String overview =
                            movieObject.optString(
                                    "overview",
                                    "No description available."
                            );

                    String releaseDate =
                            movieObject.optString(
                                    "release_date",
                                    "Unknown"
                            );

                    double rating =
                            movieObject.optDouble(
                                    "vote_average",
                                    0
                            );

                    String posterPath =
                            movieObject.optString(
                                    "poster_path",
                                    ""
                            );

                    Movie movie =
                            new Movie(
                                    title,
                                    overview,
                                    releaseDate,
                                    rating,
                                    posterPath
                            );

                    result.add(movie);
                }

            } catch (Exception e) {

                hasError = true;

                errorMessage =
                        e.getClass()
                                .getSimpleName()
                                + ": "
                                + e.getMessage();

                e.printStackTrace();

            } finally {

                try {

                    if (reader != null) {
                        reader.close();
                    }

                } catch (Exception ignored) {
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(
                ArrayList<Movie> result
        ) {

            if (progressBar != null) {
                progressBar.setVisibility(
                        View.GONE
                );
            }

            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(
                        false
                );
            }

            if (searchButton != null) {
                searchButton.setEnabled(
                        true
                );
            }

            if (!isAdded()) {
                return;
            }

            if (hasError) {

                Toast.makeText(
                        requireContext(),
                        "Error: "
                                + errorMessage,
                        Toast.LENGTH_LONG
                ).show();

                if (getActivity()
                        instanceof MainActivity) {

                    ((MainActivity) getActivity())
                            .showNotification(
                                    "Error",
                                    errorMessage
                            );
                }

                return;
            }

            movies.clear();
            movies.addAll(result);

            adapter.notifyDataSetChanged();

            if (!movies.isEmpty()) {

                Toast.makeText(
                        requireContext(),
                        "Movies loaded successfully",
                        Toast.LENGTH_SHORT
                ).show();

                if (getActivity()
                        instanceof MainActivity) {

                    ((MainActivity) getActivity())
                            .showNotification(
                                    "Success",
                                    "Movies loaded successfully"
                            );
                }

                if (listener != null) {

                    listener.onMovieSelected(
                            movies.get(0),
                            false
                    );
                }

            } else {

                Toast.makeText(
                        requireContext(),
                        "No movies found",
                        Toast.LENGTH_SHORT
                ).show();

                if (getActivity()
                        instanceof MainActivity) {

                    ((MainActivity) getActivity())
                            .showNotification(
                                    "No Results",
                                    "No movies found"
                            );
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}