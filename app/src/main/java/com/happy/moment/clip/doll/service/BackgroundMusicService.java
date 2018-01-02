package com.happy.moment.clip.doll.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.happy.moment.clip.doll.R;

import java.io.IOException;

/**
 * Created by Devin on 2017/12/2 18:30
 * E-mail:971060378@qq.com
 */

public class BackgroundMusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onStart(Intent intent, int startId) {
        // 开始播放音乐
        mediaPlayer.start();
        // 音乐播放完毕的事件处理
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                // 循环播放
                try {
                    mediaPlayer.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
        // 播放音乐时发生错误的事件处理
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                // 释放资源
                try {
                    mediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        // 初始化音乐资源
        try {
            // 创建MediaPlayer对象
            mediaPlayer = new MediaPlayer();
            // 将音乐保存在res/raw/xingshu.mp3,R.java中自动生成{public static final int xingshu=0x7f040000;}
            mediaPlayer = MediaPlayer.create(BackgroundMusicService.this, R.raw.background_music);
            // 在MediaPlayer取得播放资源与stop()之后要准备PlayBack的状态前一定要使用MediaPlayer.prepeare()
            mediaPlayer.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // 服务停止时停止播放音乐并释放资源
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
