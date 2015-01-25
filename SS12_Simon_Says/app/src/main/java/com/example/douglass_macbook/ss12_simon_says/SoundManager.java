package com.example.douglass_macbook.ss12_simon_says;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/*
 * Manages the playing of sound files
 * Class based on the following (inactive) link:
 * http://www.droidnova.com/creating-sound-effects-in-android-part-1,570.html
 * Reference for updates to SoundPool constructor:
 * https://code.google.com/p/android-developer-preview/issues/detail?id=1812
 */
public class SoundManager {
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundPoolMap;
    private AudioManager mAudioManager;
    private Context mContext;

    public static int MAX_STREAMS = 4;

    public void initSounds(Context theContext) {
        mContext = theContext;
        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes)
                .setMaxStreams(MAX_STREAMS).build();
        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }
    public void addSound(int index, int SoundID)
    {
        mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
    }
    public void playSound(int index)
    {
        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
    }
}
