# Movie Finder - Android Final Project

Movie Finder is an Android application developed as a final project for the Mobile App Development 2 course.

The application integrates with The Movie Database (TMDB) API to fetch and display real-time movie data. Users can search for movies, browse the results, and view detailed information about each movie, including the movie poster, title, release date, rating, and overview.

## Features

- Integration with TMDB API for real movie data.
- RecyclerView to display movie results dynamically.
- TabLayout with ViewPager for navigation.
- Two Fragments:
  - Movies Fragment for displaying the movie list.
  - Details Fragment for displaying selected movie information.
- Automatically displays the first movie details if no movie is selected.
- AsyncTask for fetching API data in the background.
- ProgressBar displayed while loading data.
- Search functionality to search for movies by name.
- Pull-to-Refresh to reload the latest search results.
- Passing selected movie data between Fragments.
- Popup Menu for movie actions.
- Options Menu with About and Help options.
- Notifications for successful data loading and API/network errors.
- Internet connection checking and error handling.
- Movie poster loading using Glide.
- Material Design user interface with custom CardView layouts.

## Technologies Used

- Java
- Android Studio
- TMDB API
- JSON
- RecyclerView
- Fragments
- TabLayout
- ViewPager
- AsyncTask
- SwipeRefreshLayout
- Glide
- Material Design
- Android Notifications

## API

This application uses The Movie Database (TMDB) API to retrieve movie information.

This product uses the TMDB API but is not endorsed or certified by TMDB.

## Project Purpose

This project was developed for educational purposes as part of the Mobile App Development 2 Final Project.
