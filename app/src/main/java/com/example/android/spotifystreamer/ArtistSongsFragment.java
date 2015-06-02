package com.example.android.spotifystreamer;

import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistSongsFragment extends ListFragment {
    private String mArtistId;
    private ArrayAdapter<Track> mTrackAdapter;
    private List<Track> mTracks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mArtistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        mTracks = new ArrayList<Track>();
        mTrackAdapter = new TracksAdapter(mTracks);
        setListAdapter(mTrackAdapter);
        new FetchTopSongs().execute(mArtistId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_artist_songs, container, false);
    }

    public class FetchTopSongs extends AsyncTask<String, Void, List<Track>>{
        @Override
        protected void onPostExecute(List<Track> tracks) {
            if (!(tracks == null))
                mTrackAdapter.clear();
            mTracks.addAll(tracks);
            if (!mTrackAdapter.isEmpty())
                mTrackAdapter.notifyDataSetChanged();
        }

        @Override
        protected List<Track> doInBackground(String... params) {
            String artistid = params[0];
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("country","US");
            Tracks tracks = spotify.getArtistTopTrack(artistid, query);

            List<Track> mTracks = tracks.tracks;

            return mTracks;
        }
    }

    private class TracksAdapter extends ArrayAdapter<Track> {

        public TracksAdapter(List<Track> tracks) {
            super(getActivity(), 0, tracks);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_track_view, null);
            }

            final Track track = getItem(position);
            TextView trackTextView =
                    (TextView) convertView.findViewById(R.id.track_name);
            trackTextView.setText(track.name);

            TextView albumTextView =
                    (TextView) convertView.findViewById(R.id.album_name);
            albumTextView.setText(track.album.name);


            final ImageView img = (ImageView) convertView.findViewById(R.id.track_preview);
            String thumbnail = track.album.images.get(track.album.images.size() - 2).url;

            Picasso.with(getContext()).load(thumbnail).placeholder(R.drawable.user).into(img);
            return convertView;
        }
    }
}
