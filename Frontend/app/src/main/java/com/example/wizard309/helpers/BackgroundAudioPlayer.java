package com.example.wizard309.helpers;

import android.content.Context;
import android.media.MediaPlayer;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * plays audio in the background for the states
 */
public class BackgroundAudioPlayer {

    private MediaPlayer mediaPlayer;
    private Context context;

    /**
     * consrtuctor of the background audio player
     * @param context
     * @param resourceId
     */
    public BackgroundAudioPlayer(Context context, int resourceId) {
        this.context = context;
        mediaPlayer = MediaPlayer.create(context, resourceId);
        mediaPlayer.setLooping(true); // Set looping
    }

    /**
     * switches the audio
     * @param resourceId
     */
    public void switchAudio(int resourceId) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, resourceId);
        mediaPlayer.start();
        mediaPlayer.setLooping(true); // Set looping
    }

    /**
     * plays the audio
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /**
     * pauses the audio
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * stops the audio
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
