package com.happy.moment.clip.doll.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.happy.moment.clip.doll.R;

import java.util.HashMap;

/**
 * Created by Devin on 2017/12/8 19:56
 * E-mail:971060378@qq.com
 */

public class SoundPoolUtil {

    private static SoundPoolUtil soundPoolUtil;
    private final HashMap<Integer, Integer> hashMap;
    private SoundPool soundPool;

    //单例模式
    public static SoundPoolUtil getInstance(Context context) {
        if (soundPoolUtil == null)
            soundPoolUtil = new SoundPoolUtil(context);
        return soundPoolUtil;
    }

    private SoundPoolUtil(Context context) {
        //当前系统的SDK版本大于等于21(Android 5.0)时
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频数量
            builder.setMaxStreams(6);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        }
        //当系统的SDK版本小于21时
        else {//设置最多可容纳6个音频流，音频的品质为5
            soundPool = new SoundPool(6, AudioManager.STREAM_SYSTEM, 5);
        }
        hashMap = new HashMap<>();
        hashMap.put(0, soundPool.load(context, R.raw.ready_go, 1));
        hashMap.put(1, soundPool.load(context, R.raw.handler_sound, 1));
        hashMap.put(2, soundPool.load(context, R.raw.five_s_remaind, 1));
        hashMap.put(3, soundPool.load(context, R.raw.catch_down, 1));
        hashMap.put(4, soundPool.load(context, R.raw.game_success, 1));
        hashMap.put(5, soundPool.load(context, R.raw.game_fail, 1));
    }

    public void play(int number) {
        //播放音频
        soundPool.play(hashMap.get(number), 1, 1, 0, 0, 1);
    }

    public void releaseResouce() {
        soundPool.release();
        hashMap.clear();
    }
}
