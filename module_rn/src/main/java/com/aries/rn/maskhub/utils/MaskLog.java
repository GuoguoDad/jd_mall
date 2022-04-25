package com.aries.rn.maskhub.utils;

import android.util.Log;

public class MaskLog {

    private static OnLogger logger = null;
    private static String tag = "MaskHub";

    private MaskLog() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

   public interface OnLogger {
        void v(String tag, String msg);

        void d(String tag, String msg);

        void i(String tag, String msg);

        void w(String tag, String msg);

        void e(String tag, String msg, Throwable throwable);

        void e(String tag, String msg);

        void e(String tag, Throwable throwable);

    }


    public static void v(String msg) {
        if (null != logger) logger.v(tag, msg);
    }

    public static void d(String msg) {
        if (null != logger) logger.d(tag, msg);
    }

    public static void i(String msg) {
        if (null != logger) logger.i(tag, msg);
    }

    public static void w(String msg) {
        if (null != logger) logger.w(tag, msg);
    }

    public static void e(String msg, Throwable throwable) {
        if (null != logger) logger.e(tag, msg, throwable);
    }

    public static void e(String msg) {
        if (null != logger) logger.e(tag, msg);
    }

    public static void e(Throwable e) {
        if (null != logger) logger.e(tag, e);
    }

    public static void setLogger(OnLogger logger) {
        MaskLog.logger = logger;
    }

    public static void setTag(String tag) {
        MaskLog.tag = tag;
    }


    public static class AndroidLog implements OnLogger {

        @Override
        public void v(String tag, String msg) {
            Log.v(tag, msg);
        }

        @Override
        public void d(String tag, String msg) {
            Log.d(tag, msg);
        }

        @Override
        public void i(String tag, String msg) {
            Log.i(tag, msg);
        }

        @Override
        public void w(String tag, String msg) {
            Log.w(tag, msg);
        }

        @Override
        public void e(String tag, String msg, Throwable throwable) {
            Log.e(tag, msg, throwable);
        }

        @Override
        public void e(String tag, String msg) {
            Log.e(tag, msg);
        }

        @Override
        public void e(String tag, Throwable throwable) {
            Log.e(tag, trace(throwable));
        }
    }

    private static String trace(Throwable throwable) {
        StackTraceElement[] elements = throwable.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : elements) {
            sb.append(stackTraceElement.toString()).append('\n');
        }
        return sb.toString();
    }
}
