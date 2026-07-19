package com.example.finalprojectandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class DetailsFragment
        extends Fragment {

    private ImageView detailPoster;

    private TextView detailTitle;
    private TextView detailDate;
    private TextView detailRating;
    private TextView detailOverview;

    private Movie currentMovie;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(
                        R.layout.fragment_details,
                        container,
                        false
                );

        detailPoster =
                view.findViewById(
                        R.id.detailPoster
                );

        detailTitle =
                view.findViewById(
                        R.id.detailTitle
                );

        detailDate =
                view.findViewById(
                        R.id.detailDate
                );

        detailRating =
                view.findViewById(
                        R.id.detailRating
                );

        detailOverview =
                view.findViewById(
                        R.id.detailOverview
                );


        /*
         * إذا تم تحميل الفيلم
         * قبل إنشاء View
         */
        if (currentMovie != null) {

            updateDetails(
                    currentMovie
            );
        }

        return view;
    }


    public void updateDetails(
            Movie movie
    ) {

        currentMovie = movie;

        if (detailTitle == null
                || movie == null) {

            return;
        }

        detailTitle.setText(
                movie.getTitle()
        );

        detailDate.setText(
                "Release Date: "
                        + movie.getReleaseDate()
        );

        detailRating.setText(
                "Rating: "
                        + movie.getRating()
                        + " / 10"
        );

        detailOverview.setText(
                movie.getOverview()
        );


        if (!movie
                .getPosterUrl()
                .isEmpty()) {

            Glide.with(this)
                    .load(
                            movie.getPosterUrl()
                    )
                    .placeholder(
                            android.R.drawable
                                    .ic_menu_gallery
                    )
                    .into(
                            detailPoster
                    );

        } else {

            detailPoster.setImageResource(
                    android.R.drawable
                            .ic_menu_gallery
            );
        }
    }
}