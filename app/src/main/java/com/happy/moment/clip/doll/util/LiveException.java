package com.happy.moment.clip.doll.util;

/**
 * Created by Devin on 2017/11/23 16:34
 * E-mail:971060378@qq.com
 */

public class LiveException extends Exception {

    public static int ERR_DEFAULT = 0;
    public static int ERR_VIDEO_LOAD_FAIL = 1;
    public static int ERR_JOIN_ROOM = 2;

    public int type = 0;

    public LiveException() {
        super();
    }

    public LiveException(String message) {
        super(message);
    }

    public LiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiveException(Throwable cause) {
        super(cause);
    }

    LiveException(int type, String msg, Throwable cause) {
        super(msg);
        this.type = type;
    }
}
