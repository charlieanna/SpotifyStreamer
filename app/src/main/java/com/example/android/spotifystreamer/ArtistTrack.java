package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 03/06/15.
 */
public class ArtistTrack implements Parcelable {
    private String trackName;
    private String albumName;

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public ArtistTrack(String trackName, String albumName, String albumCover) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.albumCover = albumCover;
    }

    private String albumCover;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumCover);
        dest.writeString(trackName);
        dest.writeString(trackName);
    }
}
