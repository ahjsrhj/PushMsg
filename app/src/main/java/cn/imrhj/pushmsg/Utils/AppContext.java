package cn.imrhj.pushmsg.Utils;


import android.content.Context;

/**
 * Created by rhj on 16/3/18.
 */
public class AppContext {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        AppContext.mContext = mContext;
    }
}
