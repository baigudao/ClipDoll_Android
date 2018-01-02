package com.happy.moment.clip.doll.util;

import android.content.Context;
import android.content.Intent;

import com.happy.moment.clip.doll.service.BackgroundMusicService;

/**
 * Created by Devin on 2017/12/9 00:30
 * E-mail:971060378@qq.com
 */

public class BackgroundMusicPlayerUtil {

    private static BackgroundMusicPlayerUtil backgroundMusicPlayerUtil;
    private Context mContext;

    //单例模式
    public static BackgroundMusicPlayerUtil getInstance(Context context) {
        if (backgroundMusicPlayerUtil == null)
            backgroundMusicPlayerUtil = new BackgroundMusicPlayerUtil(context);
        return backgroundMusicPlayerUtil;
    }

    private BackgroundMusicPlayerUtil(Context context) {
        this.mContext = context;
    }

    public void playMusic() {
        mContext.startService(new Intent(mContext, BackgroundMusicService.class));
    }

    public void stopMusic() {
        mContext.stopService(new Intent(mContext, BackgroundMusicService.class));
    }
}
