package com.example.android.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MusicFragment extends Fragment {
    MediaPlayer mediaPlayer;
    private ArtistTrack currentTrack;
    private SeekBar volumeControl = null;
    TextView completed;
    Handler seekHandler = new Handler();
    ArrayList<ArtistTrack> list;
    private MediaController mediaController;
    private Button btnPlay;
    private int currentTrackIndex;
    TextView album;
    ImageView albumCover;
    TextView trackView;
    TextView trackDuration;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Intent intent = getActivity().getIntent();
        Bundle iBundle = intent.getExtras();
        Bundle uBundle = iBundle.getBundle("bundle");
        list = uBundle.getParcelableArrayList("list");
        currentTrackIndex = uBundle.getInt("index");
        currentTrack = list.get(currentTrackIndex);
        Log.i("", list.toString());
    }

    public void updateView(){
        album.setText(currentTrack.getAlbumName());

        Picasso.with(getActivity()).load(currentTrack.getAlbumCover()).placeholder(R.drawable.user).into(albumCover);

        trackView.setText(currentTrack.getTrackName());
        long milliseconds = currentTrack.getSongLength();
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);


        trackDuration.setText(minutes + ":" + seconds);



        long completedmilliseconds = mediaPlayer.getCurrentPosition();

        int secondsComplete = (int) (completedmilliseconds / 1000) % 60 ;
        int minutesComplete = (int) ((completedmilliseconds / (1000*60)) % 60);

        completed.setText(minutesComplete + ":" + secondsComplete);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music, container, false);
        TextView artist = (TextView) view.findViewById(R.id.artist_name);
        artist.setText(currentTrack.getArtistName());

        album = (TextView) view.findViewById(R.id.album_name);

        trackView = (TextView) view.findViewById(R.id.track_name);

        albumCover = (ImageView) view.findViewById(R.id.track_preview);

        btnPlay = (Button) view.findViewById(R.id.play);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        ArrayList<ArtistTrack> artistTracks = bundle.getParcelableArrayList("tracks");

                volumeControl.setMax(mediaPlayer.getDuration());
                try {
                    mediaPlayer.setDataSource(currentTrack.getUrl());
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                seekUpdation();
            }
        });

        Button next = (Button) view.findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTrackIndex < (list.size() - 1)){
                    playSong(currentTrackIndex + 1);
                    currentTrackIndex = currentTrackIndex + 1;
                }else{
                    // play first song
                    playSong(0);
                    currentTrackIndex = 0;
                }
                currentTrack = list.get(currentTrackIndex);
                updateView();
            }
        });

        Button previous = (Button) view.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTrackIndex > 0){
                    playSong(currentTrackIndex - 1);
                    currentTrackIndex = currentTrackIndex - 1;
                }else{
                    // play last song
                    playSong(list.size() - 1);
                    currentTrackIndex = list.size() - 1;
                }
                currentTrack = list.get(currentTrackIndex);
                updateView();
            }
        });
        volumeControl = (SeekBar) view.findViewById(R.id.volume_bar);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(), "seek bar progress:" + progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.i("", "");
            }
        });
        trackDuration = (TextView)view.findViewById(R.id.track_duration);
        completed = (TextView)view.findViewById(R.id.track_completed);

        updateView();



        return view;
    }

    public void seekUpdation() {
        int completed = mediaPlayer.getCurrentPosition();
        volumeControl.setProgress(completed);
        seekHandler.postDelayed(run, 1000);
    }

    Runnable run = new Runnable() { @Override public void run() { seekUpdation(); } };

    public void  playSong(int songIndex){
        // Play song
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(list.get(songIndex).getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
            // Displaying Song title
//            String songTitle = list.get(songIndex).get("songTitle");
//            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            btnPlay.setBackgroundResource(R.drawable.ic_action_pause);

//            // set Progress bar values
//            songProgressBar.setProgress(0);
//            songProgressBar.setMax(100);
//
//            // Updating progress bar
//            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
