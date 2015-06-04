package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 03/06/15.
 */
public class ArtistTrack implements Parcelable {
    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    private String artistName;
    private String trackName;
    private String albumName;

    public long getSongLength() {
        return songLength;
    }

    public void setSongLength(long songLength) {
        this.songLength = songLength;
    }

    public static Creator<ArtistTrack> getCREATOR() {
        return CREATOR;
    }

    private long songLength;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

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

    public ArtistTrack(String artistName, String trackName, String albumName, String albumCover, String url, long songLength) {
        this.artistName = artistName;
        this.trackName = trackName;
        this.albumName = albumName;
        this.albumCover = albumCover;
        this.url = url;
        this.songLength = songLength;
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
        dest.writeString(albumName);
        dest.writeString(artistName);
        dest.writeString(url);
        dest.writeLong(songLength);
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ArtistTrack> CREATOR = new Parcelable.Creator<ArtistTrack>() {
        public ArtistTrack createFromParcel(Parcel in) {
            return new ArtistTrack(in);
        }

        public ArtistTrack[] newArray(int size) {
            return new ArtistTrack[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private ArtistTrack(Parcel in) {
        albumCover = in.readString();
        trackName= in.readString();
        albumName = in.readString();
        artistName = in.readString();
        url = in.readString();
        songLength = in.readLong();
    }
}
