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
    ArrayList<ArtistTrack> list;
    private ArrayAdapter<ArtistTrack> mTrackAdapter;
    private List<ArtistTrack> mTracks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mArtistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        mTracks = new ArrayList<ArtistTrack>();
        mTrackAdapter = new TracksAdapter(mTracks);
        setListAdapter(mTrackAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_artist_songs, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mTracks != null){
            list = new ArrayList<ArtistTrack>();
            for(int i = 0; i < mTracks.size(); i++) {
                ArtistTrack track = mTracks.get(i);
                list.add(track);
            }
        }
        outState.putParcelableArrayList("key", list);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            ArrayList<ArtistTrack> artistTracks = savedInstanceState.getParcelableArrayList("key");
            if (!(artistTracks == null))
                mTrackAdapter.clear();
            mTracks.addAll(artistTracks);
            if (!mTrackAdapter.isEmpty())
                mTrackAdapter.notifyDataSetChanged();
        }
        else{
            new FetchTopSongs().execute(mArtistId);
        }
    }


    public class FetchTopSongs extends AsyncTask<String, Void, List<ArtistTrack>>{
        @Override
        protected void onPostExecute(List<ArtistTrack> tracks) {
            if (!(tracks == null))
                mTrackAdapter.clear();
            mTracks.addAll(tracks);
            if (!mTrackAdapter.isEmpty())
                mTrackAdapter.notifyDataSetChanged();
        }

        @Override
        protected List<ArtistTrack> doInBackground(String... params) {
            String artistid = params[0];
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("country","US");
            Tracks tracks = spotify.getArtistTopTrack(artistid, query);

            List<Track> lTracks = tracks.tracks;
            ArrayList<ArtistTrack> list = new ArrayList<ArtistTrack>();
            for(int i=0; i< lTracks.size(); i++){
                Track track = lTracks.get(i);
                String thumbnail = track.album.images.get(track.album.images.size() - 2).url;
                ArtistTrack artistTrack = new ArtistTrack(track.name, track.album.name, thumbnail);
                list.add(artistTrack);
            }

            return list;
        }
    }

    private class TracksAdapter extends ArrayAdapter<ArtistTrack> {

        public TracksAdapter(List<ArtistTrack> tracks) {
            super(getActivity(), 0, tracks);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_track_view, null);
            }

            final ArtistTrack track = getItem(position);
            TextView trackTextView =
                    (TextView) convertView.findViewById(R.id.track_name);
            trackTextView.setText(track.getTrackName());

            TextView albumTextView =
                    (TextView) convertView.findViewById(R.id.album_name);
            albumTextView.setText(track.getAlbumName());

            final ImageView img = (ImageView) convertView.findViewById(R.id.track_preview);
            Picasso.with(getContext()).load(track.getAlbumCover()).placeholder(R.drawable.user).into(img);
            return convertView;
        }
    }
}
