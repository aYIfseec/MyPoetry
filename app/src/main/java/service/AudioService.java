package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import callback.MediaPlayCallBack;
import callback.MediaStopPlayCallBack;


public class AudioService
        extends Service
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static String tag = "AudioService";

    private MediaPlayer mediaPlayer;
    private MyBinder myBinder = new MyBinder();
    protected Context context;

    private MediaStopPlayCallBack stopPlayCallBack;
    private MediaPlayCallBack playCallBack;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        initPlayer(playCallBack, stopPlayCallBack);
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(tag, "onBind");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(tag, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(tag, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {//播放完时
        Log.e(tag,"播放完");
        if (stopPlayCallBack != null) {
            stopPlayCallBack.stopPlayCall();
        }
        //stopSelf();
    }

    /****/
    public void initPlayer(MediaPlayCallBack playCallBack, MediaStopPlayCallBack stopPlayCallBack) {
        try {
            if (mediaPlayer == null) {
                this.playCallBack = playCallBack;
                this.stopPlayCallBack = stopPlayCallBack;
                mediaPlayer = new MediaPlayer();
                //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //mediaPlayer.setOnBufferingUpdateListener(this);
                //mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnCompletionListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlay(MediaPlayCallBack playCallBack, MediaStopPlayCallBack stopPlayCallBack, String url) {
        try {
            destoryMediaPlayer();
            initPlayer(playCallBack, stopPlayCallBack);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IllegalArgumentException|SecurityException|IllegalStateException|IOException e) {
            e.printStackTrace();
        }
    }

    public void destoryMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(null);
                //mediaPlayer.setOnPreparedListener(null);
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            if (stopPlayCallBack == null) {
                Log.w(tag, "MediaStopPlayCallBack is null");
                return;
            }
            stopPlayCallBack.stopPlayCall();
        } catch (Exception e) {
            Log.e(tag, "destoryMediaPlayer 出错" + e.toString());
        }
    }

    public void pause() {
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mediaPlayer.setOnBufferingUpdateListener(this);
            //mediaPlayer.setOnPreparedListener(this);
        }
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(tag, "出错XXXXX");
        return true;
    }

    /****/
    public class MyBinder extends Binder {
        public AudioService getService(){
            Log.e(tag,"return service "+AudioService.this);
            return AudioService.this;
        }
    }

    public void play() {
        if (mediaPlayer.isPlaying() == false) {
            playCallBack.playCallBack();
            mediaPlayer.start();
        }
    }
/************/
    public int getMusicDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public void setMusicCurrentPosition(int currentPosition) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(currentPosition);
        }
    }
    public int getMusicCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }
}

