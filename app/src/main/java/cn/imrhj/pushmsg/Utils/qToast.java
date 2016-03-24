package cn.imrhj.pushmsg.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by rhj on 16/3/16.
 */
public class qToast {
    public static void s(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void l(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
