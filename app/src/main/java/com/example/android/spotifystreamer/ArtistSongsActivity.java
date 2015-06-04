package com.example.android.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class ArtistSongsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_songs);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.tracks_list_container, new ArtistSongsFragment()).commit();
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(Intent.EXTRA_TEXT, getIntent().getData());

            ArtistSongsFragment fragment = new ArtistSongsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tracks_list_container, fragment)
                    .commit();
        }
    }


}
