package com.example.android.spotifystreamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistFragment extends Fragment {
    private ArrayAdapter<Artist> mArtistAdapter;
    private ListView artistsList;
    private EditText searchArtistEditText;
    private List<Artist> mArtists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtists = new ArrayList<Artist>();
        mArtistAdapter = new ArtistAdapter(mArtists);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        searchArtistEditText = (EditText) v.findViewById(R.id.artist_search_edit_text);

        searchArtistEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    new SearchArtist().execute(searchArtistEditText.getText().toString());

                }
                return true;
            }

        });

        artistsList = (ListView) v.findViewById(R.id.artist_search_result);

        artistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = (Artist) (artistsList.getAdapter()).getItem(position);
                Log.d("Artist", artist.name + " was clicked " + artist.id);
                Intent intent = new Intent(getActivity(), ArtistSongsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, artist.id);
                startActivity(intent);
            }
        });


        artistsList.setAdapter(mArtistAdapter);

        return v;
    }

    public class SearchArtist extends AsyncTask<String, Void, List<Artist>> {

        ProgressDialog mSearching;

        @Override
        protected void onPreExecute() {
            mSearching = new ProgressDialog(getActivity());
            mSearching.setMessage("Searching...");
            mSearching.show();
        }

        @Override
        protected List<Artist> doInBackground(String... params) {
            if (params.length == 0)
                return null;
            String artistSearch = params[0];
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(artistSearch);
            //search for artist here. and return the result
            Pager<Artist> artistsPager = results.artists;
            List<Artist> mArtists = artistsPager.items;

            return mArtists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if (!(artists == null))
                mArtistAdapter.clear();
            mArtists.addAll(artists);
            if (!mArtists.isEmpty())
                mArtistAdapter.notifyDataSetChanged();
            else{
                Toast.makeText(SearchArtistFragment.this.getActivity(), "Artist not found", Toast.LENGTH_SHORT).show();
            }
            mSearching.dismiss();
        }
    }

    private class ArtistAdapter extends ArrayAdapter<Artist> {

        public ArtistAdapter(List<Artist> artists) {
            super(getActivity(), 0, artists);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_artist_view, null);
            }

            final Artist artist = getItem(position);
            TextView nameTextView =
                    (TextView) convertView.findViewById(R.id.artist_name);
            nameTextView.setText(artist.name);


            final ImageView img = (ImageView) convertView.findViewById(R.id.artist_thumbnail);
            String thumbnail = null;
            if (artist.images.size() > 0)
                thumbnail = artist.images.get(artist.images.size() - 2).url;
            Picasso.with(getContext()).load(thumbnail).placeholder(R.drawable.user).into(img);
            return convertView;
        }
    }
}
